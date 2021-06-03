package com.zxg.oauth_auth.controller;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.zxg.oauth_auth.service.OauthClientDetailsService;
import com.zxg.oauth_auth.service.UserThirdPartyService;
import com.zxg.oauth_common.data.entity.OauthClientDetails;
import com.zxg.oauth_common.data.entity.UserThirdParty;
import com.zxg.oauth_common.other.R;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthToken;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.*;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

/**
 * @Author: zhou_xg
 * @Date: 2021/6/2
 * @Purpose: 第三方登录
 */

@RestController
@RequestMapping("/third")
public class ThirdController {
    @Autowired
    private OauthClientDetailsService oauthClientDetailsService;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private UserThirdPartyService userThirdPartyService;

    /**
     * 获取授权链接并跳转到第三方授权页面
     */
    @RequestMapping("/render/{source}")
    public void renderAuth(@PathVariable("source") String source, HttpServletResponse response) throws IOException {
        OauthClientDetails ocd = oauthClientDetailsService.findByTypes(source);

        if (ObjectUtil.isEmpty(ocd)){
            response.setCharacterEncoding("utf-8");
            response.setHeader("content-type", "text/html; charset=UTF-8");
            PrintWriter writer = response.getWriter();
            writer.write(JSON.toJSONString(R.error("未授权的平台")));
        }else {
            AuthRequest authRequest = getAuthRequest(ocd);
            String authorizeUrl = authRequest.authorize(AuthStateUtils.createState());
            response.sendRedirect(authorizeUrl);
        }
    }

    /**
     * 用户在确认第三方平台授权（登录）后， 第三方平台会重定向到该地址，并携带code、state等参数
     */
    @RequestMapping("/callback/{source}")
    public Object login(@PathVariable("source") String source, AuthCallback callback) {
        OauthClientDetails ocd = oauthClientDetailsService.findByTypes(source);

        if (ObjectUtil.isEmpty(ocd)){
            return R.error("未授权的平台");
        }

        AuthRequest authRequest = getAuthRequest(ocd);

        AuthResponse login = authRequest.login(callback);

        if (login.getCode() == 2000){
            AuthUser authUser = (AuthUser) login.getData();
            AuthToken token = authUser.getToken();

            //1 查询用户与第三方相关联是否存在
            UserThirdParty thirdUser = userThirdPartyService.getThirdUser(authUser.getUuid(), authUser.getSource());

            //2 不存在则新增
            if (ObjectUtil.isEmpty(thirdUser)){
                thirdUser = userThirdPartyService.save(authUser);
            }else {
                //刷新token
                if (!thirdUser.getAccessToken().equals(token.getAccessToken())){
                    thirdUser.setAccessToken(token.getAccessToken());
                    thirdUser.setRefreshToken(token.getRefreshToken());
                    userThirdPartyService.update(thirdUser);
                }
            }

            //将token存入缓存
            stringRedisTemplate.opsForValue().set(
                    authUser.getSource() + ":" + authUser.getUuid(),
                    token.getAccessToken(),
                    token.getExpireIn(),
                    TimeUnit.SECONDS);

            //3 获取用户在本系统token信息
            return userThirdPartyService.loginThird(thirdUser);
        }else {
            return R.error(login.getMsg());
        }
    }

    /**
     * 获取授权Request
     *
     * @return AuthRequest
     */
    private AuthRequest getAuthRequest(OauthClientDetails ocd) {
        String resourceIds = ocd.getTypes();

        AuthConfig build = AuthConfig.builder()
                .clientId(ocd.getClientId())
                .clientSecret(ocd.getClientSecret())
                .redirectUri(ocd.getWebServerRedirectUri())
                .build();

        AuthDefaultRequest defaultRequest = null;
        switch (resourceIds){
            case "github":
                defaultRequest = new AuthGithubRequest(build);
                break;
            case "gitee":
                defaultRequest = new AuthGiteeRequest(build);
                break;
            case "oschina":
                defaultRequest = new AuthOschinaRequest(build);
                break;
            //...其他方式
            default:
                break;
        }

        return defaultRequest;
    }


}
