package com.zxg.oauth_auth;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.zxg.**.dao")
public class OauthAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(OauthAuthApplication.class, args);
    }

}
