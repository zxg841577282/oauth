package com.zxg.oauth_auth.web.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author: zhou_xg
 * @Date: 2021/6/4
 * @Purpose:
 */
@Getter
@Setter
public class BuildUserDTO extends LoginDTO {

    @ApiModelProperty(value = "第三方用户id")
    private String thirdId;

}
