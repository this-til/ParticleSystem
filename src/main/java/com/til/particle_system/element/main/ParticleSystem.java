package com.til.particle_system.element.main;


import com.til.particle_system.element.IElement;
import com.til.particle_system.util.Map;
import com.til.particle_system.util.Util;

/**
 * 粒子系统原数据
 *
 * @author til
 */

public class ParticleSystem {

    public Map<Class<IElement>, IElement> map = new Map<>();

    public <EE extends IElement, E> E get(Class<E> eClass) {
        return Util.forcedVonversion(map.get(eClass));
    }


}
