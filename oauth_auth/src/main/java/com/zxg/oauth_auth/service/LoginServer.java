package com.zxg.oauth_auth.service;


import com.zxg.oauth_auth.web.dto.LoginDTO;
import com.zxg.oauth_common.data.vo.LoginSuccessVO;

public interface LoginServer {
    LoginSuccessVO currencyLogin(LoginDTO dto);
}
