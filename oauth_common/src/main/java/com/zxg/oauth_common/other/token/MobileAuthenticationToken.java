package com.zxg.oauth_common.other.token;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.Collection;

/**
 * @Author: zhou_xg
 * @Date: 2021/5/14
 * @Purpose: 自定义手机号生成token类
 */

public class MobileAuthenticationToken extends AbstractAuthenticationToken implements Serializable {
    private static final long serialVersionUID= 11L;
    //手机号
    private final Object principal;
    //密码
    private Object credentials;
    //短信验证码
    private String smsCode;

    /**
     * 构建一个没有鉴权的 AccountLoginToken,手机号,密码，验证码
     */
    public MobileAuthenticationToken(Object principal, Object credentials, String smsCode) {
        super(null);
        this.principal = principal;
        this.credentials=credentials;
        this.smsCode = smsCode;
        setAuthenticated(false);
    }
    /**
     * 构建拥有鉴权的 AccountLoginToken
     */
    public MobileAuthenticationToken(Object principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        super.setAuthenticated(true);
    }
    @Override
    public Object getCredentials() {
        return this.credentials;
    }
    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    public String getSmsCode() {
        return this.smsCode;
    }
}
