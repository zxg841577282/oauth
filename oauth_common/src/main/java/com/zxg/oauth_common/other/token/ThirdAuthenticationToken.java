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

public class ThirdAuthenticationToken extends AbstractAuthenticationToken implements Serializable {
    private static final long serialVersionUID= 12L;
    //手机号
    private final Object principal;
    //token
    private Object credentials;
    //类型
    private String types;


    public ThirdAuthenticationToken(Object principal, Object credentials, String types) {
        super(null);
        this.principal = principal;
        this.credentials=credentials;
        this.types=types;
        setAuthenticated(false);
    }
    /**
     * 构建拥有鉴权的 AccountLoginToken
     */
    public ThirdAuthenticationToken(Object principal, Collection<? extends GrantedAuthority> authorities) {
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

    public String getTypes() {
        return this.types;
    }
}
