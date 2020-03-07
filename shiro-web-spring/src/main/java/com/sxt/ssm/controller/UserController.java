package com.sxt.ssm.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/user")
@Controller
public class UserController {

	/*
	 * 此方法时Shiro认证失败以后的跳转方法
	 */
	@RequestMapping("/login")
	public String login(HttpServletRequest request) {
		
		/*
		 * 获取认证失败的错误信息
		 */
		String shiroLoginFailure = (String) request.getAttribute("shiroLoginFailure");
		System.out.println("shiroLoginFailure："+shiroLoginFailure);
		/*
		 * UnknownAccountException
		 * IncorrectCredentialsException
		 */
		
		if (UnknownAccountException.class.getName().equals(shiroLoginFailure)) {
			request.setAttribute("errorMsg", "亲，账号不存在");
		}else if(IncorrectCredentialsException.class.getName().equals(shiroLoginFailure)) {
			request.setAttribute("errorMsg", "亲，密码错误");
			
		}
		
		return "forward:/login.jsp";
	}
	
	/*
	 * @RequiresPermissions("user:list")
	 * Shiro框架的权限注释
	 */
	@RequiresPermissions("user:list")
	@RequestMapping("/list")
	public String list() {
		
		return "user_list";
	}
}
