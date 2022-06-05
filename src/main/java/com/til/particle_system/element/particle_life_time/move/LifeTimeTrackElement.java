package com.til.particle_system.element.particle_life_time.move;

import com.til.json_read_write.annotation.BaseClass;
import com.til.json_read_write.annotation.JsonField;
import com.til.json_read_write.annotation.SonClass;
import com.til.math.ITime;

/***
 * 粒子轨道
 */
@BaseClass(sonClass = LifeTimeForceElement.class)
@SonClass()
public class LifeTimeTrackElement {

    /***
     * 轨道
     * 将轨道速度应用于粒子，使其围绕系统中心旋转
     */
    @JsonField
    public ITime.ITimeV3 track;

    /***
     * 轨道计算三角函数时将该值*粒子时间
     */
    @JsonField
    public ITime.ITimeNumber time;

}
