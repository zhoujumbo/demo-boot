package com.fortunetree.demo.api.handler.request;


import org.apache.tomcat.util.net.openssl.ciphers.Authentication;

import javax.servlet.http.HttpServletRequest;

public interface AuthenticateManager {

    /**
     * 初始化
     */
    void initialize(HttpServletRequest request, String username);

    void initialize(HttpServletRequest request, Authentication authentication);

    /**
     * 添加第三方访问令牌（如：微信）
     *
     * @param token
     * @param expiresIn
     * @return
     */
    void add(String token, int expiresIn);

    /**
     * 添加第三方访问令牌（如：微信）
     *
     * @param token
     * @param expiresIn
     * @param refreshToken
     * @return
     */
    void add(String token, String refreshToken, int expiresIn);

    /**
     * 清空访问令牌
     *
     * @param deviceId
     */
    void clear(String deviceId);

    /**
     * 访问令牌是否过期
     *
     * @return
     */
    boolean expired();

    /**
     * 存储受管理的所有Token
     */
    void store();

    /**
     * 获取请求头部的visitorId
     *
     * @return
     * 		访客id
     */
    String getVisitorId();
}
