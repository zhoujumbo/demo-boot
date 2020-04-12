package com.fortunetree.demo.api.handler.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fortunetree.basic.support.commons.business.logger.LogUtil;
import com.fortunetree.basic.support.commons.business.result.ResultMessage;
import com.google.common.base.Charsets;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.accept.HeaderContentNegotiationStrategy;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName MyRestExceptionHandler
 * @Description TODO
 * @Author jb.zhou
 * @Date 2019/7/18
 * @Version 1.0
 */
@ControllerAdvice
public class RestApiExceptionHandler {

    @Autowired
    private ObjectMapper objectMapper;

    private HeaderContentNegotiationStrategy headerContentNegotiationStrategy = new HeaderContentNegotiationStrategy();
    private Gson gson = new Gson();


    @ExceptionHandler({InterfaceException.class})
    public void interfaceException(InterfaceException e, HttpServletRequest request, HttpServletResponse response) {
        LogUtil.error("ServiceException Handler", e);
        response.setCharacterEncoding(Charsets.UTF_8.name());
        response.setStatus(HttpStatus.OK.value());
        boolean isTextHtml = this.isTextHtml(request);
        if (isTextHtml) {
            int code = e.getCode();
            request.setAttribute("code", code);
            request.setAttribute("message", e.getMessage());
            response.setContentType("text/html;charset=utf-8");
            try {
//                String redirectUri = "error";
//                if (InterfaceError.UNAUTHENTICATED.code() == code) {
//                    redirectUri = SSOContext.getAuthorizeUrl();
//                } else if (Strings.isNullOrEmpty(interfaceResult.getMessage())) {
//                    response.sendError(code);
//                } else {
//                    response.sendError(code, interfaceResult.getMessage());
//                }
//                response.sendRedirect(redirectUri);
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("code", code);
                map.put("message", e.getMessage());
                response.getWriter().print(gson.toJson(map));
            } catch (IOException var1) {
                LogUtil.error("重定向异常页面时发生异常", var1);
            }
        } else {
                response.setContentType("application/json;charset=UTF-8");
            try {
                response.getWriter().write(objectMapper.writeValueAsString(ResultMessage.error(e.getCode(), e.getMessage())));
            } catch (Exception var2) {
                LogUtil.error("转换异常对象时发生异常 - {}", var2.getMessage());
            }
        }
    }


    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
        Map<String, Object> map = new HashMap<>();
        map.put("code",500);
        map.put("msg","文件大小超出限制, 请压缩或降低文件质量! ");
        return map;
    }


    private boolean isTextHtml(HttpServletRequest request) {
        List mediaTypes = null;
        try {
            mediaTypes = this.headerContentNegotiationStrategy.resolveMediaTypes(new ServletWebRequest(request));
        } catch (HttpMediaTypeNotAcceptableException var3) {
            return false;
        }

        return mediaTypes.contains(MediaType.TEXT_HTML);
    }


}
