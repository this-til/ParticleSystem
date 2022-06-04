package com.til.particle_system.element.particle_life_time.move;

import com.til.json_read_write.annotation.BaseClass;
import com.til.json_read_write.annotation.JsonField;
import com.til.json_read_write.annotation.SonClass;
import com.til.json_read_write.util.math.ITime;

/***
 * 粒子生命周期内受力
 * @author til
 */
@BaseClass(sonClass = LifeTimeForceElement.class)
@SonClass()
public class LifeTimeForceElement {

    /***
     * 粒子受力
     */
    @JsonField
    public ITime.ITimeV3 force;

}
