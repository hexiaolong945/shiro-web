package com.sxt.ssm.shiro;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

public class CustomRealm extends AuthorizingRealm {

	
	/*
	 * 认证方法：在此方法内部开发者自己完成认证逻辑
	 * 
	 * 认证思路
	 * 
	 * 1.注入Service层，Service依赖dao层，dao层操作数据库
	 * @Autowired
	 * private UserService userService;
	 * 
	 * 2.根据token获取生成调用Service的根据用户名查询用户是否存在的方法
	 * User user = userService.selectByUsername(username);
	 * 	会出现两种情况：
	 * 		情况一：数据库中存在，返回User对象，进行下一步操作
	 * 		情况二：数据库中不在对应的账号，方法直接返回null
	 * 				Shiro底层会抛出：UnknownAccountException，账号不存在
	 * 
	 * 3.创建认证信息对象
	 * SimpleAuthencationInfo
	 * 将查询的身份（user对象），品质（查询的结果的密码）设置给认证信息对象，并返回出去
	 * 
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		//获取身份、账号
		String username = (String) token.getPrincipal();
		
		//模拟数据库中的账号
		List<String> users = Arrays.asList("lucy","lili","jack","rose");
		//模拟根据账号去数据库查询
		if (!users.contains(username)) {
			//数据库不存在此账号
			return null;
		}
		
		//账号存在情况，实际开发在此方法中完成授权的逻辑
		//创建认证信息对象
		/*
		 * Object principal = null;
		 *  身份：service层查询的User对象
		 *  因为当前只是一个模拟过程，没有User对象，所以可以直接使用username
		 *  
		 *  Object credentials = null;
		 *   凭证：密码，从user对象中获取（数据库查询出来的）
		 *   Object credentials = user.getPassword();
		 *   String realmName = null;
		 *   
		 *   当前realm的名称，
		 *   直接调用this.getName()即可
		 */
		Object principal = username;
		
		//明文认证使用
		//AuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(principal, credentials, this.getName());
		
		//数据库查询出来的凭证（密码）Object credentials = user.getPassword();
		//数据库中加密的密码
		Object hashedCredentials = "142a04d176b6960cf517c6b2bac95630";
		
		//获取User对象的salt
		String salt = "qwer";
		ByteSource credentialsSalt = ByteSource.Util.bytes(salt);
		SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(principal, hashedCredentials, credentialsSalt, this.getName());
		
		
		//当前方法返回认证信息对象，Shiro框架底层，会拿到用户提交的密码和数据库查询出的密码比对象，Shiro底层自己完成
		/*
		 * 两种情况
		 * 	情况一：比对成功（认证通过）登录成功
		 * 	情况二：比对失败
		 * IncorrectCredentialsException 无效的凭证异常（密码错误）
		 */
		
		return authenticationInfo;
	}
	
	/*
	 * 授权方法：在此方法内部开发者自己完成授权逻辑
	 * 授权的前提是用户已经认证（登录）通过
	 * 授权思路
	 * 	1.获取认证通过以后啊保存的身份信息
	 * 			实际开发就是user整个对象
	 * User user = (User)principals.getPrimaryPrincipal();
	 * 
	 * 2.获取用户表中角色id（用户肯定有角色外键，对应的角色id），然后根据角色的id查询对应的所有的权限的（1,2,3）
	 * List<Integer> permissionIds = userService.selectPermissionIdsByRoleId(user.getRoleId());
	 *	集合就是一堆权限的id（一个角色有多个权限）1,2,3,4,5
	 *
	 *	3.根据这些权限的id查询出对应的权限表达式（一个资源在数据库表中对应一个权限表达式）
	 *	List<String> permissions = permissionService.selectPermissionByIds(permissionIds);
	 *		此集合就是一堆权限表达式，给Shiro框架用的
	 *			user:insert
	 *			user:list
	 *			user:delete
	 *			user:update
	 *			teacher:list
	 *			student:list
			不同的用户，对应不同的角色，对应的不同的权限，对应不同的权限表达式
			上述的所有相关数据对应的表都是由开发者自己设计自己维护的数据，和Shiro无关，这些数据是支撑Shiro框架安全处理的数据
			
		4.创建授权信息对象（SimpleAuthenticationInfo接口的）
		
		5.将第三步查询的权限表达式的集合添加到授权对象中
		
		6.返回授权信息对象
		
		7.开始授权操作（Shiro底层自动完成）
			授权逻辑：把授权程序入口的权限表达式和数据库查询的这一堆权限表达式比对，比对上，说明有权限
			
			
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		//User user = (User)principals.getPrimaryPrincipal();
		
		System.out.println("CustomRealm.doGetAuthorizationInfo()");
		
		//模拟当前认证用户拥有的角色对应的权限对应的权限表达式
		List<String> permissions = new ArrayList<>();
		permissions.add("user:insert");
		//permissions.add("user:list");
		permissions.add("role:list");
		
		//创建授权信息对象
		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		//查询的权限表达式的集合添加到授权对象中
		authorizationInfo.addStringPermissions(permissions);
		
		return authorizationInfo;
	}

}
