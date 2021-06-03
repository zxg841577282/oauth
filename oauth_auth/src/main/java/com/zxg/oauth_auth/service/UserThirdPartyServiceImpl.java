package com.zxg.oauth_auth.service;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zxg.oauth_common.dao.UserThirdPartyMapper;
import com.zxg.oauth_common.data.entity.User;
import com.zxg.oauth_common.data.entity.UserThirdParty;
import com.zxg.oauth_common.data.vo.LoginSuccessVO;
import com.zxg.oauth_common.other.R;
import me.zhyd.oauth.model.AuthToken;
import me.zhyd.oauth.model.AuthUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author: zhou_xg
 * @Date: 2021/5/17
 * @Purpose:
 */

@Service
public class UserThirdPartyServiceImpl implements UserThirdPartyService {

    @Resource
    private UserThirdPartyMapper userThirdPartyMapper;
    @Autowired
    private UserService userService;


    @Override
    public UserThirdParty getThirdUser(String uuid, String source) {
        return userThirdPartyMapper.selectOne(new LambdaQueryWrapper<UserThirdParty>()
                .eq(UserThirdParty::getId,Long.valueOf(uuid))
                .eq(UserThirdParty::getTypes,source)
        );
    }

    @Override
    public UserThirdParty save(AuthUser authUser) {
        //新增user
        User user = userService.saveByThirdUser(authUser);

        //绑定
        UserThirdParty userThirdParty = new UserThirdParty();
        userThirdParty.setId(authUser.getUuid());
        userThirdParty.setUserId(user.getId());
        userThirdParty.setTypes(authUser.getSource());
        AuthToken token = authUser.getToken();
        userThirdParty.setAccessToken(token.getAccessToken());
        userThirdParty.setRefreshToken(token.getRefreshToken());
        userThirdPartyMapper.insert(userThirdParty);

        return userThirdParty;
    }

    @Override
    public R loginThird(UserThirdParty thirdUser) {
        //调用自定义第三方授权
        String address = "http://localhost:1000/oauth/token" +
                "?uuid=UUID" +
                "&types=TYPES" +
                "&token=TOKEN" +
                "&grant_type=third" +
                "&scope=all" +
                "&client_id=zxg_client1" +
                "&client_secret=zxg_client1";

        address = address.replace("UUID",thirdUser.getId())
                .replace("TYPES",thirdUser.getTypes())
                .replace("TOKEN",thirdUser.getAccessToken())
        ;

        return build(HttpUtil.post(address,""));
    }



    public R build(String back) {
        LoginSuccessVO loginSuccessVO = JSONObject.parseObject(back, LoginSuccessVO.class);

        if (loginSuccessVO.getSuccess()){
            return R.ok(loginSuccessVO);
        }else {
            return R.error(loginSuccessVO.getCode(),loginSuccessVO.getErrorInfo());
        }
    }

    @Override
    public boolean update(UserThirdParty thirdUser) {
        return userThirdPartyMapper.updateById(thirdUser) > 0;
    }
}
