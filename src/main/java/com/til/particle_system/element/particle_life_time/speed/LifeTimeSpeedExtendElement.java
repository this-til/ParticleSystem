package com.til.particle_system.element.particle_life_time.speed;

import com.til.json_read_write.annotation.BaseClass;
import com.til.json_read_write.annotation.JsonField;
import com.til.json_read_write.annotation.SonClass;
import com.til.json_read_write.api.JsonTransform;
import com.til.json_read_write.util.math.ITime;
import com.til.particle_system.element.IElement;

/***
 * 生命周期速度继承
 */
@BaseClass(sonClass = LifeTimeSpeedLimitElement.class)
@SonClass()
public class LifeTimeSpeedExtendElement implements IElement.IParticleElement{

    /***
     * 层级
     */
    @JsonField
    public ITime.ITimeNumber extend;

    @JsonField
    public ExtendType extendType;

    @BaseClass(sonClass = ExtendType.class)
    @SonClass(transform = JsonTransform.EnumCurrencyJsonTransform.class)
    public enum ExtendType{
        /***
         * 加成1
         */
        ADD,
        /***
         * 乘积
         */
        MULTIPLY
    }

}
