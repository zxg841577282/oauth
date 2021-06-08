package com.zxg.oauth_common.data.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Author: zhou_xg
 * @Date: 2021/5/17
 * @Purpose:
 */
@TableName(value ="user_third_party")
@Data
public class UserThirdParty {

    @TableId(type = IdType.INPUT)
    private String id;

    /**
     * 用户Id
     */
    private Long userId;

    /**
     * 类型
     */
    private String types;

    private String accessToken;
    private String refreshToken;

    private String openId;

}
