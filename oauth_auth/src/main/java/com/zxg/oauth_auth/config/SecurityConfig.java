package com.zxg.oauth_auth.config;

import com.zxg.oauth_auth.other.provider.MobileProvider;
import com.zxg.oauth_auth.other.provider.MyDaoAuthenticationProvider;
import com.zxg.oauth_auth.other.provider.ThirdProvider;
import com.zxg.oauth_auth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.Resource;


/**
 * @Author: zhou_xg
 * @Date: 2021/5/31
 * @Purpose: Security基础配置
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserService userService;
    @Resource
    private MobileProvider phoneSmsProvider;
    @Resource
    private ThirdProvider thirdProvider;
    @Resource
    private MyDaoAuthenticationProvider myDaoAuthenticationProvider;

    @Bean
    PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    /**
     * 注入AuthenticationManager接口，启用OAuth2密码模式
     *
     * @return
     * @throws Exception
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * 通过HttpSecurity实现Security的自定义过滤配置
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(noAuthUrl()).permitAll()       //此类url不拦截
                .anyRequest().authenticated()               //全部拦截
        ;
    }

    /**
     * 设置不拦截前缀
     */
    private String[] noAuthUrl(){
        return new String[]{
                "/oauth/**",        //oauth原有接口
                "/currencyLogin",   //通用登录接口
                "/buildUser",       //绑定第三方账户接口
                "/getLoginSmsCode", //获取登录验证码接口
                "/third/**"         //第三方登录接口
        };
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService);
        auth.authenticationProvider(phoneSmsProvider);
        auth.authenticationProvider(thirdProvider);
        auth.authenticationProvider(myDaoAuthenticationProvider);
    }

}
