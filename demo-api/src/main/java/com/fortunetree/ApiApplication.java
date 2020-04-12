package com.fortunetree;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.WebApplicationInitializer;


/**
 * @ClassName BackApplication
 * @Description TODO
 * @Author jb.zhou
 * @Date 2019/6/12
 * @Version 1.0
 */
@MapperScan({"com.**.core.**.dao","com.**.core.mapper"})
@SpringBootApplication
@EnableAsync // 开启异步
public class ApiApplication extends SpringBootServletInitializer implements WebApplicationInitializer {

    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }
}
