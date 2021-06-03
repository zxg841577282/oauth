package com.zxg.oauth_common.other;

/**
 * @Author: zhou_xg
 * @Date: 2021/6/2
 * @Purpose:
 */

public class Asserts {
    public static void fail(String message) {
        throw new RuntimeException(message);
    }

    public static void fail(boolean state,String message){
        if (state){
            throw new RuntimeException(message);
        }
    }
}
