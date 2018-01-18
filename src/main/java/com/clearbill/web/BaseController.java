package com.clearbill.web;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.clearbill.exception.ValidateException;
import com.clearbill.util.ResultUtil;
import com.clearbill.util.ResultVo;

public class BaseController {
	
	/**
	 * 参数校验
	 * @param validateResult
	 * @throws ValidateException
	 */
	protected void validateParam(BindingResult validateResult) throws ValidateException{
		//参数验证
		if(validateResult.hasErrors()){
			throw new ValidateException();
		}
	}

	/**
	 * 异常处理
	 * @param request
	 * @param ex
	 * @return
	 */
	@ExceptionHandler
	@ResponseBody
	protected ResultVo exceptionHandler( Exception ex) {
		ex.printStackTrace();
		if(ex instanceof ValidateException){
			return ResultUtil.error("参数错误");
		}
		return ResultUtil.error("服务器异常");
	}
}
