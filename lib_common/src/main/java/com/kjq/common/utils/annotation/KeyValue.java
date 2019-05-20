package com.kjq.common.utils.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface KeyValue {
    String majorKey();
    String assistantKey() default "";
    Class observableFieldType() default String.class;
    Class tClass() default Object.class;
    String methodName() default "";
}
