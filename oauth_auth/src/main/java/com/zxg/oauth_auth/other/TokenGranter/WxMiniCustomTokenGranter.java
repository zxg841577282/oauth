package com.zxg.oauth_auth.other.TokenGranter;

import com.zxg.oauth_common.data.constant.TokenConstant;
import com.zxg.oauth_common.other.token.WxMiniAuthenticationToken;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Author: zhou_xg
 * @Date: 2021/5/14
 * @Purpose: 自定义第三方模式
 */

public class WxMiniCustomTokenGranter extends AbstractTokenGranter {

    private final AuthenticationManager authenticationManager;

    public WxMiniCustomTokenGranter(AuthorizationServerTokenServices tokenServices, ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory, AuthenticationManager authenticationManager) {
        super(tokenServices, clientDetailsService, requestFactory, TokenConstant.THIRD_PWD);
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {
        Map<String, String> parameters = new LinkedHashMap<>(tokenRequest.getRequestParameters());
        //从参数中获取手机号
        String jscode = parameters.get("jscode");
        //参数中获取密码
        String encryptedData = parameters.get("encryptedData");
        //参数中获取密码
        String iv = parameters.get("iv");

        //暂时将三个全部传入
        Authentication userAuth = new WxMiniAuthenticationToken(jscode, encryptedData,iv);
        ((WxMiniAuthenticationToken) userAuth).setDetails(parameters);

        try {
            userAuth = authenticationManager.authenticate(userAuth);
        }catch (AccountStatusException | BadCredentialsException ase) {
            //covers expired, locked, disabled cases (mentioned in section 5.2, draft 31)
            throw new InvalidGrantException(ase.getMessage());
        }
        // If the username/password are wrong the spec says we should send 400/invalid grant
        if (userAuth == null || !userAuth.isAuthenticated()) {
            throw new InvalidGrantException("校验失败: " + jscode);
        }

        //验证通过，则返回一个Oauth2token。
        OAuth2Request storedOAuth2Request = getRequestFactory().createOAuth2Request(client, tokenRequest);
        return new OAuth2Authentication(storedOAuth2Request, userAuth);
    }

}
