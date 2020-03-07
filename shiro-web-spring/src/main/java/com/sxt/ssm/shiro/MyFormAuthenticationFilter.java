package com.sxt.ssm.shiro;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

public class MyFormAuthenticationFilter extends FormAuthenticationFilter {

	
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
		//�������л�ȡShiro�� ����
		Subject subject = getSubject(request, response);
//		Subject subject = SecurityUtils.getSubject();
		
		//�������л�ȡShiro��ܵ�Session
		Session session = subject.getSession();
		//�������û����֤��Session����֤������ �����Ѿ����ü�ס����
		if (!subject.isAuthenticated() && subject.isRemembered()) {
			//��ȡ�������ݣ��Ӽ�ס�ҵ�Cookie�л�ȡ�ģ�
			//User principal = (User) subject.getPrincipal();
			String username = (String) subject.getPrincipal();
			System.out.println("username:"+username);
			//�������֤��Ϣ���� Session��
			session.setAttribute("USER_IN_SESSION", username);
		}
		return subject.isAuthenticated() || subject.isRemembered();
	}
}
