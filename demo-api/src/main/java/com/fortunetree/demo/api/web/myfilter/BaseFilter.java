package com.fortunetree.demo.api.web.myfilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fortunetree.basic.support.commons.business.logger.LogUtil;
import com.fortunetree.basic.support.commons.business.result.ResponseCode;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

/**
 * @author ZhouJumbo
 * @see
 */
@Component
public class BaseFilter implements Filter {

    private static ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public void destroy() {
        LogUtil.debug("BaseFilter destroy.");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        LogUtil.debug("BaseFilter doFilter.");

        LogUtil.debug("设置跨域请求");
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        //此处ip地址为需要访问服务器的ip及端口号
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Credentials", "true");
        resp.setHeader("Access-Control-Allow-Methods", "GET, HEAD, POST, PUT, DELETE, TRACE, OPTIONS, PATCH");
        resp.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type,Token,Accept, Connection, User-Agent, Cookie");
        // 指定本次预检请求的有效期，单位为秒
        resp.setHeader("Access-Control-Max-Age", "7200");
        // xss防护 1; mode=block：如果检测到恶意代码，在不渲染恶意代码   0： 表示关闭浏览器的XSS防护机制
        resp.setHeader("X-xss-protection","1;mode=block" );

        //获得所有请求的参数名
        Enumeration params = req.getParameterNames();
        String sql = "";
        while (params.hasMoreElements()) {
            //得到参数名 
            String param = params.nextElement().toString();
            //得到参数对应值
            String[] value = req.getParameterValues(param);
            for(int i = 0;i < value.length;i++){
                sql = sql + value[i];
            }
        }
        // sql防注入检测
        if(sqlValidate(sql)){
            //有异常参数
            resp.setStatus(ResponseCode.PARAM_INJECTION_RISK.value());
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("防注入提示："+objectMapper.writeValueAsString(ResponseCode.PARAM_INJECTION_RISK
                    .reasonPhrase()));
        }else{
            chain.doFilter(req, resp);
        }
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
        LogUtil.debug("BaseFilter init.");
    }

    /**
     * 防sql注入攻击
     * @param str
     * @return
     */
    protected static boolean sqlValidate(String str){
        //统一转为小写
        str = str.toLowerCase();
        //过滤掉的sql关键字，可以手动添加
        String badStr = "'|and|exec|execute|insert|select|delete|update|count|drop|*|%|chr|mid|master|truncate|" +  
                "char|declare|sitename|net user|xp_cmdshell|;|or|+|like'|create table|" +
                "table|from|grant|use|group_concat|column_name|" +  
                "information_schema.columns|table_schema|union|where|count|--";
        String[] badStrs = badStr.split("\\|");  
        for (int i = 0; i < badStrs.length; i++) {  
            if (str.indexOf(badStrs[i]) >= 0) {  
                return true;  
            }  
        }  
        return false;  
    }

}
