package com.fortunetree.demo.api.suport.properties.prop;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author zhoujumbo
 *
 */
@ConfigurationProperties(prefix = "api.conf")
@Data
public class ApiProperties {

}

