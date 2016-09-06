package com.kk.wechat.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 表示model中，对应的微信支付接口中的字段，以及是否为必填项
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface ApiRequestField {

    String value() default "";

    boolean required() default true;
}
