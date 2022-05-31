package com.til.particle_system.util;

import com.til.particle_system.element.use_field.UseField;

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

        class NumberFinal implements IValueNumber {
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
                double d1 = max.as().doubleValue();
                double d2 = min.as().doubleValue();
                return random.nextDouble(Math.min(d1, d2), Math.max(d1, d2));
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
