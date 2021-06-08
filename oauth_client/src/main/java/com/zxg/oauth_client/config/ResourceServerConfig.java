package com.zxg.oauth_client.config;

import com.zxg.oauth_common.other.CustomAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;


/**
 * @Author: zhou_xg
 * @Date: 2021/4/26
 * @Purpose: 资源服务器配置
 */

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    private static final String RESOURCE_IDS = "order2";

    @Autowired
    RedisConnectionFactory redisConnectionFactory;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.resourceId(RESOURCE_IDS)
                .stateless(true)
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                .tokenStore(new RedisTokenStore(redisConnectionFactory))
        ;
    }

    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeRequests()
                .antMatchers(
                        "/oauth/**",
                        "/swagger-resources/**",
                        "/webjars/**",
                        "/v2/api-docs"
                ).permitAll()
                .antMatchers("/**").authenticated()
        ;
    }
}
