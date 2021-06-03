package com.zxg.oauth_auth.other.provider;

import com.zxg.oauth_auth.service.UserService;
import com.zxg.oauth_common.data.vo.MyUserDetails;
import com.zxg.oauth_common.other.token.MobileAuthenticationToken;
import com.zxg.oauth_common.data.constant.TokenConstant;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * @Author: zhou_xg
 * @Date: 2021/5/14
 * @Purpose: 自定义手机号登录验证逻辑处理
 */
@Component
public class MobileProvider implements AuthenticationProvider {
    @Autowired
    private UserService userService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        MobileAuthenticationToken accountLoginToken = (MobileAuthenticationToken) authentication;

        MyUserDetails userDetails = userService.loadUserByMobile(accountLoginToken.getName());

        //若验证码不为空 则以验证码模式
        if (StringUtils.isNotBlank(accountLoginToken.getSmsCode())) {
            String redisCode = stringRedisTemplate.opsForValue().get(TokenConstant.SMS_CODE_REDIS_PREFIX + accountLoginToken.getPrincipal());
            if ( redisCode != null) {
                if (!redisCode.equalsIgnoreCase(accountLoginToken.getSmsCode())) {
                    throw new BadCredentialsException("验证码不正确");
                }
            } else {
                throw new BadCredentialsException("验证码已失效！");
            }

            //删除缓存中的验证码
//            stringRedisTemplate.delete(TokenConstant.SMS_CODE_REDIS_PREFIX + accountLoginToken.getPrincipal());

            //校验成功，返回一个授权过的Token
            return new MobileAuthenticationToken(userDetails, userDetails.getAuthorities());
        } else {
            //否则是手机号+密码模式
            if (NoOpPasswordEncoder.getInstance().matches(authentication.getCredentials().toString(), userDetails.getPassword())) {
                //返回一个授权过的Token
                return new MobileAuthenticationToken(userDetails, userDetails.getAuthorities());
            }
        }
        throw new BadCredentialsException("用户名密码不正确");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return MobileAuthenticationToken.class.isAssignableFrom(authentication);
    }

}
