package com.fortunetree.demo.api.suport.properties;

import com.fortunetree.demo.api.suport.properties.prop.ApiProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(ApiProperties.class)
public class PropertiesConfig {
}
