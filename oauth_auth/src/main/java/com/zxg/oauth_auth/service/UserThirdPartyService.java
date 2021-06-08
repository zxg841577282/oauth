package com.zxg.oauth_auth.service;

import com.zxg.oauth_common.data.entity.UserThirdParty;
import com.zxg.oauth_common.other.R;
import me.zhyd.oauth.model.AuthUser;

public interface UserThirdPartyService {
    /**
     * 用户唯一id和类型查询
     */
    UserThirdParty getThirdUser(String uuid, String source);

    /**
     * 新增第三方用户
     */
    UserThirdParty save(AuthUser authUser);

    /**
     * 第三方登录
     */
    R loginThird(UserThirdParty thirdUser);

    /**
     * 封装成直接输出的对象
     * back: oauth2 获取token 返回的字符串
     */
    R build(String back);

    /**
     * 更新
     */
    boolean update(UserThirdParty thirdUser);

    /**
     * 绑定第三方账户
     */
    boolean buildUser(Long userId, String thirdId);

    /**
     * 判断账户
     */
    void check(String thirdId);
}
