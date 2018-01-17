package com.clearbill.util;


public class ResultUtil {
	
	public static ResultVo success(Object data){
		ResultVo result = new ResultVo();
		result.setCode(0);
		result.setMsg("成功");
		result.setData(data);
		return result;
	}
	
	public static ResultVo success(){
		ResultVo result = new ResultVo();
		result.setCode(0);
		result.setMsg("成功");
		return result;
	}
	
	public static ResultVo error(String msg){
		ResultVo result = new ResultVo();
		result.setCode(1);
		result.setMsg(msg);
		return result;
	}
	
	public static ResultVo error(String msg,Object data){
		ResultVo result = new ResultVo();
		result.setCode(1);
		result.setMsg(msg);
		result.setData(data);
		return result;
	}

}
