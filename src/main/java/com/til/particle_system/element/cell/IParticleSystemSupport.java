package com.til.particle_system.element.cell;

import com.til.math.Quaternion;
import com.til.math.V3;

/***
 * 对粒子系统的支持
 * @author til
 */

public interface IParticleSystemSupport {

    /***
     * 返回粒子系统所在位置
     */
    V3 getPos();

    /***
     * 返回上一t粒子系统所在位置
     */
    V3 getOldPos();

    /***
     * 返回粒子系统的旋转
     */
    Quaternion getRotate();

    /***
     * 返回上一t粒子系统的旋转
     */
    Quaternion getOldRotate();

}
