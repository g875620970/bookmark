package com.clearbill.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.validation.groups.Default;

import com.clearbill.dto.groups.LoginRegister;

public class UserInfo {
	/**
	 * 用户名
	 */
	@NotNull(message="用户名不能为空",groups={LoginRegister.class,Default.class})
	@Size(min=5,max=20,message="用户名长度应为5~20",groups={LoginRegister.class,Default.class})
	private String userName;
	/**
	 * 密码
	 */
	@NotNull(message="密码不能为空",groups={LoginRegister.class})
	@Size(min=6,max=16,message="密码长度应为6~16",groups={LoginRegister.class})
	private String pwd;
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	
}
