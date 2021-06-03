package com.zxg.oauth_auth.service;


import com.zxg.oauth_common.data.entity.OauthClientDetails;

public interface OauthClientDetailsService {

    OauthClientDetails findByTypes(String source);
}
