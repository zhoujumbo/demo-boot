package com.fortunetree.demo.api.configuration;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.wall.WallConfig;
import com.alibaba.druid.wall.WallFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class DruidDBConfig {
    private Logger logger = LoggerFactory.getLogger(DruidDBConfig.class);
    @Bean
    @Primary  //在同样的DataSource中，首先使用被标注的DataSource
    @ConfigurationProperties(prefix = "spring.datasource.druid")
    public DataSource dataSource(){
        DruidDataSource datasource = new DruidDataSource();

        List<Filter> filters = new ArrayList<>();
        filters.add(wallFilter);
        datasource.setProxyFilters(filters);

        return datasource;
    }

    @Autowired
    WallFilter wallFilter;


    @Bean(name = "wallConfig")
    WallConfig wallFilterConfig(){
        WallConfig wc = new WallConfig ();
        wc.setMultiStatementAllow(true);
        wc.setCommentAllow(true);
        return wc;
    }

    @Bean(name = "wallFilter")
    @DependsOn("wallConfig")
    WallFilter wallFilter(WallConfig wallConfig){
        WallFilter wfilter = new WallFilter ();
        wfilter.setConfig(wallConfig);
        return wfilter;
    }
}