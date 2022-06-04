package com.til.math;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.til.json_read_write.annotation.BaseClass;
import com.til.json_read_write.annotation.DefaultNew;
import com.til.json_read_write.annotation.JsonField;
import com.til.json_read_write.annotation.SonClass;
import com.til.json_read_write.JsonAnalysis;
import com.til.json_read_write.JsonTransform;

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

    @BaseClass(sonClass = {IValueNumber.NumberFinal.class, IValueNumber.NumberRandom.class})
    @DefaultNew(newExample = IValueNumber.NumberFinal.class)
    interface IValueNumber extends IValue<Number> {

        @SonClass(transform = NumberFinal.NumberFinalTransform.class)
        class NumberFinal implements IValueNumber {
            @JsonField
            public Number value;

            public NumberFinal() {
                super();
                value = 0;
            }

            @Override
            public Number as() {
                return value;
            }

            public static class NumberFinalTransform extends JsonTransform<NumberFinal> {
                public NumberFinalTransform(Class<NumberFinal> type, SonClass sonClass, JsonAnalysis jsonAnalysis) throws Exception {
                    super(type, sonClass, jsonAnalysis);
                }

                @Override
                public JsonElement from(NumberFinal numberFinal) throws Exception {
                    return new JsonPrimitive(numberFinal.value);
                }

                @Override
                public NumberFinal as(JsonElement jsonElement) throws Exception {
                    NumberFinal numberFinal = new NumberFinal();
                    numberFinal.value = jsonElement.getAsNumber();
                    return numberFinal;
                }
            }

        }

        @SonClass()
        class NumberRandom implements IValueNumber {

            @JsonField
            public double max;
            @JsonField
            public double min;
            public Random random = new Random();

            public NumberRandom() {
                super();
                max = 0;
                min = 0;
            }

            @Override
            public Number as() {
                double d1 = max;
                double d2 = min;
                return random.nextDouble(Math.min(d1, d2), Math.max(d1, d2));
            }
        }
    }
}
