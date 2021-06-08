package com.zxg.oauth_auth.web.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * @Author: zhou_xg
 * @Date: 2021/5/10
 * @Purpose:
 */
@Getter
@Setter
public class LoginDTO {
    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "验证码")
    private String smsCode;

    @NotNull(message = "请选择登录方式")
    @ApiModelProperty(value = "1账户+密码 2手机号+密码 3手机号+验证码",required = true)
    private Integer loginType;
}
