package uk.dioxic.mgenerate.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.SOURCE)
public @interface PojoProperty {
    String value() default "";
    boolean required() default false;
    boolean subDoc() default false;
}
