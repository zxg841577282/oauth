package com.zxg.oauth_auth.other.provider;

import com.zxg.oauth_auth.service.UserService;
import com.zxg.oauth_common.data.vo.MyUserDetails;
import com.zxg.oauth_common.other.token.ThirdAuthenticationToken;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

/**
 * @Author: zhou_xg
 * @Date: 2021/5/18
 * @Purpose:
 */
@Component
public class ThirdProvider implements AuthenticationProvider {
    @Autowired
    private UserService userService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        ThirdAuthenticationToken accountLoginToken = (ThirdAuthenticationToken) authentication;

        MyUserDetails userDetails = userService.loadUserByThirdAndToken(accountLoginToken.getName(),accountLoginToken.getTypes());

        String redisToken = stringRedisTemplate.opsForValue().get(accountLoginToken.getTypes() + ":" + accountLoginToken.getPrincipal());
        if (StringUtils.isBlank(redisToken) || !redisToken.equals(accountLoginToken.getCredentials())){
            throw new BadCredentialsException("授权过期,请重新授权登录");
        }

        return new ThirdAuthenticationToken(userDetails, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return ThirdAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
