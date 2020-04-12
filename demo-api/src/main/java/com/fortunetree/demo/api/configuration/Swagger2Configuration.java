package com.fortunetree.demo.api.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2
public class Swagger2Configuration {

    @Value("${swagger2.doc.host}")
    private String host;
    @Value("${swagger2.doc.basePackage}")
    private String basePackage;
    @Value("${swagger2.doc.title}")
    private String title;
    @Value("${swagger2.doc.description}")
    private String description;
    @Value("${swagger2.doc.version}")
    private String version;
    @Value("${swagger2.doc.licenseUrl}")
    private String licenseUrl;


    @Bean
    public Docket docket(ApiInfo apiInfo) {

        List<ResponseMessage> responseMessageList = new ArrayList<>();
        responseMessageList.add(new ResponseMessageBuilder().code(200).message("OK").responseModel(new ModelRef("ServiceException")).build());
        responseMessageList.add(new ResponseMessageBuilder().code(201).message("请求无效").responseModel(new ModelRef("ServiceException")).build());
        responseMessageList.add(new ResponseMessageBuilder().code(401).message("请求需要认证").responseModel(new ModelRef("ServiceException")).build());
        responseMessageList.add(new ResponseMessageBuilder().code(403).message("请求无权限").responseModel(new ModelRef("ServiceException")).build());
        responseMessageList.add(new ResponseMessageBuilder().code(404).message("请求资源不存在").responseModel(new ModelRef("ServiceException")).build());
        responseMessageList.add(new ResponseMessageBuilder().code(500).message("服务异常").responseModel(new ModelRef("ServiceException")).build());




        // @formatter:off
        return new Docket(DocumentationType.SWAGGER_2)
                .host(host)
                .apiInfo(apiInfo)
//                .globalResponseMessage(RequestMethod.GET, responseMessageList)
//                .globalResponseMessage(RequestMethod.POST, responseMessageList)
//                .globalResponseMessage(RequestMethod.PUT, responseMessageList)
//                .globalResponseMessage(RequestMethod.DELETE, responseMessageList)
                .select()
                .paths(PathSelectors.any())
                .apis(RequestHandlerSelectors.basePackage(basePackage))

                .build();
        // @formatter:on
    }

    @Bean
    public ApiInfo apiInfo() {
        // @formatter:off
        return new ApiInfoBuilder()
                .title(title)
                .description(description)
                .version(version)
                .contact(new Contact("Mr.zhou","", "shenqi9507@163.com"))
//                    .termsOfServiceUrl("http://local.chinajoy.net")
                .license("LICENSE")
                .licenseUrl(licenseUrl)
                .build();
        // @formatter:on
    }



}
