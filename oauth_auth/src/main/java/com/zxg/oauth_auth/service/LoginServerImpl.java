package com.zxg.oauth_auth.service;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.zxg.oauth_auth.web.dto.LoginDTO;
import com.zxg.oauth_auth.web.dto.WxLoginDTO;
import com.zxg.oauth_common.data.vo.LoginSuccessVO;
import com.zxg.oauth_common.data.vo.MyUserDetails;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author: zhou_xg
 * @Date: 2021/6/3
 * @Purpose:
 */

@Service
public class LoginServerImpl implements LoginServer {
    @Resource
    private RedisTokenStore redisTokenStore;

    @Override
    public LoginSuccessVO currencyLogin(LoginDTO dto) {
        Integer loginType = dto.getLoginType();

        LoginSuccessVO vo;

        //1账户+密码 2手机号+密码 3手机号+验证码
        switch (loginType){
            case 1:
                vo = usernamePasswordLogin(dto.getUsername(),dto.getPassword());
                break;
            case 2:
                vo = mobilePasswordLogin(dto.getMobile(),dto.getPassword());
                break;
            case 3:
                vo = mobileSmsCodeLogin(dto.getMobile(),dto.getSmsCode());
                break;
            default:
                throw new RuntimeException("不支持的登录类型");
        }

        if (vo.getSuccess()){
            OAuth2Authentication oAuth2Authentication = redisTokenStore.readAuthentication(vo.getAccess_token());

            MyUserDetails loginUserVO = (MyUserDetails) oAuth2Authentication.getUserAuthentication().getPrincipal();

            vo.setUserId(loginUserVO.getUserId());
        }

        return vo;
    }

    @Override
    public LoginSuccessVO wxClientLogin(WxLoginDTO wxLoginDTO) {

        LoginSuccessVO vo = jsCodeLogin(wxLoginDTO);

        if (vo.getSuccess()){
            OAuth2Authentication oAuth2Authentication = redisTokenStore.readAuthentication(vo.getAccess_token());

            MyUserDetails loginUserVO = (MyUserDetails) oAuth2Authentication.getUserAuthentication().getPrincipal();

            vo.setUserId(loginUserVO.getUserId());
        }

        return vo;
    }



    @Value("${oauth.get_token_url}")
    private String getTokenUrl;
    @Value("${oauth.scopo}")
    private String scopo;
    @Value("${oauth.clientId}")
    private String clientId;
    @Value("${oauth.clientSecret}")
    private String clientSecret;

    private LoginSuccessVO usernamePasswordLogin(String username, String password) {

        String url = getTokenUrl +
                "username=USER&" +
                "password=PWD&" +
                "grant_type=password&" +
                "scope=SCOPE&" +
                "client_id=CLIENTID&" +
                "client_secret=CLIENTSECRET"
                ;

        url = url.replace("USER",username)
                .replace("PWD",password)
                .replace("SCOPE",scopo)
                .replace("CLIENTID",clientId)
                .replace("CLIENTSECRET",clientSecret)
        ;

        return build(HttpUtil.get(url));
    }

    private LoginSuccessVO mobilePasswordLogin(String mobile, String password) {

        String url = getTokenUrl +
                "phone=PHONE&" +
                "password=PWD&" +
                "grant_type=phone&" +
                "scope=SCOPE&" +
                "client_id=CLIENTID&" +
                "client_secret=CLIENTSECRET"
                ;

        url = url.replace("PHONE",mobile)
                .replace("PWD",password)
                .replace("SCOPE",scopo)
                .replace("CLIENTID",clientId)
                .replace("CLIENTSECRET",clientSecret)
        ;

        return build(HttpUtil.get(url));
    }

    private LoginSuccessVO mobileSmsCodeLogin(String mobile, String smsCode) {
        String url = getTokenUrl +
                "phone=PHONE&" +
                "sms_code=SMSCODE&" +
                "grant_type=phone&" +
                "scope=SCOPE&" +
                "client_id=CLIENTID&" +
                "client_secret=CLIENTSECRET"
                ;

        url = url.replace("PHONE",mobile)
                .replace("SMSCODE",smsCode)
                .replace("SCOPE",scopo)
                .replace("CLIENTID",clientId)
                .replace("CLIENTSECRET",clientSecret)
        ;

        return build(HttpUtil.get(url));
    }

    private LoginSuccessVO jsCodeLogin(WxLoginDTO wxLoginDTO) {
        String url = getTokenUrl +
                "jscode=JSCODE&" +
                "encryptedData=ENCRYPTEDDATA&" +
                "iv=IV&" +
                "grant_type=wxmini&" +
                "scope=SCOPE&" +
                "client_id=CLIENTID&" +
                "client_secret=CLIENTSECRET"
                ;

        url = url.replace("JSCODE",wxLoginDTO.getCode())
                .replace("ENCRYPTEDDATA",wxLoginDTO.getEncryptedData())
                .replace("IV",wxLoginDTO.getIv())
                .replace("SCOPE",scopo)
                .replace("CLIENTID",clientId)
                .replace("CLIENTSECRET",clientSecret)
        ;

        return build(HttpUtil.get(url));
    }

    /**
     * 请求返回数据封装为LoginSuccessVO对象
     * @param backInfo
     * @return
     */
    private LoginSuccessVO build(String backInfo){
        LoginSuccessVO vo;

        JSONObject jsonObject = JSONObject.parseObject(backInfo);
        if (StringUtils.isNotBlank(jsonObject.getString("access_token"))){
            vo = new LoginSuccessVO();
            vo.setAccess_token(jsonObject.getString("access_token"));
            vo.setToken_type(jsonObject.getString("token_type"));
            vo.setRefresh_token(jsonObject.getString("refresh_token"));
            vo.setExpires_in(jsonObject.getInteger("expires_in"));
            vo.setScope(jsonObject.getString("scope"));
        }else {
            vo = JSONObject.parseObject(backInfo,LoginSuccessVO.class);
        }

        return vo;
    }

}
