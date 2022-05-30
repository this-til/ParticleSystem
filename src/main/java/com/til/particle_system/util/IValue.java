package com.til.particle_system.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.til.particle_system.element.use_field.UseField;
import com.til.particle_system.event.EventMod;
import com.til.particle_system.register.IFromJsonElement;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

/**
 * @author til
 */
public interface IValue<E> {

    /***
     * 生成
     * @return 随机生成的值
     */
    E as();

    @Deprecated
    interface IFinalNumber extends IValue<Number> {

        @Deprecated
        class NumberFinal implements IValueNumber, IFinalNumber {
            @UseField
            public Number value;

            @Override
            public Number as() {
                return value;
            }

        }

    }

    interface IValueNumber extends IValue<Number> {

        class NumberFinal implements IValueNumber, IFinalNumber {
            @UseField
            public Number value;

            @Override
            public Number as() {
                return value;
            }
        }

        class NumberRandom implements IValueNumber {

            @UseField
            public IFinalNumber max;
            @UseField
            public IFinalNumber min;
            public Random random = new Random();

            @Override
            public Number as() {
                return random.nextDouble(min.as().doubleValue(), max.as().doubleValue());
            }
        }


    }

    @Deprecated
    interface IValueBoolean extends IValue<Boolean> {

        @Deprecated
        class BoolFinal implements IValueBoolean {
            @UseField
            public boolean value;

            @Override
            public Boolean as() {
                return value;
            }
        }

    }

}
