package com.zxg.oauth_auth.service;

import com.zxg.oauth_common.data.vo.MyUserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    MyUserDetails loadUserByMobile(String mobile);
}
