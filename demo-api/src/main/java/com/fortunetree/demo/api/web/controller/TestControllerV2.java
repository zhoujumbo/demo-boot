package com.fortunetree.demo.api.web.controller;

import com.fortunetree.demo.api.handler.version.ApiVersion;
import com.fortunetree.demo.api.suport.properties.prop.ApiProperties;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName UrlMapsController
 * @Description TODO
 * @Author jb.zhou
 * @Date 2019/6/27
 * @Version 1.0
 */
@Api(tags = "Test接口")
@ApiVersion(1)
@RestController
@RequestMapping("/{version}/test")
public class TestControllerV2 {

    @Autowired
    private ApiProperties backProperties;

    @ApiOperation(value = "URLMap", notes = "获取url信息", httpMethod = "GET", response = JSONObject.class)
    @ApiResponses({@ApiResponse(code=200, message="OK")})
    @GetMapping
    public String test01(){
        return "test";
    }

}
