package com.zxg.oauth_auth.service;


import com.zxg.oauth_auth.web.dto.LoginDTO;
import com.zxg.oauth_auth.web.dto.WxLoginDTO;
import com.zxg.oauth_common.data.vo.LoginSuccessVO;

public interface LoginServer {
    /**
     * 通用登录
     */
    LoginSuccessVO currencyLogin(LoginDTO dto);

    /**
     * 微信小程序登录
     */
    LoginSuccessVO wxClientLogin(WxLoginDTO wxLoginDTO);
}
