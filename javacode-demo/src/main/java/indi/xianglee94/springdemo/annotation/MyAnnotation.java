package indi.xianglee94.javacode.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
public @interface MyAnnotation {
    String value() default "defaultString";
}
