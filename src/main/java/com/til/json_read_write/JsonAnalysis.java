package com.til.json_read_write;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.til.json_read_write.annotation.BaseClass;
import com.til.json_read_write.annotation.SonClass;
import com.til.json_read_write.annotation.UsePrefab;
import com.til.util.Util;
import com.til.util.Map;
import com.til.util.UseString;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;

/***
 * @author til
 */
public class JsonAnalysis {
    public final Map<Class<?>, SonCell<?>> MAP = new Map<>();
    public final Map<Class<?>, JsonTransform<?>> JSON_ANALYSIS_MAP_MAP = new Map<>();
    public final Map<Class<?>, ReadIO.JsonReloadListenerCell<?>> USE_PREFAB_MAP = new Map<>();
    public final ReadIO readIO;

    public JsonAnalysis() {
        init(this);
        readIO = new ReadIO(this);
        MinecraftForge.EVENT_BUS.register(this);
    }

    public <E> JsonTransform<E> getJsonTransform(Class<E> target) throws Exception {
        if (target.isAnnotationPresent(SonClass.class)) {
            SonClass sonClass = target.getAnnotation(SonClass.class);
            if (JSON_ANALYSIS_MAP_MAP.containsKey(target)) {
                return Util.forcedConversion(JSON_ANALYSIS_MAP_MAP.get(target));
            } else {
                JsonTransform<E> transform = Util.forcedConversion(sonClass.transform().getDeclaredConstructor(Class.class, SonClass.class, JsonAnalysis.class).newInstance(target, sonClass, this));
                JSON_ANALYSIS_MAP_MAP.put(target, transform);
                return transform;
            }
        }
        return null;
    }

    public <E> SonCell<E> get(Class<E> eClass) throws Exception {
        if (MAP.containsKey(eClass)) {
            return Util.forcedConversion(MAP.get(eClass));
        } else {
            SonCell<E> sonCell = Util.forcedConversion(new SonCell<>(eClass.getAnnotation(BaseClass.class), this));
            MAP.put(eClass, sonCell);
            if (eClass.isAnnotationPresent(UsePrefab.class)) {
                USE_PREFAB_MAP.put(eClass, readIO.add(Util.splicingRoute(eClass.getAnnotation(UsePrefab.class).route()), eClass));
            }
            return sonCell;
        }
    }

    public <E> void add(Class<E> eClass, SonCell<E> sonCell) {
        MAP.put(eClass, sonCell);
    }

    /***
     * 除去注解声明的类型之外再添加编译元素
     */
    public <E> void add(Class<E> basics, Class<E> eClass) throws Exception {
        SonClass sonClass = eClass.getAnnotation(SonClass.class);
        get(basics).add(eClass, sonClass.name(), Util.forcedConversion(getJsonTransform(sonClass.transform())));
    }

    /***
     * 反序列化
     */
    public <E> E as(Class<E> eClass, JsonElement jsonElement) throws Exception {
        SonCell<E> sonCell = get(eClass);
        if (jsonElement.isJsonObject()) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            if (jsonObject.has(UseString.TYPE) && jsonObject.get(UseString.TYPE).isJsonPrimitive()) {
                String type = jsonObject.get(UseString.TYPE).getAsString();
                if (type.equals(UseString.USE_PREFAB)) {
                    if (USE_PREFAB_MAP.containsKey(eClass)) {
                        for (java.util.Map.Entry<ResourceLocation, ?> entry : USE_PREFAB_MAP.get(eClass).map.entrySet()) {
                            if (entry.getKey().equals(new ResourceLocation(jsonObject.get(UseString.PREFAB).getAsString()))) {
                                return Util.forcedConversion(entry.getValue());
                            }
                        }
                    }
                }
                return sonCell.as(type, jsonElement);
            }
        }
        return sonCell.as(UseString.DEFAULT, jsonElement);
    }

    /***
     * 序列化
     * @param baseType 父类型
     */
    public <E> JsonElement from(Class<E> baseType, E e) throws Exception {
        return get(baseType).from(e);
    }

    /***
     * 序列化
     * 父类型为对象类型
     */
    public <E> JsonElement from(E e) throws Exception {
        return from(Util.forcedConversion(e.getClass()), e);
    }

    public static class SonCell<E> {

        public final JsonAnalysis jsonAnalysis;
        public final Map<Class<E>, JsonTransform<E>> jsonTransformMap = new Map<>();
        public final Map<String, Class<E>> classMap = new Map<>();

        public SonCell(JsonAnalysis jsonAnalysis) {
            this.jsonAnalysis = jsonAnalysis;
        }

        public SonCell(BaseClass baseClass, JsonAnalysis jsonAnalysis) throws Exception {
            this.jsonAnalysis = jsonAnalysis;
            for (Class<?> aClass : baseClass.sonClass()) {
                SonClass sonClass = aClass.getAnnotation(SonClass.class);
                classMap.put(sonClass.name(), Util.forcedConversion(aClass));
                jsonTransformMap.put(Util.forcedConversion(aClass), Util.forcedConversion(jsonAnalysis.getJsonTransform(aClass)));
            }
        }

        /***
         * 添加解析元素
         */
        public void add(Class<E> type, String name, JsonTransform<E> jsonTransform) {
            classMap.put(name, type);
            jsonTransformMap.put(type, jsonTransform);
        }

        /***
         * 反序列化
         */
        public E as(Class<E> type, JsonElement jsonElement) throws Exception {
            return jsonTransformMap.get(type).as(jsonElement);
        }

        /***
         * 反序列化
         */
        public E as(String type, JsonElement jsonElement) throws Exception {
            return as(classMap.get(type), jsonElement);
        }

        /***
         * 序列化
         * 父类型为对象类型
         */
        public JsonElement from(E e) throws Exception {
            Class<?> eClass = e.getClass();
            JsonElement jsonElement = jsonTransformMap.get(eClass).from(e);
            if (jsonElement.isJsonObject()) {
                jsonElement.getAsJsonObject().add(UseString.TYPE, new JsonPrimitive(classMap.getKey(Util.forcedConversion(eClass))));
            }
            return jsonElement;
        }

    }

    public static void init(JsonAnalysis jsonAnalysis) {
        jsonAnalysis.add(Object.class, new SonCell<>(jsonAnalysis) {
            @Override
            public Object as(Class<Object> type, JsonElement jsonElement) {
                return jsonElement;
            }

            @Override
            public Object as(String type, JsonElement jsonElement) {
                return jsonElement;
            }

            @Override
            public JsonElement from(Object o) {
                return o instanceof JsonElement ? (JsonElement) o : new JsonPrimitive(o.toString());
            }
        });
        jsonAnalysis.add(Number.class, new SonCell<>(jsonAnalysis) {
            @Override
            public Number as(Class<Number> type, JsonElement jsonElement) {
                return jsonElement.getAsNumber();
            }

            @Override
            public Number as(String type, JsonElement jsonElement) {
                return jsonElement.getAsNumber();
            }

            @Override
            public JsonElement from(Number integer) {
                return new JsonPrimitive(integer);
            }
        });
        {
            SonCell<Byte> sonCell = new SonCell<>(jsonAnalysis) {
                @Override
                public Byte as(Class<Byte> type, JsonElement jsonElement) {
                    return jsonElement.getAsByte();
                }

                @Override
                public Byte as(String type, JsonElement jsonElement) {
                    return jsonElement.getAsByte();
                }

                @Override
                public JsonElement from(Byte aByte) {
                    return new JsonPrimitive(aByte);
                }
            };
            jsonAnalysis.add(byte.class, sonCell);
            jsonAnalysis.add(Byte.class, sonCell);
        }
        {
            SonCell<Short> sonCell = new SonCell<>(jsonAnalysis) {
                @Override
                public Short as(Class<Short> type, JsonElement jsonElement) {
                    return jsonElement.getAsShort();
                }

                @Override
                public Short as(String type, JsonElement jsonElement) {
                    return jsonElement.getAsShort();
                }

                @Override
                public JsonElement from(Short aShort) {
                    return new JsonPrimitive(aShort);
                }
            };
            jsonAnalysis.add(Short.class, sonCell);
            jsonAnalysis.add(short.class, sonCell);
        }
        {
            SonCell<Integer> sonCell = new SonCell<>(jsonAnalysis) {
                @Override
                public Integer as(Class<Integer> type, JsonElement jsonElement) {
                    return jsonElement.getAsInt();
                }

                @Override
                public Integer as(String type, JsonElement jsonElement) {
                    return jsonElement.getAsInt();
                }

                @Override
                public JsonElement from(Integer integer) {
                    return new JsonPrimitive(integer);
                }
            };
            jsonAnalysis.add(Integer.class, sonCell);
            jsonAnalysis.add(int.class, sonCell);
        }
        {
            SonCell<Long> sonCell = new SonCell<>(jsonAnalysis) {
                @Override
                public Long as(Class<Long> type, JsonElement jsonElement) {
                    return jsonElement.getAsLong();
                }

                @Override
                public Long as(String type, JsonElement jsonElement) {
                    return jsonElement.getAsLong();
                }

                @Override
                public JsonElement from(Long integer) {
                    return new JsonPrimitive(integer);
                }
            };
            jsonAnalysis.add(Long.class, sonCell);
            jsonAnalysis.add(long.class, sonCell);
        }
        {
            SonCell<Float> sonCell = new SonCell<>(jsonAnalysis) {
                @Override
                public Float as(Class<Float> type, JsonElement jsonElement) {
                    return jsonElement.getAsFloat();
                }

                @Override
                public Float as(String type, JsonElement jsonElement) {
                    return jsonElement.getAsFloat();
                }

                @Override
                public JsonElement from(Float integer) {
                    return new JsonPrimitive(integer);
                }
            };
            jsonAnalysis.add(Float.class, sonCell);
            jsonAnalysis.add(float.class, sonCell);
        }
        {
            SonCell<Double> sonCell = new SonCell<>(jsonAnalysis) {
                @Override
                public Double as(Class<Double> type, JsonElement jsonElement) {
                    return jsonElement.getAsDouble();
                }

                @Override
                public Double as(String type, JsonElement jsonElement) {
                    return jsonElement.getAsDouble();
                }

                @Override
                public JsonElement from(Double aDouble) {
                    return new JsonPrimitive(aDouble);
                }
            };
            jsonAnalysis.add(double.class, sonCell);
            jsonAnalysis.add(Double.class, sonCell);
        }
        {
            SonCell<Character> sonCell = new SonCell<>(jsonAnalysis) {
                @Override
                public Character as(Class<Character> type, JsonElement jsonElement) {
                    return (char) jsonElement.getAsInt();
                }

                @Override
                public Character as(String type, JsonElement jsonElement) {
                    return (char) jsonElement.getAsInt();
                }

                @Override
                public JsonElement from(Character aDouble) {
                    return new JsonPrimitive((int) aDouble);
                }
            };
            jsonAnalysis.add(char.class, sonCell);
            jsonAnalysis.add(Character.class, sonCell);
        }
        {
            SonCell<Boolean> sonCell = new SonCell<>(jsonAnalysis) {
                @Override
                public Boolean as(Class<Boolean> type, JsonElement jsonElement) {
                    return jsonElement.getAsBoolean();
                }

                @Override
                public Boolean as(String type, JsonElement jsonElement) {
                    return jsonElement.getAsBoolean();
                }

                @Override
                public JsonElement from(Boolean aBoolean) {
                    return new JsonPrimitive(aBoolean);
                }
            };
            jsonAnalysis.add(boolean.class, sonCell);
            jsonAnalysis.add(Boolean.class, sonCell);
        }

        jsonAnalysis.add(String.class, new SonCell<>(jsonAnalysis) {
            @Override
            public String as(Class<String> type, JsonElement jsonElement) {
                return jsonElement.isJsonObject() ? jsonElement.toString() : jsonElement.getAsString();
            }

            @Override
            public String as(String type, JsonElement jsonElement) {
                return jsonElement.isJsonObject() ? jsonElement.toString() : jsonElement.getAsString();
            }

            @Override
            public JsonElement from(String s) {
                return new JsonPrimitive(s);
            }
        });
        jsonAnalysis.add(JsonElement.class, new SonCell<>(jsonAnalysis) {
            @Override
            public JsonElement as(Class<JsonElement> type, JsonElement jsonElement) throws Exception {
                return jsonElement;
            }

            @Override
            public JsonElement as(String type, JsonElement jsonElement) throws Exception {
                return jsonElement;
            }

            @Override
            public JsonElement from(JsonElement jsonElement) throws Exception {
                return jsonElement;
            }
        });

    }


}
