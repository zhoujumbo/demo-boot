package com.fortunetree.demo.api.handler.aspect;

import com.fortunetree.basic.support.commons.business.result.ResponseCode;
import com.fortunetree.demo.api.handler.exception.InterfaceException;
import com.fortunetree.demo.api.handler.request.AccessAuthenticateManager;
import com.fortunetree.demo.api.handler.request.AuthenticationIgnore;
import com.fortunetree.demo.api.handler.request.DuplicateRequestToken;
import com.fortunetree.demo.api.suport.constants.TokenConstants;
import com.fortunetree.demo.core.common.exception.ServiceException;
import com.fortunetree.demo.core.common.utils.RedisKeyUtil;
import com.google.common.base.Joiner;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

/**
 * 添加操作人、操作时间信息
 * @author ZhouJumbo
 */
@Aspect
@Component
public class BusinessRequestAspect {
    private static final Logger logger = LoggerFactory.getLogger(BusinessRequestAspect.class);

    @Value("${duplicate.timeout}")
    private long duplicateTmeout;

    private Joiner joinerParam = Joiner.on("&").useForNull("");

    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    AccessAuthenticateManager accessAuthenticateManager;

    @Pointcut("execution(* com.fortunetree.demo.api.web.controller.*.*(..))")
    private void allRequest(){}

    @Around("allRequest()")
    public Object doAround(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();

        // 校验用户权限
        validateAccessToken(signature);
        // 校验重复请求
        validateDuplicateRequestToken(signature);

        return point.proceed();
    }

    private void validateDuplicateRequestToken(MethodSignature signature) throws InterfaceException {
        if (signature.getMethod().getAnnotation(DuplicateRequestToken.class) != null) {

            String username = accessAuthenticateManager.getAuthentication().name();
            HttpServletRequest request = accessAuthenticateManager.getRequest();
            String uri = request.getRequestURI();
            String key = RedisKeyUtil.getKey(TokenConstants.BACK_REQUESR_DUPLICATE_TOKEN, username, uri);
            if(null != stringRedisTemplate.opsForValue().get(key)){
                logger.info("[validateDuplicateRequestToken fail, key={}]", key);
                throw new ServiceException(ResponseCode.DUPLICATE_REQUEST);
            }
            stringRedisTemplate.opsForValue().set(key, key, duplicateTmeout, TimeUnit.SECONDS);
            logger.info("[validateDuplicateRequestToken success, key={}]", key);
        }
    }


    private void validateAccessToken(MethodSignature signature) throws InterfaceException{
        if (signature.getMethod().getAnnotation(AuthenticationIgnore.class) != null) {
//            RequestAttributes ra = RequestContextHolder.getRequestAttributes();
//            ServletRequestAttributes sra = (ServletRequestAttributes) ra;
//            HttpServletRequest request = sra.getRequest();
//
//            Assert.notNull(authentication, ()->{
//                throw new ServiceException(ResponseCode.UNAUTHORIZED);
//            });
//            accessAuthenticateManager.initialize(request, "");
//            if(StringUtils.isBlank(accessAuthenticateManager.getAuthentication().getName())){
//                throw new ServiceException(ResponseCode.UNAUTHORIZED);
//            }
        }
    }



}
