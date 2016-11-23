package com.dynamic;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
@net.sf.oval.configuration.annotation.Constraint(checkWith = BigDecimalConstraint.class)
public @interface BigDecimalCheck {
    String message() default "max scale is {scale}. Max precision is {precision}";
    int scale() default 2147483647;

    int precision() default 0;
}
