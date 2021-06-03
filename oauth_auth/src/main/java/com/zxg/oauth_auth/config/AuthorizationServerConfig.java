package com.zxg.oauth_auth.config;


import com.zxg.oauth_auth.other.CustomClientCredentialsTokenEndpointFilter;
import com.zxg.oauth_auth.other.CustomWebResponseExceptionTranslator;
import com.zxg.oauth_auth.other.TokenGranter.MobileCustomTokenGranter;
import com.zxg.oauth_auth.other.TokenGranter.ThirdCustomTokenGranter;
import com.zxg.oauth_auth.service.UserService;
import com.zxg.oauth_common.other.CustomAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeTokenGranter;
import org.springframework.security.oauth2.provider.password.ResourceOwnerPasswordTokenGranter;
import org.springframework.security.oauth2.provider.refresh.RefreshTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author: zhou_xg
 * @Date: 2021/5/31
 * @Purpose: 认证服务器配置
 */

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    RedisConnectionFactory redisConnectionFactory;

    @Autowired
    private UserService userService;

    @Resource
    private DataSource dataSource;

    @Autowired
    @Qualifier(value = "customWebResponseExceptionTranslator")
    private CustomWebResponseExceptionTranslator customWebResponseExceptionTranslator;

    @Bean
    public ClientDetailsService clientDetails() {
        return new JdbcClientDetailsService(dataSource);
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        //从数据库读取客户端配置信息
        clients.withClientDetails(clientDetails());
    }

    /**
     * 认证服务端点配置
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints
                //用户管理
                .userDetailsService(userService)
                //token存到redis
                .tokenStore(new RedisTokenStore(redisConnectionFactory))
                //启用oauth2管理
                .authenticationManager(authenticationManager)
                //设置异常处理
                .exceptionTranslator(customWebResponseExceptionTranslator)
                //接收GET和POST
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST)
                //配置token生成方式
                .tokenGranter(new CompositeTokenGranter(getTokenGranters(endpoints.getAuthorizationCodeServices(), endpoints.getTokenServices(), endpoints.getClientDetailsService(), endpoints.getOAuth2RequestFactory())))
        ;
    }

    //设置token的生成者
    private List<TokenGranter> getTokenGranters(AuthorizationCodeServices authorizationCodeServices, AuthorizationServerTokenServices tokenServices, ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory) {
        return new ArrayList<>(Arrays.asList(
                //密码模式
                new ResourceOwnerPasswordTokenGranter(authenticationManager, tokenServices, clientDetailsService, requestFactory),
                //授权码模式
                new AuthorizationCodeTokenGranter(tokenServices, authorizationCodeServices, clientDetailsService, requestFactory),
                //刷新token模式
                new RefreshTokenGranter(tokenServices, clientDetailsService, requestFactory),
                //手机模式
                new MobileCustomTokenGranter(tokenServices, clientDetailsService, requestFactory,authenticationManager),
                //第三方模式
                new ThirdCustomTokenGranter(tokenServices, clientDetailsService, requestFactory,authenticationManager)
        ));
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) {
        CustomClientCredentialsTokenEndpointFilter endpointFilter = new CustomClientCredentialsTokenEndpointFilter(oauthServer);
        endpointFilter.afterPropertiesSet();
        endpointFilter.setAuthenticationEntryPoint(new CustomAuthenticationEntryPoint());
        // 客户端认证之前的过滤器
        oauthServer.addTokenEndpointAuthenticationFilter(endpointFilter);

        //开启远程调用认证服务验证Token
//        oauthServer.checkTokenAccess("isAuthenticated()").allowFormAuthenticationForClients();
    }

}
