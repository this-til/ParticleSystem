package com.til.particle_system.util;


import com.til.particle_system.element.use_field.UseField;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

import java.awt.*;
import java.util.Random;

/**
 * @author til
 */
public interface ITime<E> {

    /***
     * 生成
     * @param time 0~1之间一般表示粒子的生命周期或者系统的生命周期
     * @return 随机生成的值
     */
    E as(double time);

    interface ITimeNumber extends ITime<Number> {
        class NumberFinal implements ITimeNumber {

            public Number number;

            @Override
            public Number as(double time) {
                return number;
            }
        }

        class RandomNumber implements ITimeNumber {
            @UseField
            public ITimeNumber max;
            @UseField
            public ITimeNumber min;
            public Random random = new Random();

            @Override
            public Number as(double time) {
                return random.nextDouble(min.as(time).doubleValue(), max.as(time).doubleValue());
            }
        }

        class GradientNumber implements ITimeNumber {

            @Override
            public Number as(double time) {
                return 0;
            }
        }
    }

    interface ITimeV3 extends ITime<V3> {

        class DefaultTimeV3 implements ITimeV3 {
            @UseField
            public ITimeNumber x;
            @UseField
            public ITimeNumber y;
            @UseField
            public ITimeNumber z;

            @Override
            public V3 as(double time) {
                return new V3(x.as(time).doubleValue(), y.as(time).doubleValue(), z.as(time).doubleValue());
            }

        }

        class NoSeparationTimeV3 implements ITimeV3 {
            @UseField
            public ITimeNumber value;

            @Override
            public V3 as(double time) {
                return new V3(value.as(time).doubleValue(), value.as(time).doubleValue(), value.as(time).doubleValue());
            }

        }
    }

    interface ITimeV2 extends ITime<V2> {
        class DefaultTimeV2 implements ITimeV2 {
            @UseField
            public ITimeNumber x;
            @UseField
            public ITimeNumber y;

            @Override
            public V2 as(double time) {
                return new V2(x.as(time).doubleValue(), y.as(time).doubleValue());
            }
        }

        class NoSeparationTimeV2 implements ITimeV2 {

            @UseField
            public ITimeNumber value;

            @Override
            public V2 as(double time) {
                return new V2(value.as(time).doubleValue(), value.as(time).doubleValue());
            }
        }
    }

    interface ITimeColour extends ITime<Colour> {

        class DefaultTimeColour implements ITimeColour {

            @UseField
            public ITimeNumber r;
            @UseField
            public ITimeNumber g;
            @UseField
            public ITimeNumber b;
            @UseField
            public ITimeNumber a;

            @Override
            public Colour as(double time) {
                return new Colour(r.as(time).doubleValue(), g.as(time).doubleValue(), b.as(time).doubleValue(), a.as(time).doubleValue());
            }

        }

        class NoSeparationTimeColour implements ITimeColour {
            @UseField
            public ITimeNumber value;

            @Override
            public Colour as(double time) {
                return new Colour(value.as(time).doubleValue(), value.as(time).doubleValue(), value.as(time).doubleValue(), value.as(time).doubleValue());
            }

        }


    }

}
