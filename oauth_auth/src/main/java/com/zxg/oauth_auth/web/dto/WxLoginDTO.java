package com.zxg.oauth_auth.web.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author: zhou_xg
 * @Date: 2021/6/8
 * @Purpose:
 */

@Getter
@Setter
public class WxLoginDTO {

    //用户登录凭证
    String code;

    //加密用户数据
    String encryptedData;

    //加密算法的初始向量
    String iv;
}
