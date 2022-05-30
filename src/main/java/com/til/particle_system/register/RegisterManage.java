package com.til.particle_system.register;


import com.til.particle_system.MainParticleSystem;
import com.til.particle_system.util.Map;
import net.minecraft.resources.ResourceLocation;

import java.text.MessageFormat;

/**
 * @author til
 */
public class RegisterManage {

    public final Map<Class<?>, Analysis<?>> ANALYSIS_MAP = new Map<>();

    public boolean has(Class<?> key) {
        return ANALYSIS_MAP.containsKey(key);
    }

    @SuppressWarnings("unchecked")
    public <E> Analysis<E> put(Class<E> eClass) {
        if (ANALYSIS_MAP.containsKey(eClass)){
            return (Analysis<E>)ANALYSIS_MAP.get(eClass);
        }
        return (Analysis<E>) ANALYSIS_MAP.put(eClass, new Analysis<>(eClass));
    }

    public <E> void put(Class<E> eClass, Analysis<E> analysis)  {
        ANALYSIS_MAP.put(eClass, analysis);
    }

    /***
     * 获取解析器
     */
    @SuppressWarnings("unchecked")
    public <E> Analysis<E> get(Class<E> eClass) {
        if (has(eClass)) {
            Analysis<?> analysis = ANALYSIS_MAP.get(eClass);
            return (Analysis<E>) analysis;
        } else {
            return put(eClass);
        }
    }

}
