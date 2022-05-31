package com.til.particle_system.register;


import com.google.gson.JsonElement;
import com.til.particle_system.MainParticleSystem;
import com.til.particle_system.util.Map;
import com.til.particle_system.util.UseString;
import com.til.particle_system.util.Util;

import java.text.MessageFormat;

/**
 * 解析器
 *
 * @author til
 */
public class Analysis<E> {

    public final Class<E> eClass;
    public final Map<Class<?>, IFromJsonElement<E>> classIAnalysisElementMap = new Map<>();
    public final Map<String, Class<?>> stringClassMap = new Map<>();

    public Analysis(Class<E> eClass) {
        this.eClass = eClass;
    }

    public <EE extends E> void put(IFromJsonElement<EE> iFromJsonElement) {
        classIAnalysisElementMap.put(iFromJsonElement.getType(), Util.forcedVonversion(iFromJsonElement));
        stringClassMap.put(iFromJsonElement.getTypeName(), iFromJsonElement.getType());
    }

    public IFromJsonElement<E> get(Class<?> eClass) {
        if (!classIAnalysisElementMap.containsKey(eClass)) {
            MainParticleSystem.LOGGER.error(MessageFormat.format("在[{0}]中没有[{1}]注册项", classIAnalysisElementMap, eClass));
        }
        return classIAnalysisElementMap.get(eClass);
    }

    public IFromJsonElement<E> get(String s) {
        return get(stringClassMap.get(s));
    }

    public E as(JsonElement jsonElement) {
        if (jsonElement.isJsonObject() && jsonElement.getAsJsonObject().has(UseString.TYPE)) {
            return get(jsonElement.getAsJsonObject().get(UseString.TYPE).getAsString()).as(jsonElement);
        } else {
            return get(UseString.TYPE).as(jsonElement);
        }
    }

    public JsonElement from(E e) {
        return get(e.getClass()).from(e);
    }

}
