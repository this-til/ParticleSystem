package com.til.particle_system.register;


import com.til.particle_system.util.List;
import com.til.particle_system.util.Map;

/**
 * @author til
 */
public class RegisterManage {

    public final Map<Class<?>, Analysis<?>> ANALYSIS_MAP = new Map<>();
    public final Map<Class<?>, List<Class<?>>> INTERCEPT = new Map<>();

    public boolean has(Class<?> key) {
        return ANALYSIS_MAP.containsKey(key);
    }

    @SuppressWarnings("unchecked")
    public <E> Analysis<E> put(Class<E> eClass) {
        if (ANALYSIS_MAP.containsKey(eClass)) {
            return (Analysis<E>) ANALYSIS_MAP.get(eClass);
        }
        Analysis<E> analysis = new Analysis<>(eClass);
        ANALYSIS_MAP.put(eClass, analysis);
        INTERCEPT.forEach((k, v) -> {
            if (k.isAssignableFrom(eClass)) {
                v.add(eClass);
            }
        });
        return analysis;
    }

    public void addIntercept(Class<?> c) {
        if (!INTERCEPT.containsKey(c)) {
            INTERCEPT.put(c, new List<>());
        }
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
