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
	 * �˷���ʱShiro��֤ʧ���Ժ����ת����
	 */
	@RequestMapping("/login")
	public String login(HttpServletRequest request) {
		
		/*
		 * ��ȡ��֤ʧ�ܵĴ�����Ϣ
		 */
		String shiroLoginFailure = (String) request.getAttribute("shiroLoginFailure");
		System.out.println("shiroLoginFailure��"+shiroLoginFailure);
		/*
		 * UnknownAccountException
		 * IncorrectCredentialsException
		 */
		
		if (UnknownAccountException.class.getName().equals(shiroLoginFailure)) {
			request.setAttribute("errorMsg", "�ף��˺Ų�����");
		}else if(IncorrectCredentialsException.class.getName().equals(shiroLoginFailure)) {
			request.setAttribute("errorMsg", "�ף��������");
			
		}
		
		return "forward:/login.jsp";
	}
	
	/*
	 * @RequiresPermissions("user:list")
	 * Shiro��ܵ�Ȩ��ע��
	 */
	@RequiresPermissions("user:list")
	@RequestMapping("/list")
	public String list() {
		
		return "user_list";
	}
}
