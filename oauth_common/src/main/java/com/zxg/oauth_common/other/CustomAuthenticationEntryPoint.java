package com.zxg.oauth_common.other;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.exceptions.*;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 无效token 异常重写
 * @author yjw
 * @date 2020/09/01
 **/
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Value("${noLogin.loginUrl}")
    private String serverUrl;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authenticationException) throws IOException, ServletException {
        response.setHeader("Content-Type", "application/json;charset=UTF-8");
        response.setStatus(HttpStatus.OK.value());

        Throwable throwable = authenticationException.fillInStackTrace();

        if (throwable instanceof BadCredentialsException){
            response.getWriter().write(JSON.toJSONString(R.error(401,"错误的客户端信息")));
        }else {
            Throwable cause = authenticationException.getCause();
            if (cause instanceof InvalidTokenException) {
                response.getWriter().write(JSON.toJSONString(R.error(401,"无效的token信息")));
            } else if (cause instanceof AccountExpiredException){
                response.getWriter().write(JSON.toJSONString(R.error("账户已过期")));
            } else if (cause instanceof LockedException){
                response.getWriter().write(JSON.toJSONString(R.error("账户已被锁定")));
            } else if (cause instanceof InvalidClientException || cause instanceof BadClientCredentialsException){
                response.getWriter().write(JSON.toJSONString(R.error(401,"无效的客户端")));
            } else if (cause instanceof InvalidGrantException || cause instanceof RedirectMismatchException){
                response.getWriter().write(JSON.toJSONString(R.error("无效的类型")));
            } else if (cause instanceof UnauthorizedClientException) {
                response.getWriter().write(JSON.toJSONString(R.error("未经授权的客户端")));
            }else {
                response.getWriter().write(JSON.toJSONString(R.error(401,"缺失信息")));
                response.setHeader("refresh","3;"+serverUrl);
            }
        }
    }
}
