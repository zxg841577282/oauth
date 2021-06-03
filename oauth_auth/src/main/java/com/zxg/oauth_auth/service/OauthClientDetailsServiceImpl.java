package com.zxg.oauth_auth.service;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.zxg.oauth_common.dao.OauthClientDetailsMapper;
import com.zxg.oauth_common.data.entity.OauthClientDetails;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author: zhou_xg
 * @Date: 2021/5/14
 * @Purpose:
 */
@Service
public class OauthClientDetailsServiceImpl implements OauthClientDetailsService {
    @Resource
    private OauthClientDetailsMapper oauthClientDetailsMapper;


    @Override
    public OauthClientDetails findByTypes(String source) {
        return oauthClientDetailsMapper.selectOne(new LambdaUpdateWrapper<OauthClientDetails>()
            .eq(OauthClientDetails::getTypes,source)
        );
    }
}
