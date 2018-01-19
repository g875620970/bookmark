package com.clearbill.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.clearbill.dto.UserInfo;
import com.clearbill.dto.groups.LoginRegister;
import com.clearbill.exception.ValidateException;
import com.clearbill.util.JedisUtils;
import com.clearbill.util.ResultUtil;
import com.clearbill.util.ResultVo;

@Controller
public class IndexController extends BaseController {

	private final String PWD_KEY="USER_PASSWORD";
	
	/**
	 * 主页
	 * @return
	 */
	@RequestMapping(value="/index",method=RequestMethod.GET)
	public ModelAndView index(){
		return new ModelAndView("index");
	}
	
	/**
	 * 登录页
	 * @return
	 */
	@RequestMapping(value="/login",method=RequestMethod.GET)
	public ModelAndView login(){
		return new ModelAndView("login");
	}
	
	/**
	 * 注册页
	 * @return
	 */
	@RequestMapping(value="/register",method=RequestMethod.GET)
	public ModelAndView register(){
		return new ModelAndView("register");
	}
	
	/**
	 * 登录
	 * @param request
	 * @param userInfo
	 * @param validateResult
	 * @return
	 * @throws ValidateException
	 */
	@RequestMapping(value="/login",method=RequestMethod.POST)
	@ResponseBody
	public ResultVo login(HttpServletRequest request,@Validated(LoginRegister.class) UserInfo userInfo,BindingResult validateResult) throws ValidateException{
		validateParam(validateResult);
		String userName = userInfo.getUserName();
		String md5Pwd = DigestUtils.md5Hex(userInfo.getPwd());
		String relPwd = JedisUtils.getInstall().hget(PWD_KEY,userName);
		if(relPwd==null || !md5Pwd.equals(relPwd)){
			return ResultUtil.error("用户不存在或密码错误");
		}
		HttpSession httpSession = request.getSession();
		httpSession.setAttribute("userName", userName);
		return ResultUtil.success();
	}
	
	/**
	 * 注册
	 * @param userInfo
	 * @param validateResult
	 * @return
	 * @throws ValidateException
	 */
	@RequestMapping(value="/register",method=RequestMethod.POST)
	@ResponseBody
	public ResultVo register(@Validated(LoginRegister.class) UserInfo userInfo,BindingResult validateResult) throws ValidateException{
		validateParam(validateResult);
		String userName = userInfo.getUserName();
		String md5Pwd = DigestUtils.md5Hex(userInfo.getPwd());
		JedisUtils.getInstall().hset(PWD_KEY,userName,md5Pwd);
		return ResultUtil.success();
	}
	
}
