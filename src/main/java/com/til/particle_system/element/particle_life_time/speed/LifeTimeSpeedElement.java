package com.til.particle_system.element.particle_life_time.speed;

import com.til.json_read_write.annotation.BaseClass;
import com.til.json_read_write.annotation.JsonField;
import com.til.json_read_write.annotation.SonClass;
import com.til.math.ITime;
import com.til.particle_system.element.IElement;

/***
 * 生命周期内速度
 * @author til
 */
@BaseClass(sonClass = LifeTimeSpeedElement.class)
@SonClass()
public class LifeTimeSpeedElement implements IElement.IParticleElement {

    /***
     * 生命周期内的速度控制
     * -起始速度*该值
     */
    @JsonField
    public ITime.ITimeV3 speed;

}
