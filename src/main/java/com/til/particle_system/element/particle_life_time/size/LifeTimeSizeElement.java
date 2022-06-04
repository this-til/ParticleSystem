package com.til.particle_system.element.particle_life_time.size;

import com.til.json_read_write.annotation.BaseClass;
import com.til.json_read_write.annotation.JsonField;
import com.til.json_read_write.annotation.SonClass;
import com.til.math.ITime;
import com.til.particle_system.element.IElement;

/***
 * 生命周期大小
 * @author til
 */
@BaseClass(sonClass = LifeTimeSizeElement.class)
@SonClass()
public class LifeTimeSizeElement implements IElement.IParticleElement {

    /***
     * 大小
     * -起始大小*该值
     */
    @JsonField
    public ITime.ITimeV3 size;
}
