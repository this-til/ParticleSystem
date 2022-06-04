package com.til.particle_system.element.particle_life_time.colour;

import com.til.json_read_write.annotation.BaseClass;
import com.til.json_read_write.annotation.JsonField;
import com.til.json_read_write.annotation.SonClass;
import com.til.math.ITime;
import com.til.particle_system.element.IElement;

/***
 * 生命周期颜色
 * @author til
 */
@BaseClass(sonClass = LifeTimeColourElement.class)
@SonClass()
public class LifeTimeColourElement implements IElement.IParticleElement {

    /***
     * 颜色变换
     * -粒子颜色*该颜色
     */
    @JsonField
    public ITime.ITimeColour colour;
}
