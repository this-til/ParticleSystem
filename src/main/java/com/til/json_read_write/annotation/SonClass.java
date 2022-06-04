package com.til.json_read_write.annotation;

import com.til.json_read_write.JsonTransform;
import com.til.util.UseString;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/***
 * @author til
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SonClass {
    String name() default UseString.DEFAULT;

    Class<?> transform() default JsonTransform.CurrencyJsonTransform.class;
}
