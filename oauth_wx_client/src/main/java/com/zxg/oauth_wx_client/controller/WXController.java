package com.zxg.oauth_wx_client.controller;

import com.zxg.oauth_common.other.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: zhou_xg
 * @Date: 2021/6/8
 * @Purpose:
 */
@RestController
public class WXController {

    @GetMapping("/getWxInfo")
    public R getWxInfo(){



        return R.ok();
    }


}
