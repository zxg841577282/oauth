package com.zxg.oauth_common.data.constant;

/**
 * @Author: zhou_xg
 * @Date: 2021/6/4
 * @Purpose:
 */

public class InfoConstant {
    /**
     * 登录获取短信验证码Redis前缀
     */
    public static final String SMS_CODE_REDIS_PREFIX = "login_sms_code:";

    /**
     * 手机验证码次数Redis前缀
     */
    public static final String MOBILE_NUM_REDIS_PREFIX = "mobile_num:";

    /**
     * 允许繁忙前最大次数
     */
    public static final int GET_SMS_CODE_NUM = 5;
}
