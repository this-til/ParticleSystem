package com.til.particle_system.element.particle_life_time.speed;

import com.til.json_read_write.annotation.BaseClass;
import com.til.json_read_write.annotation.JsonField;
import com.til.json_read_write.annotation.SonClass;
import com.til.json_read_write.JsonTransform;
import com.til.math.ITime;
import com.til.math.IValue;
import com.til.particle_system.element.IElement;

/***
 * 生命周期速度继承
 */
@BaseClass(sonClass = LifeTimeSpeedLimitElement.class)
@SonClass()
public class LifeTimeSpeedExtendElement implements IElement.IParticleElement{

    /***
     * 于原速度的乘积
     */
    @JsonField
    public IValue.IValurV3 extend;

    @JsonField
    public ExtendType extendType;

    @BaseClass(sonClass = ExtendType.class)
    @SonClass(transform = JsonTransform.EnumCurrencyJsonTransform.class)
    public enum ExtendType{
        /***
         *
         */
        START,
        /***
         * 乘积
         */
        ALWAYS;
    }

}
