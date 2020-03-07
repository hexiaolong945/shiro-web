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
	 * ��֤�������ڴ˷����ڲ��������Լ������֤�߼�
	 * 
	 * ��֤˼·
	 * 
	 * 1.ע��Service�㣬Service����dao�㣬dao��������ݿ�
	 * @Autowired
	 * private UserService userService;
	 * 
	 * 2.����token��ȡ���ɵ���Service�ĸ����û�����ѯ�û��Ƿ���ڵķ���
	 * User user = userService.selectByUsername(username);
	 * 	��������������
	 * 		���һ�����ݿ��д��ڣ�����User���󣬽�����һ������
	 * 		����������ݿ��в��ڶ�Ӧ���˺ţ�����ֱ�ӷ���null
	 * 				Shiro�ײ���׳���UnknownAccountException���˺Ų�����
	 * 
	 * 3.������֤��Ϣ����
	 * SimpleAuthencationInfo
	 * ����ѯ����ݣ�user���󣩣�Ʒ�ʣ���ѯ�Ľ�������룩���ø���֤��Ϣ���󣬲����س�ȥ
	 * 
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		//��ȡ��ݡ��˺�
		String username = (String) token.getPrincipal();
		
		//ģ�����ݿ��е��˺�
		List<String> users = Arrays.asList("lucy","lili","jack","rose");
		//ģ������˺�ȥ���ݿ��ѯ
		if (!users.contains(username)) {
			//���ݿⲻ���ڴ��˺�
			return null;
		}
		
		//�˺Ŵ��������ʵ�ʿ����ڴ˷����������Ȩ���߼�
		//������֤��Ϣ����
		/*
		 * Object principal = null;
		 *  ��ݣ�service���ѯ��User����
		 *  ��Ϊ��ǰֻ��һ��ģ����̣�û��User�������Կ���ֱ��ʹ��username
		 *  
		 *  Object credentials = null;
		 *   ƾ֤�����룬��user�����л�ȡ�����ݿ��ѯ�����ģ�
		 *   Object credentials = user.getPassword();
		 *   String realmName = null;
		 *   
		 *   ��ǰrealm�����ƣ�
		 *   ֱ�ӵ���this.getName()����
		 */
		Object principal = username;
		
		//������֤ʹ��
		//AuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(principal, credentials, this.getName());
		
		//���ݿ��ѯ������ƾ֤�����룩Object credentials = user.getPassword();
		//���ݿ��м��ܵ�����
		Object hashedCredentials = "142a04d176b6960cf517c6b2bac95630";
		
		//��ȡUser�����salt
		String salt = "qwer";
		ByteSource credentialsSalt = ByteSource.Util.bytes(salt);
		SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(principal, hashedCredentials, credentialsSalt, this.getName());
		
		
		//��ǰ����������֤��Ϣ����Shiro��ܵײ㣬���õ��û��ύ����������ݿ��ѯ��������ȶ���Shiro�ײ��Լ����
		/*
		 * �������
		 * 	���һ���ȶԳɹ�����֤ͨ������¼�ɹ�
		 * 	��������ȶ�ʧ��
		 * IncorrectCredentialsException ��Ч��ƾ֤�쳣���������
		 */
		
		return authenticationInfo;
	}
	
	/*
	 * ��Ȩ�������ڴ˷����ڲ��������Լ������Ȩ�߼�
	 * ��Ȩ��ǰ�����û��Ѿ���֤����¼��ͨ��
	 * ��Ȩ˼·
	 * 	1.��ȡ��֤ͨ���Ժ󰡱���������Ϣ
	 * 			ʵ�ʿ�������user��������
	 * User user = (User)principals.getPrimaryPrincipal();
	 * 
	 * 2.��ȡ�û����н�ɫid���û��϶��н�ɫ�������Ӧ�Ľ�ɫid����Ȼ����ݽ�ɫ��id��ѯ��Ӧ�����е�Ȩ�޵ģ�1,2,3��
	 * List<Integer> permissionIds = userService.selectPermissionIdsByRoleId(user.getRoleId());
	 *	���Ͼ���һ��Ȩ�޵�id��һ����ɫ�ж��Ȩ�ޣ�1,2,3,4,5
	 *
	 *	3.������ЩȨ�޵�id��ѯ����Ӧ��Ȩ�ޱ��ʽ��һ����Դ�����ݿ���ж�Ӧһ��Ȩ�ޱ��ʽ��
	 *	List<String> permissions = permissionService.selectPermissionByIds(permissionIds);
	 *		�˼��Ͼ���һ��Ȩ�ޱ��ʽ����Shiro����õ�
	 *			user:insert
	 *			user:list
	 *			user:delete
	 *			user:update
	 *			teacher:list
	 *			student:list
			��ͬ���û�����Ӧ��ͬ�Ľ�ɫ����Ӧ�Ĳ�ͬ��Ȩ�ޣ���Ӧ��ͬ��Ȩ�ޱ��ʽ
			����������������ݶ�Ӧ�ı����ɿ������Լ�����Լ�ά�������ݣ���Shiro�޹أ���Щ������֧��Shiro��ܰ�ȫ���������
			
		4.������Ȩ��Ϣ����SimpleAuthenticationInfo�ӿڵģ�
		
		5.����������ѯ��Ȩ�ޱ��ʽ�ļ�����ӵ���Ȩ������
		
		6.������Ȩ��Ϣ����
		
		7.��ʼ��Ȩ������Shiro�ײ��Զ���ɣ�
			��Ȩ�߼�������Ȩ������ڵ�Ȩ�ޱ��ʽ�����ݿ��ѯ����һ��Ȩ�ޱ��ʽ�ȶԣ��ȶ��ϣ�˵����Ȩ��
			
			
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		//User user = (User)principals.getPrimaryPrincipal();
		
		System.out.println("CustomRealm.doGetAuthorizationInfo()");
		
		//ģ�⵱ǰ��֤�û�ӵ�еĽ�ɫ��Ӧ��Ȩ�޶�Ӧ��Ȩ�ޱ��ʽ
		List<String> permissions = new ArrayList<>();
		permissions.add("user:insert");
		//permissions.add("user:list");
		permissions.add("role:list");
		
		//������Ȩ��Ϣ����
		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		//��ѯ��Ȩ�ޱ��ʽ�ļ�����ӵ���Ȩ������
		authorizationInfo.addStringPermissions(permissions);
		
		return authorizationInfo;
	}

}
