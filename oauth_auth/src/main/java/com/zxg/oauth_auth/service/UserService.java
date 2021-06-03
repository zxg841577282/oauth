package com.zxg.oauth_auth.service;

import com.zxg.oauth_common.data.entity.User;
import com.zxg.oauth_common.data.vo.MyUserDetails;
import me.zhyd.oauth.model.AuthUser;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    MyUserDetails loadUserByMobile(String mobile);

    /**
     * 根据第三方用户新增
     */
    User saveByThirdUser(AuthUser authUser);

    MyUserDetails loadUserByThirdAndToken(String id, String types);
}
