package com.til.particle_system.element.particle_life_time.speed;

import com.til.json_read_write.annotation.BaseClass;
import com.til.json_read_write.annotation.JsonField;
import com.til.json_read_write.annotation.SonClass;
import com.til.math.ITime;

/***
 * 生命周期内阻力
 */
@BaseClass(sonClass = LifeTimeSpeedResistanceElement.class)
@SonClass()
public class LifeTimeSpeedResistanceElement {

    /***
     * 阻力*位移方向取反(0-1)
     */
    @JsonField
    public ITime.ITimeNumber resistance;

    /***
     * 阻力*大小
     */
    @JsonField
    public Boolean multiplySize;

    /***
     * 阻力*速度
     */
    @JsonField
    public Boolean multiplySpeed;

}
