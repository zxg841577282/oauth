package com.zxg.oauth_common.other.token;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.Collection;

/**
 * @Author: zhou_xg
 * @Date: 2021/6/8
 * @Purpose:
 */

public class WxMiniAuthenticationToken extends AbstractAuthenticationToken implements Serializable {
    private static final long serialVersionUID= 11L;
    private final Object principal;
    private String encryptedData;
    private String iv;

    /**
     * 构建一个没有鉴权的 AccountLoginToken,手机号,密码，验证码
     */
    public WxMiniAuthenticationToken(Object principal, String encryptedData, String iv) {
        super(null);
        this.principal = principal;
        this.encryptedData=encryptedData;
        this.iv = iv;
        setAuthenticated(false);
    }
    /**
     * 构建拥有鉴权的 AccountLoginToken
     */
    public WxMiniAuthenticationToken(Object principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        super.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    public String getIv() {
        return this.iv;
    }

    public String getEncryptedData() {
        return this.encryptedData;
    }
}
