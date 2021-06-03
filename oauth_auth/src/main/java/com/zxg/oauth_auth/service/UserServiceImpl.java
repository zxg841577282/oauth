package com.zxg.oauth_auth.service;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zxg.oauth_common.dao.UserMapper;
import com.zxg.oauth_common.data.entity.User;
import com.zxg.oauth_common.data.entity.UserThirdParty;
import com.zxg.oauth_common.data.vo.MyUserDetails;
import com.zxg.oauth_common.other.Asserts;
import me.zhyd.oauth.model.AuthUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: zhou_xg
 * @Date: 2021/5/31
 * @Purpose:
 */
@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;
    @Autowired
    private UserThirdPartyService userThirdPartyService;


    @Override
    public MyUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
        if (ObjectUtil.isEmpty(user)){
            throw new RuntimeException("用户不存在");
        }

        //获取用户权限
        List<String> authList = new ArrayList<>();

        return new MyUserDetails(user,authList);
    }


    @Override
    public MyUserDetails loadUserByMobile(String mobile) {

        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getMobile, mobile));
        if (ObjectUtil.isEmpty(user)){
            throw new RuntimeException("用户不存在");
        }

        //获取用户权限
        List<String> authList = new ArrayList<>();

        return new MyUserDetails(user,authList);
    }

    @Override
    public User saveByThirdUser(AuthUser authUser) {
        User user = new User();

        user.setUsername(authUser.getUsername());
        user.setPassword("123456");

        userMapper.insert(user);

        return user;
    }


    @Override
    public MyUserDetails loadUserByThirdAndToken(String id, String types) {
        UserThirdParty thirdUser = userThirdPartyService.getThirdUser(id, types);

        Asserts.fail(ObjectUtil.isEmpty(thirdUser),"用户不存在");

        User user = userMapper.selectById(thirdUser.getUserId());

        Asserts.fail(ObjectUtil.isEmpty(user),"用户不存在");

        return getUserVO(user);
    }

    private MyUserDetails getUserVO(User user){
        //获取用户权限
        List<String> authList = new ArrayList<>();

        return new MyUserDetails(user,authList);
    }
}
