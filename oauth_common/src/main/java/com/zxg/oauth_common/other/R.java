package com.zxg.oauth_common.other;


import java.util.HashMap;
import java.util.Map;

/**
 * 默认返回数据
 */
public class R extends HashMap<String, Object> {
	private static final long serialVersionUID = 1L;

	
	public R() {
		//1 成功
		put("code", 1);
		put("msg", "success");
	}
	
	public static R error(int code, String msg) {
		R r = new R();
		r.put("code", code);
		r.put("msg", msg);
		return r;
	}

	public static R error(String msg) {
		R r = new R();
		r.put("code", 500);
		r.put("msg", msg);
		return r;
	}

	public static R ok(String msg) {
		R r = new R();
		r.put("msg", msg);
		return r;
	}
	
	public static R ok(Map<String, Object> map) {
		R r = new R();
		r.putAll(map);
		return r;
	}

	public static R ok(Object data) {
		R r = new R();
		r.put("data", data);
		return r;
	}
	
	public static R ok() {
		return new R();
	}

	public static <T> R status(boolean flag) {
		return flag ? ok("操作成功") : error("操作失败");
	}

	public static <T> R status(boolean flag,String str) {
		return flag ? ok("操作成功") : error(str);
	}
}
