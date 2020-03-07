<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<h3>登录页面</h3>

<!-- 
	method：表单必须是post，shiro框架底层判断，只有post才有效
	action：必须是认证失败的页面，Shiro底层也会判断
	
	账号：默认必须叫做username
	密码：默认必须叫做password
	记住我：默认必须叫做rememberMe
 -->
<span style="color: red">${errorMsg }</span>

<form action="${pageContext.request.contextPath}/user/login.do" method="post" >
	账号：<input type="text" name="username" /><br/>
	密码：<input type="password" name="password" /><br/>
	记住我：<input type="checkbox" name="rememberMe" /><br/>
	<button type="submit" >登录</button>
</form>