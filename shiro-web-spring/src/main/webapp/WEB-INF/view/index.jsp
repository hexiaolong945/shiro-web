<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<!-- 引入Shiro框架的标签库 -->
<%@ taglib uri="http://shiro.apache.org/tags" prefix="shiro" %>
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
我是首页
<!-- 
	从Shiro标签获取当前的身份信息
	身份是从Session中获取的
	new SimpleAuthenticationInfo(principal, credentials, this.getName())
	从Realm中认证信息对象的第一个参数
		如果：单个账号username：shiro:principal
		如果：是User对象获取对象的属性：shiro:principal property="username" 
		
 -->
<span style="float: right;">欢迎,<span style="color: red;"><shiro:principal /></span>
&nbsp;&nbsp;<a href="${pageContext.request.contextPath }/logout.do">退出登录</a></span><br>

<hr>

<!-- 
	判断权限标签
 -->
 <shiro:hasPermission name="user:list">
	<a href="${pageContext.request.contextPath }/user/list.do">用户管理</a><br>
 </shiro:hasPermission>
 <shiro:hasPermission name="role:list">
	<a href="${pageContext.request.contextPath }/role/list.do">角色管理</a> 
 </shiro:hasPermission>

</body>
</html>