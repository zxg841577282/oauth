package com.zxg.oauth_common.data.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Author: zhou_xg
 * @Date: 2021/5/14
 * @Purpose:
 */
@TableName(value ="oauth_client_details")
@Data
public class OauthClientDetails {

    @TableId
    private String clientId;

    private String resourceIds;
    private String clientSecret;
    private String scope;
    private String authorizedGrantTypes;
    private String webServerRedirectUri;
    private String authorities;
    private Integer accessTokenValidity;
    private Integer refreshTokenValidity;
    private String additionalInformation;
    private String autoapprove;
    private String types;

}
