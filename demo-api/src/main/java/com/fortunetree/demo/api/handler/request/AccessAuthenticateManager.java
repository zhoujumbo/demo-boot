package com.fortunetree.demo.api.handler.request;

import lombok.Getter;
import lombok.Setter;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpServletRequest;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class AccessAuthenticateManager  implements AuthenticateManager {

    private static final Logger logger = LoggerFactory.getLogger(AccessAuthenticateManager.class);



    @Setter
    @Getter
    private Authentication authentication;

    /**
     * 由本系统生成的用户唯一标识
     */
    @Setter
    @Getter
    private Integer userId;

    /**
     * 由本系统生成的用户认证
     */
    @Setter
    @Getter
    private String token;

    @Setter
    @Getter
    private String username;

    /**
     * 本次请求
     */
    @Setter
    @Getter
    private HttpServletRequest request;

    @Override
    public void initialize(HttpServletRequest request, String token) {
        setRequest(request);
        setToken(token);
    }

    @Override
    public void initialize(HttpServletRequest request, Authentication authentication) {
        setRequest(request);
        setAuthentication(authentication);
//        setUserId(cacheService.getUserIdByToken(token));
    }

    @Override
    public void add(String token, int expiresIn) {

    }

    @Override
    public void add(String token, String refreshToken, int expiresIn) {

    }

    @Override
    public void clear(String deviceId) {

    }

    @Override
    public boolean expired() {
        return getUserId() == null;
    }

    @Override
    public void store() {

    }

    @Override
    public String getVisitorId() {
        return null;
    }
}
