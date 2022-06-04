package com.til.particle_system.element.particle_life_time.colour;

import com.til.json_read_write.annotation.BaseClass;
import com.til.json_read_write.annotation.JsonField;
import com.til.json_read_write.annotation.SonClass;
import com.til.math.ITime;
import com.til.math.V2;
import com.til.particle_system.element.IElement;
import com.til.particle_system.element.particle_life_time.size.LifeTimeSizeElement;

/***
 * 速度颜色
 * @author til
 */
@BaseClass(sonClass = LifeTimeSizeElement.class)
@SonClass
public class LifeTimeSpeedColourElement implements IElement.IParticleElement {

    /***
     * 眼色变换
     * -根据速度范围计算time
     * -粒子颜色*该颜色
     */
    @JsonField
    public ITime.ITimeColour colour;

    /***
     * 速度范围
     * -根据移动速度的模计算time
     */
    @JsonField
    public V2 speedRange;
}
