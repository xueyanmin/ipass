package com.xue.ipass.annotation;

import java.lang.annotation.*;


@Target({ElementType.METHOD})  //在方法上使用
@Retention(RetentionPolicy.RUNTIME) //运行时生效

/**自定义注解--java元注解*/
public @interface AddLog {

    String value();

    String name() default "";
}
