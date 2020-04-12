package com.fortunetree.demo.core.common.properties;

import com.fortunetree.demo.core.common.properties.config.CoreProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName CorePropertiesConfig
 * @Description TODO
 * @Author jb.zhou
 * @Date 2019/7/16
 * @Version 1.0
 */
@Configuration
@EnableConfigurationProperties(CoreProperties.class)
public class CorePropertiesConfig {
}
