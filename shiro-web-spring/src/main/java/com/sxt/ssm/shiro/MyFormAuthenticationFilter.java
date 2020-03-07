package com.sxt.ssm.shiro;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

public class MyFormAuthenticationFilter extends FormAuthenticationFilter {

	
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
		//从请求中获取Shiro的 主体
		Subject subject = getSubject(request, response);
//		Subject subject = SecurityUtils.getSubject();
		
		//从主体中获取Shiro框架的Session
		Session session = subject.getSession();
		//如果主体没有认证（Session中认证）并且 主体已经设置记住我了
		if (!subject.isAuthenticated() && subject.isRemembered()) {
			//获取主体的身份（从记住我的Cookie中获取的）
			//User principal = (User) subject.getPrincipal();
			String username = (String) subject.getPrincipal();
			System.out.println("username:"+username);
			//将身份认证信息共享到 Session中
			session.setAttribute("USER_IN_SESSION", username);
		}
		return subject.isAuthenticated() || subject.isRemembered();
	}
}
