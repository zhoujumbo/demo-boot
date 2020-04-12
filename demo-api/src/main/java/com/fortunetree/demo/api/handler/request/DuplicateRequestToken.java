package com.fortunetree.demo.api.handler.request;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DuplicateRequestToken {
    /**一次请求完成之前防止重复提交*/
    public static final int REQUEST=1;
    /**一次会话中防止重复提交*/
    public static final int SESSION=2;

    /**保存重复提交标记 默认为需要保存*/
    boolean save() default true;

    /**防止重复提交类型，默认：一次请求完成之前防止重复提交*/
    int type() default REQUEST;
}
