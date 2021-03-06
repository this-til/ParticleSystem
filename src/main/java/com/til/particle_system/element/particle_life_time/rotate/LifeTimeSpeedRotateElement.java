package com.til.particle_system.element.particle_life_time.rotate;

import com.til.json_read_write.annotation.BaseClass;
import com.til.json_read_write.annotation.JsonField;
import com.til.json_read_write.annotation.SonClass;
import com.til.math.ITime;
import com.til.math.V2;
import com.til.particle_system.element.IElement;

/***
 * 按照速度旋转
 * @author til
 */
@BaseClass(sonClass = LifeTimeSpeedRotateElement.class)
@SonClass
public class LifeTimeSpeedRotateElement implements IElement.IParticleElement {

    /***
     * 角速度
     */
    @JsonField
    public ITime.ITimeV3 angularVelocity;

    /***
     * 速度范围
     */
    @JsonField
    public V2 speedRange;

}
