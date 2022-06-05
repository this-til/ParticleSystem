package com.til.math;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.til.json_read_write.annotation.*;
import com.til.json_read_write.JsonAnalysis;
import com.til.json_read_write.JsonTransform;
import com.til.util.UseString;

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

    @BaseClass(sonClass = {IValueNumber.FinalNumber.class, IValueNumber.RandomNumber.class})
    @DefaultNew(newExample = IValueNumber.FinalNumber.class)
    @UsePrefab(route = {UseString.PREFAB,UseString.VALUE ,"number"})
    interface IValueNumber extends IValue<Number> {

        @SonClass(transform = FinalNumber.NumberFinalTransform.class)
        class FinalNumber implements IValueNumber {
            @JsonField
            public Number value;

            public FinalNumber() {
                super();
                value = 0;
            }

            @Override
            public Number as() {
                return value;
            }

            public static class NumberFinalTransform extends JsonTransform<FinalNumber> {
                public NumberFinalTransform(Class<FinalNumber> type, SonClass sonClass, JsonAnalysis jsonAnalysis) throws Exception {
                    super(type, sonClass, jsonAnalysis);
                }

                @Override
                public JsonElement from(FinalNumber numberFinal) throws Exception {
                    return new JsonPrimitive(numberFinal.value);
                }

                @Override
                public FinalNumber as(JsonElement jsonElement) throws Exception {
                    FinalNumber numberFinal = new FinalNumber();
                    numberFinal.value = jsonElement.getAsNumber();
                    return numberFinal;
                }
            }

        }

        @SonClass(name = UseString.RANDOM)
        class RandomNumber implements IValueNumber {

            @JsonField
            public IValueNumber max;
            @JsonField
            public IValueNumber min;
            public Random random = new Random();

            @Override
            public Number as() {
                double d1 = max.as().doubleValue();
                double d2 = min.as().doubleValue();
                return random.nextDouble(Math.min(d1, d2), Math.max(d1, d2));
            }
        }
    }

    @BaseClass(sonClass = {IValurV2.ValurV2.class, IValurV2.NoSeparationValurV2.class})
    @DefaultNew(newExample = IValurV2.ValurV2.class)
    @UsePrefab(route = {UseString.PREFAB,UseString.VALUE ,"v2"})
    interface IValurV2 extends IValue<V2> {
        @SonClass()
        class ValurV2 implements IValurV2 {

            @JsonField
            public IValueNumber x;
            @JsonField
            public IValueNumber y;

            @Override
            public V2 as() {
                return new V2(x.as().doubleValue(), y.as().doubleValue());
            }
        }

        @SonClass(name = UseString.NO_SEPARATION)
        class NoSeparationValurV2 implements IValurV2 {
            @JsonField
            public IValueNumber value;

            @Override
            public V2 as() {
                return new V2(value.as().doubleValue(), value.as().doubleValue());
            }
        }

    }

    @BaseClass(sonClass = {IValurV3.ValurV3.class, IValurV3.NoSeparationValurV3.class})
    @DefaultNew(newExample = IValurV3.ValurV3.class)
    @UsePrefab(route = {UseString.PREFAB,UseString.VALUE ,"v3"})
    interface IValurV3 extends IValue<V3> {

        @SonClass()
        class ValurV3 implements IValurV3 {

            @JsonField
            public IValueNumber x;
            @JsonField
            public IValueNumber y;
            @JsonField
            public IValueNumber z;

            @Override
            public V3 as() {
                return new V3(x.as().doubleValue(), y.as().doubleValue(), z.as().doubleValue());
            }
        }

        @SonClass(name = UseString.NO_SEPARATION)
        class NoSeparationValurV3 implements IValurV3 {
            @JsonField
            public IValueNumber value;

            @Override
            public V3 as() {
                return new V3(value.as().doubleValue(), value.as().doubleValue(), value.as().doubleValue());
            }
        }

    }

    @BaseClass(sonClass = {IValurColour.ValurColour.class, IValurColour.NoSeparationValurColour.class})
    @DefaultNew(newExample = IValurColour.ValurColour.class)
    @UsePrefab(route = {UseString.PREFAB,UseString.VALUE ,"colour"})
    interface IValurColour extends IValue<Colour> {

        @SonClass()
        class ValurColour implements IValue<Colour> {

            @JsonField
            public IValueNumber r;
            @JsonField
            public IValueNumber g;
            @JsonField
            public IValueNumber b;
            @JsonField
            public IValueNumber a;

            @Override
            public Colour as() {
                return new Colour(r.as().doubleValue(), g.as().doubleValue(), b.as().doubleValue(), a.as().doubleValue());
            }
        }

        @SonClass(name = UseString.NO_SEPARATION)
        class NoSeparationValurColour implements IValue<Colour> {
            @JsonField
            public IValueNumber value;

            @Override
            public Colour as() {
                return new Colour(value.as().doubleValue(), value.as().doubleValue(), value.as().doubleValue(), value.as().doubleValue());
            }
        }
    }
}
