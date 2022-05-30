package com.til.particle_system.main;


import com.til.particle_system.element.Element;
import com.til.particle_system.util.Map;

import javax.annotation.Nullable;

/**
 * 粒子系统原数据
 *
 * @author til
 */

public class ParticleSystem {
    protected Map<Class<Element>, Element> elementMap;

    public ParticleSystem() {

    }

    /***
     * 获取元素
     * @param elementClass 元素类型
     * @param <E> 元素
     * @return 元素实例
     */
    @SuppressWarnings("unchecked")
    @Nullable
    public <E> E getElement(Class<Element> elementClass) {
        Element element = elementMap.get(elementClass);
        if (element != null) {
            return (E) element;
        }
        return null;
    }

}
