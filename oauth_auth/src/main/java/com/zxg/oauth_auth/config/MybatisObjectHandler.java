package com.zxg.oauth_auth.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

/**
 * @Author: zhou_xg
 * @Date: 2021/4/28
 * @Purpose: 设置对象的实体类中，新增/更新操作，时间字段自动填充
 */
@Configuration
public class MybatisObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        setFieldValByName("createTime", LocalDateTime.now(),metaObject);
        setFieldValByName("updateTime",LocalDateTime.now(),metaObject);
        setFieldValByName("modifyTime",LocalDateTime.now(),metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        setFieldValByName("updateTime",LocalDateTime.now(),metaObject);
        setFieldValByName("modifyTime",LocalDateTime.now(),metaObject);
    }
}
