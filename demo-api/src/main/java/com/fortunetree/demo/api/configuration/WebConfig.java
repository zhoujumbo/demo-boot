package com.fortunetree.demo.api.configuration;

import com.fortunetree.demo.api.handler.version.CustomRequestMappingHandlerMapping;
import com.fortunetree.demo.api.web.interceptor.MyInterceptor;
import com.fortunetree.demo.api.web.myfilter.BaseFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * 修改静态路径映射
 * 注意：因为启动类默认已经包含Configuration，所以不能用@Configuration，否则失效
 */
@Configuration
//@EnableWebMvc
public class WebConfig extends WebMvcConfigurationSupport {

    private static final String URL_PATTERNS = "/*";

    @Autowired
    private MyInterceptor myInterceptor;


    /**
     * 重新处理方法
     */
    @Override
    public RequestMappingHandlerMapping requestMappingHandlerMapping() {
        RequestMappingHandlerMapping handlerMapping = new CustomRequestMappingHandlerMapping();
        handlerMapping.setOrder(0);
        handlerMapping.setInterceptors(getInterceptors());
        return handlerMapping;
    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 将/static/**访问映射到classpath:/mystatic/
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/")
                .addResourceLocations("classpath:/resources/")
                .addResourceLocations("classpath:/public/")
                .addResourceLocations("classpath:/templates/");

        super.addResourceHandlers(registry);
    }



    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(myInterceptor);
    }
    /**
     * 注册 将自定义Filter加入到过滤器链
     * @see BaseFilter
     */
    @Bean
    public FilterRegistrationBean addBaseFilter() {

        FilterRegistrationBean registration = new FilterRegistrationBean();

        registration.setFilter(baseFilter());
        List<String> urls = new ArrayList<>();
        urls.add(URL_PATTERNS);
        registration.setUrlPatterns(urls);

        return registration;
    }

    @Bean
    public BaseFilter baseFilter(){
        return new BaseFilter();
    }
}
