package com.zxg.oauth_common.data.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;

import java.util.List;

/**
 * @Author: zhou_xg
 * @Date: 2021/5/10
 * @Purpose:
 */
@Getter
@Setter
public class LoginSuccessVO {
    private String access_token;
    private String token_type;
    private String refresh_token;
    private int expires_in;
    private String scope;

    //第三方id
    private String thirdId;

    private Boolean needBuild = false;

    @JsonIgnore
    private String message;
    @JsonIgnore
    private String msg;
    @JsonIgnore
    private Integer code;
    @JsonIgnore
    private Long userId;

    @JsonIgnore
    public String getErrorInfo(){
        if (StringUtils.isNotBlank(this.message)){
            return this.message;
        }
        if (StringUtils.isNotBlank(this.msg)){
            return this.msg;
        }
        return "未知错误";
    }

    @JsonIgnore
    public boolean getSuccess(){
        if (StringUtils.isNotBlank(this.access_token)){
            return true;
        }else {
            return false;
        }
    }

}
