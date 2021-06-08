package com.zxg.oauth_auth.web.controller;

import com.zxg.oauth_auth.service.LoginServer;
import com.zxg.oauth_auth.service.UserThirdPartyService;
import com.zxg.oauth_auth.web.dto.BuildUserDTO;
import com.zxg.oauth_auth.web.dto.LoginDTO;
import com.zxg.oauth_auth.web.dto.WxLoginDTO;
import com.zxg.oauth_common.data.constant.InfoConstant;
import com.zxg.oauth_common.data.vo.LoginSuccessVO;
import com.zxg.oauth_common.other.R;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @Author: zhou_xg
 * @Date: 2021/6/8
 * @Purpose:
 */

@RestController
public class LoginController {

    @Autowired
    private LoginServer loginServer;
    @Autowired
    private UserThirdPartyService userThirdPartyService;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @PostMapping("/currencyLogin")
    private R currencyLogin(@RequestBody LoginDTO dto){
        LoginSuccessVO vo = loginServer.currencyLogin(dto);

        if (vo.getSuccess()){
            return R.ok(vo);
        }else {
            return R.error(vo.getCode(),vo.getErrorInfo());
        }
    }

    @PostMapping("/wxClientLogin")
    private R wxClientLogin(@RequestBody WxLoginDTO wxLoginDTO){
        LoginSuccessVO vo = loginServer.wxClientLogin(wxLoginDTO);

        if (vo.getSuccess()){
            return R.ok(vo);
        }else {
            return R.error(vo.getCode(),vo.getErrorInfo());
        }
    }

    @PostMapping("/buildUser")
    private R buildUser(@RequestBody BuildUserDTO dto){

        //先判断第三方用户是否存在且是否允许绑定
        userThirdPartyService.check(dto.getThirdId());

        //调用登录接口
        LoginSuccessVO vo = loginServer.currencyLogin(dto);

        //判断是否成功，成功则绑定且返回
        if (vo.getSuccess()){
            boolean build = userThirdPartyService.buildUser(vo.getUserId(), dto.getThirdId());
            if (!build){
                return R.error("账户异常,绑定失败");
            }
            return R.ok(vo);
        }else {
            return R.error(vo.getCode(),vo.getErrorInfo());
        }
    }

    @GetMapping("/getLoginSmsCode")
    private R getLoginSmsCode(String phone){
        ValueOperations<String, String> operations = redisTemplate.opsForValue();

        //判断当前账户获取验证码是否繁忙
        String mobileNumKey = InfoConstant.MOBILE_NUM_REDIS_PREFIX + phone;
        String mobileNumValue = operations.get(mobileNumKey);
        int num = 0;
        if (StringUtils.isNotBlank(mobileNumValue)){
            num = Integer.parseInt(mobileNumValue);

            if (InfoConstant.GET_SMS_CODE_NUM == num){
                return R.error("获取验证码繁忙，请稍后重试");
            }
        }
        num++;

        //生成验证码
        String smsCode = generateSmsCode();
        //验证码存入缓存 5分钟有效期
        String smsCodeKey = InfoConstant.SMS_CODE_REDIS_PREFIX + phone;
        operations.set(smsCodeKey,smsCode,5, TimeUnit.MINUTES);
        //繁忙次数存入缓存 3分钟有效期
        operations.set(mobileNumKey, String.valueOf(num),3, TimeUnit.MINUTES);

        //调用第三方发送短信


        return R.ok("");
    }

    /**
     * 随机生成6位验证码
     */
    private String generateSmsCode(){
        Random random = new Random();
        int randomNum = random.nextInt(1000000);
        return String.format("%06d", randomNum);
    }


}
