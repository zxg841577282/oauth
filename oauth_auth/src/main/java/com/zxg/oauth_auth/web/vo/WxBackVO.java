package com.zxg.oauth_auth.web.vo;

import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author: zhou_xg
 * @Date: 2021/6/8
 * @Purpose:
 */
@Getter
@Setter
public class WxBackVO {

    private WxMaJscode2SessionResult result;

    private WxMaUserInfo userInfo;

    private String mobile;

}
