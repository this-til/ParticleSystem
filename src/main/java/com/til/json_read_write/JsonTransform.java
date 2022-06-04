package com.til.json_read_write;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.til.json_read_write.annotation.DefaultNew;
import com.til.json_read_write.annotation.JsonField;
import com.til.json_read_write.annotation.NeedInit;
import com.til.json_read_write.annotation.SonClass;
import com.til.particle_system.MainParticleSystem;
import com.til.util.Util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.MessageFormat;
import java.util.List;

/***
 * 这是一个转换类
 * 将对象转成Json或Json转对象
 * @param <E>
 * @author til
 */
public abstract class JsonTransform<E> {

    public final JsonAnalysis jsonAnalysis;
    public final Class<E> type;
    public final SonClass sonClass;

    public JsonTransform(Class<E> type, SonClass sonClass, JsonAnalysis jsonAnalysis) throws Exception {
        this.type = type;
        this.sonClass = sonClass;
        this.jsonAnalysis = jsonAnalysis;
    }

    public abstract JsonElement from(E e) throws Exception;

    public abstract E as(JsonElement jsonElement) throws Exception;

    public static class CurrencyJsonTransform<E> extends JsonTransform<E> {

        public CurrencyJsonTransform(Class<E> type, SonClass sonClass, JsonAnalysis jsonAnalysis) throws Exception {
            super(type, sonClass, jsonAnalysis);
        }

        @Override
        public JsonElement from(E e) throws Exception {
            JsonObject jsonObject = new JsonObject();

            for (Field declaredField : e.getClass().getDeclaredFields()) {
                try {
                    if (Modifier.isStatic(declaredField.getModifiers())) {
                        continue;
                    }
                    declaredField.setAccessible(true);
                    if (declaredField.isAnnotationPresent(JsonField.class)) {
                        JsonField jsonField = declaredField.getAnnotation(JsonField.class);
                        String name = jsonField.name().isEmpty() ? declaredField.getName() : jsonField.name();
                        Object fObj = declaredField.get(e);
                        if (fObj == null) {
                            continue;
                        }
                        if (!jsonField.fromMethod().isEmpty()) {
                            jsonObject.add(name, (JsonElement) e.getClass().getMethod(jsonField.fromMethod(), declaredField.getType()).invoke(e, fObj));
                        } else {
                            jsonObject.add(name, jsonAnalysis.from(Util.forcedConversion(declaredField.getType()), fObj));
                        }
                    }
                } catch (Exception exception) {
                    MainParticleSystem.main.logger.error(MessageFormat.format("解析[{0}]的[{1}]字段出现问题", e, declaredField), exception);
                }
            }

            return jsonObject;
        }

        @Override
        public E as(JsonElement jsonElement) throws Exception {
            E e = type.getDeclaredConstructor().newInstance();
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            for (Field declaredField : e.getClass().getDeclaredFields()) {
                try {
                    if (Modifier.isStatic(declaredField.getModifiers())) {
                        continue;
                    }
                    declaredField.setAccessible(true);
                    if (declaredField.isAnnotationPresent(JsonField.class)) {
                        JsonField jsonField = declaredField.getAnnotation(JsonField.class);
                        String name = jsonField.name().isEmpty() ? declaredField.getName() : jsonField.name();
                        if (jsonObject.has(name)) {
                            Object v;
                            if (!jsonField.fromMethod().isEmpty()) {
                                v = e.getClass().getMethod(jsonField.asMethod(), JsonElement.class).invoke(e, jsonObject.get(name));
                            } else {
                                v = jsonAnalysis.as(Util.forcedConversion(declaredField.getType()), jsonObject.get(name));
                            }
                            if (v.getClass().isAnnotationPresent(NeedInit.class)) {
                                for (String s : v.getClass().getAnnotation(NeedInit.class).initMethod()) {
                                    Method method = v.getClass().getMethod(s);
                                    if (Modifier.isStatic(method.getModifiers())) {
                                        method.invoke(null);
                                    } else {
                                        method.invoke(v);
                                    }

                                }
                            }
                            declaredField.set(e, v);
                        } else if (declaredField.getType().isAnnotationPresent(DefaultNew.class)) {
                            declaredField.set(e, newDefaultExample(declaredField.getType(), 5));
                        }
                    }
                } catch (Exception exception) {
                    MainParticleSystem.main.logger.error(MessageFormat.format("解析[{0}]时[{1}]字段出现问题", jsonElement, declaredField), exception);
                }
            }
            return e;
        }

        /***
         * 创建对象
         * @param type 类型
         * @param recursion 递归次数，为0时停止创建
         */

        public static <E> E newDefaultExample(Class<E> type, int recursion) throws Exception {
            if (recursion == 0) {
                return null;
            }
            E e = Util.forcedConversion(type.getAnnotation(DefaultNew.class).newExample().getDeclaredConstructor().newInstance());
            for (Field declaredField : e.getClass().getDeclaredFields()) {
                try {
                    declaredField.setAccessible(true);
                    if (declaredField.getType().isAnnotationPresent(DefaultNew.class)) {
                        declaredField.set(e, newDefaultExample(declaredField.getType(), recursion - 1));
                    }
                } catch (Exception exception) {
                    MainParticleSystem.main.logger.error(MessageFormat.format("new[{0}]时，字段[{1}]，出现问题", type, declaredField), exception);
                }
            }
            if (e.getClass().isAnnotationPresent(NeedInit.class)) {
                for (String s : e.getClass().getAnnotation(NeedInit.class).initMethod()) {
                    Method method = e.getClass().getMethod(s);
                    if (Modifier.isStatic(method.getModifiers())) {
                        method.invoke(null);
                    } else {
                        method.invoke(e);
                    }

                }
            }
            return e;
        }


    }

    public static class EnumCurrencyJsonTransform<E extends Enum<E>> extends JsonTransform<E> {

        public EnumCurrencyJsonTransform(Class<E> type, SonClass sonClass, JsonAnalysis jsonAnalysis) throws Exception {
            super(type, sonClass, jsonAnalysis);
        }

        @Override
        public JsonElement from(E e) {
            return new JsonPrimitive(e.toString());
        }

        @Override
        public E as(JsonElement jsonElement) {
            return Enum.valueOf(type, jsonElement.getAsString());
        }
    }

    public abstract static class ListCurrencyJsonTransform<T, E extends List<T>> extends JsonTransform<E> {


        public ListCurrencyJsonTransform(Class<E> type, SonClass sonClass, JsonAnalysis jsonAnalysis) throws Exception {
            super(type, sonClass, jsonAnalysis);
        }

        @Override
        public JsonElement from(E ts) throws Exception {
            JsonArray jsonArray = new JsonArray();
            for (T t : ts) {
                try {
                    jsonArray.add(jsonAnalysis.from(getElementType(), t));
                } catch (Exception e) {
                    MainParticleSystem.main.logger.error(MessageFormat.format("在解析[{0}]的[{1}]元素时出现错误", jsonArray, t), e);
                }
            }
            return jsonArray;
        }

        @Override
        public E as(JsonElement jsonElement) throws Exception {
            E e = type.getDeclaredConstructor().newInstance();
            for (JsonElement element : jsonElement.getAsJsonArray()) {
                try {
                    T t = jsonAnalysis.as(getElementType(), element);
                    if (t.getClass().isAnnotationPresent(NeedInit.class)) {
                        for (String s : t.getClass().getAnnotation(NeedInit.class).initMethod()) {
                            Method method = t.getClass().getMethod(s);
                            if (Modifier.isStatic(method.getModifiers())) {
                                method.invoke(null);
                            } else {
                                method.invoke(t);
                            }
                        }
                    }
                    e.add(t);
                } catch (Exception exception) {
                    MainParticleSystem.main.logger.error(MessageFormat.format("在解析[{0}]的[{1}]元素时出现错误", jsonElement, e), exception);
                }
            }
            return e;
        }

        public abstract Class<T> getElementType();

    }

    public abstract static class MapCurrencyJsonTransform<T, E extends java.util.Map<String, T>> extends JsonTransform<E> {
        public MapCurrencyJsonTransform(Class<E> type, SonClass sonClass, JsonAnalysis jsonAnalysis) throws Exception {
            super(type, sonClass, jsonAnalysis);
        }

        @Override
        public JsonElement from(E e) throws Exception {
            JsonObject jsonObject = new JsonObject();
            for (java.util.Map.Entry<String, T> entry : e.entrySet()) {
                try {
                    jsonObject.add(entry.getKey(), jsonAnalysis.from(getValueType(), entry.getValue()));
                } catch (Exception exception) {
                    MainParticleSystem.main.logger.error(MessageFormat.format("在解析[{0}]的[{1}]键值对时出现错误", e, entry), exception);
                }
            }
            return jsonObject;
        }

        @Override
        public E as(JsonElement jsonElement) throws Exception {
            E e = type.getDeclaredConstructor().newInstance();
            for (java.util.Map.Entry<String, JsonElement> stringJsonElementEntry : jsonElement.getAsJsonObject().entrySet()) {
                try {
                    e.put(stringJsonElementEntry.getKey(), jsonAnalysis.as(getValueType(), stringJsonElementEntry.getValue()));
                } catch (Exception exception) {
                    MainParticleSystem.main.logger.error(MessageFormat.format("在解析[{0}]的[{1}]键值对时出现错误", jsonElement, stringJsonElementEntry), exception);
                }
            }
            return e;
        }

        public abstract Class<T> getValueType();
    }

}
