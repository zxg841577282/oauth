package com.zxg.oauth_auth.other;

import com.baomidou.mybatisplus.extension.exceptions.ApiException;
import com.zxg.oauth_common.other.R;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.oauth2.common.exceptions.*;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.stereotype.Component;

import javax.security.auth.message.AuthException;

/**
 * @Author: zhou_xg
 * @Date: 2021/5/8
 * @Purpose: 验证认证信息
 */
@Component
public class CustomWebResponseExceptionTranslator implements WebResponseExceptionTranslator<OAuth2Exception> {

    @Override
    public ResponseEntity translate(Exception e) {
        String message;
        if (e instanceof AuthException || e.getCause() instanceof AuthException) {
            message = e.getMessage();
        } else if (e instanceof InternalAuthenticationServiceException) {
            message = "身份验证失败";
        } else if (e instanceof InvalidGrantException) {
            message = e.getMessage();
        } else if (e instanceof InvalidTokenException) {
            message = "Token无效或过期";
        } else if (e instanceof UnsupportedGrantTypeException) {
            message = "不支持的授予类型";
        } else if (e instanceof InvalidRequestException) {
            message = "缺少关键信息:" + e.getMessage();
        } else if (e instanceof ApiException){
            message = e.getMessage();
        } else if (e instanceof InvalidClientException && e.getMessage().contains("Unauthorized grant type")){
            message = "不支持的授权类型";
        } else {
            message = "登录失败";
        }
        return ResponseEntity.ok(R.error(message));
    }
}