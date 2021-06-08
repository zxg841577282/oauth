package com.zxg.oauth_auth.util;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import com.zxg.oauth_auth.web.vo.WxBackVO;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.commons.lang.StringUtils;

import java.util.Objects;

/**
 * @Author: zhou_xg
 * @Date: 2021/6/8
 * @Purpose:
 */

public class MyWxUtil {
    final WxMaService wxService = getWxMaService();


    public WxBackVO login(Object code,String encryptedData,String iv) throws WxErrorException {
        // 获取微信用户session
        WxMaJscode2SessionResult session = wxService.getUserService().getSessionInfo(String.valueOf(code));
        if (null == session) {
            throw new RuntimeException("login handler error");
        }

        // 解密用户信息
        WxMaUserInfo wxUserInfo = wxService.getUserService().getUserInfo(session.getSessionKey(),
                encryptedData, iv);
        if (null == wxUserInfo) {
            throw new RuntimeException("wxUser not exist");
        }

        // 解密手机号码信息
        WxMaPhoneNumberInfo wxMaPhoneNumberInfo = wxService.getUserService().getPhoneNoInfo(session.getSessionKey(),
                encryptedData, iv);
        if (Objects.isNull(wxMaPhoneNumberInfo) || StringUtils.isBlank(wxMaPhoneNumberInfo.getPhoneNumber())) {
            // 解密手机号码信息错误
            throw new RuntimeException("获取微信手机号异常");
        }

        WxBackVO vo = new WxBackVO();
        vo.setResult(session);
        vo.setUserInfo(wxUserInfo);
        vo.setMobile(wxMaPhoneNumberInfo.getPhoneNumber());

        return vo;
    }

    public String getAccessToken() throws WxErrorException{
        return wxService.getAccessToken();
    }

    private WxMaService getWxMaService() {
        WxMaDefaultConfigImpl config = new WxMaDefaultConfigImpl();
        config.setAppid("appId");
        config.setSecret("appSecret");
        config.setMsgDataFormat("JSON");
        WxMaService wxMaService = new WxMaServiceImpl();
        wxMaService.setWxMaConfig(config);
        return wxMaService;
    }

}
