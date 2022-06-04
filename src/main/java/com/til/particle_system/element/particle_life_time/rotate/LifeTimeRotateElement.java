package com.til.particle_system.element.particle_life_time.rotate;

import com.til.json_read_write.annotation.BaseClass;
import com.til.json_read_write.annotation.JsonField;
import com.til.json_read_write.annotation.SonClass;
import com.til.math.ITime;
import com.til.particle_system.element.IElement;

/***
 * 基于粒子生命周期的旋转
 * @author til
 */
@BaseClass(sonClass = LifeTimeRotateElement.class)
@SonClass()
public class LifeTimeRotateElement implements IElement.IParticleElement {

    /***
     * 角速度
     */
    @JsonField
    public ITime.ITimeV3 angularVelocity;
}
