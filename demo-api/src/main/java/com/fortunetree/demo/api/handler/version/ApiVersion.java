package com.fortunetree.demo.api.handler.version;

import org.springframework.web.bind.annotation.Mapping;

import java.lang.annotation.*;

/**
 * @Description TODO
 * @Author jb.zhou
 * @Date
 * @Version 1.0
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Mapping
public @interface ApiVersion {
    /**
     * 标识版本号
     * @return
     */
    int value();
}
