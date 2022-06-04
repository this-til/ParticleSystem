package com.til.particle_system.element.particle_life_time.size;

import com.til.json_read_write.annotation.BaseClass;
import com.til.json_read_write.annotation.JsonField;
import com.til.json_read_write.annotation.SonClass;
import com.til.math.ITime;
import com.til.math.V2;
import com.til.particle_system.element.IElement;

/***
 * 速度决定大小
 * @author til
 */
@BaseClass(sonClass = LifeTimeSpeedSizeElement.class)
@SonClass()
public class LifeTimeSpeedSizeElement implements IElement.IParticleElement {

    /***
     * 大小变换
     * -根据速度范围计算time
     * -粒子大小*改值
     */
    @JsonField
    public ITime.ITimeV3 size;

    /***
     * 速度范围
     * -根据移动速度的模计算time
     */
    @JsonField
    public V2 speedRange;
}
