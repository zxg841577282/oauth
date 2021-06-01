package com.zxg.oauth_common.data.vo;

import com.zxg.oauth_common.data.entity.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @Author: zhou_xg
 * @Date: 2021/5/31
 * @Purpose:
 */
@Data
public class MyUserDetails implements UserDetails {
    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 状态 0锁定 1有效
     */
    private String status;

    /**
     * 0未删 1删除
     */
    private Boolean isDelete;

    /**
     * 权限
     */
    private Collection<GrantedAuthority> authorities;

    public MyUserDetails() {
    }

    public MyUserDetails(User user, List<String> authList) {
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.status = user.getStatus();
        this.isDelete = user.getIsDelete();

        Collection<GrantedAuthority> authorities = new ArrayList<>();
        for (String s : authList) {
            authorities.add(new SimpleGrantedAuthority(s));
        }
        this.authorities = authorities;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.status.equals("1");
    }

    /**
     * 凭证是否过期
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return (!this.isDelete && this.status.equals("1"));
    }
}
