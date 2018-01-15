package com.clearbill.util;

import com.alibaba.fastjson.JSONObject;

public class ResultUtil {
	
	public static String success(Object data){
		JSONObject json = new JSONObject();
		json.put("code", 0);
		json.put("msg", "success");
		json.put("data", data);
		return json.toJSONString();
	}
	
	public static String error(String msg){
		JSONObject json = new JSONObject();
		json.put("code", 1);
		json.put("msg", msg);
		return json.toJSONString();
	}

}
