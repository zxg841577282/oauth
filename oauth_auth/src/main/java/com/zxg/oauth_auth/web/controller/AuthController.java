package com.zxg.oauth_auth.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


/**
 * @Author: zhou_xg
 * @Date: 2021/6/1
 * @Purpose:
 */
@Controller
public class AuthController {

    @GetMapping("/homeLogin")
    public String homeLogin(){
        return "homeLogin";
    }

}
