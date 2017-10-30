package com.gj.frameworks.shiro.base;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

/**
 * shiro身份校验核心类 默认
 * 
 */

public class DefaultUserRealm extends AuthorizingRealm {
	
	private static final String DEF_USERNAME="admin";
	private static final String DEF_PASSWORD="admin";

	/**
	 * 认证信息.(身份验证) : Authentication 是用来验证用户身份
	 * @param token
	 * @return
	 * @throws AuthenticationException
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
		String name = token.getUsername();
		String password = String.valueOf(token.getPassword());
		if(!DEF_USERNAME.equals(name)) {
			throw new UnknownAccountException();
		}
		if( !DEF_PASSWORD.equals(password)) {
			 throw new IncorrectCredentialsException();
		}
		return new SimpleAuthenticationInfo(name, password, getName());
	}

	/**
	 * 授权
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		return info;
	}
}
