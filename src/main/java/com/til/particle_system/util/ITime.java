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

    interface ITimeVec3 extends ITime<Vec3> {

        class DefaultTimeVec3 implements ITimeVec3 {
            @UseField
            public ITimeNumber x;
            @UseField
            public ITimeNumber y;
            @UseField
            public ITimeNumber z;

            @Override
            public Vec3 as(double time) {
                return new Vec3(x.as(time).doubleValue(), y.as(time).doubleValue(), z.as(time).doubleValue());
            }

        }

        class NoSeparationTimeVec3 implements ITimeVec3 {
            @UseField
            public ITimeNumber value;

            @Override
            public Vec3 as(double time) {
                return new Vec3(value.as(time).doubleValue(), value.as(time).doubleValue(), value.as(time).doubleValue());
            }

        }
    }

    interface ITimeVec2 extends ITime<Vec2> {
        class DefaultTimeVec2 implements ITimeVec2 {
            @UseField
            public ITimeNumber x;
            @UseField
            public ITimeNumber y;

            @Override
            public Vec2 as(double time) {
                return new Vec2(x.as(time).floatValue(), y.as(time).floatValue());
            }
        }

        class NoSeparationTimeVec2 implements ITimeVec2 {

            @UseField
            public ITimeNumber value;

            @Override
            public Vec2 as(double time) {
                return new Vec2(value.as(time).floatValue(), value.as(time).floatValue());
            }
        }
    }

    interface ITimeColour extends ITime<Color> {

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
            public Color as(double time) {
                return new Color(r.as(time).intValue(), g.as(time).intValue(), b.as(time).intValue(), a.as(time).intValue());
            }

        }

        class NoSeparationTimeColour implements ITimeColour {
            @UseField
            public ITimeNumber value;

            @Override
            public Color as(double time) {
                return new Color(value.as(time).intValue(), value.as(time).intValue(), value.as(time).intValue(), value.as(time).intValue());
            }

        }


    }

}
