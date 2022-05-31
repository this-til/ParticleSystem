package com.til.particle_system.register;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.til.particle_system.MainParticleSystem;
import com.til.particle_system.element.use_field.UseField;
import com.til.particle_system.util.List;
import com.til.particle_system.util.UseString;
import com.til.particle_system.util.Util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/***
 * @author til
 */
public interface IFromJsonElement<E> {
    JsonElement from(E e);

    E as(JsonElement jsonElement);

    Class<E> getType();

    default String getTypeName() {
        return UseString.DEFAULT;
    }

    class CurrencyFromJsonElement<E> implements IFromJsonElement<E> {

        public Class<E> type;
        public List<Field> fieldList = new List<>();
        public String typeName;

        public CurrencyFromJsonElement(Class<E> eClass, String typeName) {
            this.type = eClass;
            for (Field field : eClass.getFields()) {
                if (field.isAnnotationPresent(UseField.class)) {
                    fieldList.add(field);
                }
            }
            this.typeName = typeName;
        }

        public CurrencyFromJsonElement(Class<E> eClass) {
            this(eClass, UseString.DEFAULT);
        }


        @Override
        public JsonElement from(E e) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.add(UseString.TYPE, new JsonPrimitive(getTypeName()));
            for (Field field : fieldList) {
                try {
                    Object o = field.get(e);
                    if (o != null) {
                        Class<?> fileType = field.getType();
                        Analysis<?> analysis = MainParticleSystem.main.registerManage.get(fileType);
                        jsonObject.add(field.getName(), analysis.from(Util.forcedVonversion(o)));
                    }
                } catch (IllegalAccessException ex) {
                    ex.printStackTrace();
                }

            }
            return jsonObject;
        }

        @Override
        public E as(JsonElement jsonElement) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            E e = null;
            try {
                e = type.getDeclaredConstructor().newInstance();
                for (Field field : fieldList) {
                    if (jsonObject.has(field.getName())) {
                        Class<?> fileType = field.getType();
                        field.set(e, MainParticleSystem.main.registerManage.get(fileType).as(jsonObject.get(field.getName())));
                    }
                }
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
                ex.printStackTrace();
            }
            return e;
        }

        @Override
        public Class<E> getType() {
            return type;
        }

        @Override
        public String getTypeName() {
            return typeName;
        }
    }

    class EnumFromJsonElement<E extends Enum<E>> implements IFromJsonElement<E> {
        public Class<E> type;

        public EnumFromJsonElement(Class<E> eClass) {
            this.type = eClass;
        }

        @Override
        public JsonElement from(E e) {
            return new JsonPrimitive(e.toString());
        }

        @Override
        public E as(JsonElement jsonElement) {
            return Enum.valueOf(type, jsonElement.getAsString());
        }

        @Override
        public Class<E> getType() {
            return type;
        }

        @Override
        public String getTypeName() {
            return UseString.DEFAULT;
        }
    }

    class ListFromJsonElement<T, E extends List<T>> implements IFromJsonElement<E> {

        public Class<E> eClass;
        public Class<T> tClass;

        public ListFromJsonElement(Class<E> eClass, Class<T> tClass) {
            this.eClass = eClass;
            this.tClass = tClass;
        }

        @Override
        public JsonElement from(E ls) {
            JsonArray jsonArray = new JsonArray();
            Analysis<T> analysis = MainParticleSystem.main.registerManage.get(tClass);
            for (T l : ls) {
                jsonArray.add(analysis.from(l));
            }
            return jsonArray;
        }

        @Override
        public E as(JsonElement jsonElement) {
            E e = null;
            JsonArray jsonElements = jsonElement.getAsJsonArray();
            try {
                e = eClass.getDeclaredConstructor().newInstance();
                Analysis<T> analysis = MainParticleSystem.main.registerManage.get(tClass);
                for (JsonElement element : jsonElements) {
                    e.add(analysis.as(element));
                }
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
                ex.printStackTrace();
            }
            return e;
        }

        @Override
        public Class<E> getType() {
            return eClass;
        }

        @Override
        public String getTypeName() {
            return UseString.DEFAULT;
        }

    }

}
