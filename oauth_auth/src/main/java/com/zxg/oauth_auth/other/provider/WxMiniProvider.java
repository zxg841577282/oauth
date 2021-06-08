package com.zxg.oauth_auth.other.provider;

import cn.hutool.core.util.ObjectUtil;
import com.zxg.oauth_auth.service.UserService;
import com.zxg.oauth_auth.util.MyWxUtil;
import com.zxg.oauth_auth.web.vo.WxBackVO;
import com.zxg.oauth_common.data.vo.MyUserDetails;
import com.zxg.oauth_common.other.token.WxMiniAuthenticationToken;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

/**
 * @Author: zhou_xg
 * @Date: 2021/6/8
 * @Purpose: 微信小程序登录验证逻辑处理
 */
@Component
public class WxMiniProvider implements AuthenticationProvider {

    @Autowired
    private UserService userService;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        WxMiniAuthenticationToken token = (WxMiniAuthenticationToken) authentication;

        WxBackVO login = null;
        //调用微信api获取openId
        try {
            login = new MyWxUtil().login(token.getPrincipal(),token.getEncryptedData(),token.getIv());
        } catch (WxErrorException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }

        if (ObjectUtil.isNotEmpty(login)){

            MyUserDetails userDetails = userService.loadUserByWxMiniBack(login);

            //校验成功，返回一个授权过的Token
            return new WxMiniAuthenticationToken(userDetails, userDetails.getAuthorities());
        }

        throw new RuntimeException("微信小程序登录失败");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return WxMiniAuthenticationToken.class.isAssignableFrom(authentication);
    }

}
