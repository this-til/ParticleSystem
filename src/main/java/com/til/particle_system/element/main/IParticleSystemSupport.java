package com.til.particle_system.element.main;

import com.til.particle_system.util.Quaternion;
import com.til.particle_system.util.V3;

/***
 * 粒子系统支持
 * @author til
 */
public interface IParticleSystemSupport {

    /***
     * 返回粒子系统所在位置
     */
    V3 getPos();

    /***
     * 返回粒子系统旋转
     */
    Quaternion getRotate();

}
