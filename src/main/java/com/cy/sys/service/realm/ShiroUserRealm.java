package com.cy.sys.service.realm;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.cy.sys.dao.SysMenuDao;
import com.cy.sys.dao.SysRoleMenuDao;
import com.cy.sys.dao.SysUserDao;
import com.cy.sys.dao.SysUserRoleDao;
import com.cy.sys.entity.SysUser;

@Service
public class ShiroUserRealm extends AuthorizingRealm {
	@Autowired
	private SysUserDao sysUserDao;
	
//	@Autowired
//	private SysUserRoleDao sysUserRoleDao;
//	
//	@Autowired
//	private SysRoleMenuDao sysRoleMenuDao;
//	
//	@Autowired
//	private SysMenuDao sysMenuDao;
	/**
	 * 设置凭证匹配器，通过此对象指定加密算法
	 */
	@Override
	public void setCredentialsMatcher(CredentialsMatcher credentialsMatcher) {
		//构建凭证匹配器对象
		HashedCredentialsMatcher cMatcher = new HashedCredentialsMatcher();
		//设置算法
		cMatcher.setHashAlgorithmName("MD5");
		//设置加密次数
		cMatcher.setHashIterations(1);
		//传递凭证匹配器
		super.setCredentialsMatcher(cMatcher);
	}
	
	/**
	 * 负责认证信息的获取及封装
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		//1.获取用户名
		UsernamePasswordToken upToken = (UsernamePasswordToken) token;
		String username = upToken.getUsername();
		//2.基于用户名查询信息
		SysUser user = sysUserDao.findUserByUserName(username);
		//3.判定用户是否存在
		if (user == null) {
			throw new UnknownAccountException();
		}
		//4.判定用户是否已被禁用
		if (user.getValid() == 0) {
			throw new LockedAccountException();
		}
		//5.封装用户信息
		ByteSource credentialsSalt = ByteSource.Util.bytes(user.getSalt());
		SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user,user.getPassword(),credentialsSalt,getName());
		//6.返回封装结果
		return info;
	}
	
	/**
	 * 负责授权信息的获取及封装
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
//		//1.获取用户信息
//		SysUser user = (SysUser) principals.getPrimaryPrincipal();
//		//2.基于用户id获取用户拥有的角色（sys_user_roles）
//		List<Integer> roleIds = sysUserRoleDao.findRoleIdsByUserId(user.getId());
//		if(roleIds==null||roleIds.size()==0)
//			throw new AuthorizationException();
//		//3.基于角色id获取菜单id
//		Integer[] array={};
//		List<Integer> menuIds=sysRoleMenuDao.findMenuIdsByRoleIds(roleIds.toArray(array));
//	    if(menuIds==null||menuIds.size()==0)
//	    	throw new AuthorizationException();
//	  //4.基于菜单id获取权限标识(sys_menus)
//	    List<String> permissions=sysMenuDao.findPermissions(menuIds.toArray(array));
//		//5.对权限标识信息进行封装并返回
//	    Set<String> set=new HashSet<>();
//	    for(String per:permissions){
//	    	if(!StringUtils.isEmpty(per)){
//	    		set.add(per);
//	    	}
//	    }
//	    SimpleAuthorizationInfo info= new SimpleAuthorizationInfo();
//	    info.setStringPermissions(set);
//		return info;//返回给授权管理器
		return null;
	}
}
