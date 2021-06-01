package com.zxg.oauth_auth.service;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zxg.oauth_common.dao.UserMapper;
import com.zxg.oauth_common.data.entity.User;
import com.zxg.oauth_common.data.vo.MyUserDetails;
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



}
