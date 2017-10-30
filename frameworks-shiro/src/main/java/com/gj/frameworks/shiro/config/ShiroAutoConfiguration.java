package com.gj.frameworks.shiro.config;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.gj.frameworks.shiro.base.DefaultUserRealm;
import com.gj.frameworks.shiro.base.RedisCacheManager;
import com.gj.frameworks.shiro.base.RedisSessionDao;

@Configuration
public class ShiroAutoConfiguration implements EnvironmentAware {

	private static final String REDIS_CACHE = "redis";

	private Environment env;

	@Bean
	public ShiroProperties shiroProperties() {
		ShiroProperties shiroProperties = new ShiroProperties();
		shiroProperties.setCache(env.getProperty("spring.shiro.cache"));
		shiroProperties.setTimeout(env.getProperty("spring.shiro.timeout", Integer.class));
		return shiroProperties;
	}

	/**
	 * 身份认证realm; (这个需要自己写，账号密码校验；权限等)
	 * 
	 * @return
	 */
	@Bean
	public DefaultUserRealm defaultUserRealm() {
		DefaultUserRealm userRealm = new DefaultUserRealm();
		return userRealm;
	}

	@Bean
	public LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
		return new LifecycleBeanPostProcessor();
	}

	/**
	 * cacheManager 缓存 redis实现 使用的是shiro-redis开源插件
	 * 
	 * @return
	 */
	public CacheManager cacheManager() {
		if (REDIS_CACHE.equals(shiroProperties().getCache())) {
			return new RedisCacheManager();
		} else {
			EhCacheManager cacheManager = new EhCacheManager();
			cacheManager.setCacheManagerConfigFile("classpath:ehcache.xml");
			return cacheManager;
		}
	}

	/**
	 * RedisSessionDAO shiro sessionDao层的实现 通过redis 使用的是shiro-redis开源插件
	 */
	@Bean
	public SessionDAO sessionDAO() {
		if (REDIS_CACHE.equals(shiroProperties().getCache())) {
			RedisSessionDao redisSessionDao = new RedisSessionDao();
			redisSessionDao.setExpireTime(shiroProperties().getTimeout());
			return redisSessionDao;
		} else {
			return null;
		}
	}

	/**
	 * Session Manager 使用的是shiro-redis开源插件
	 */
	@Bean
	public DefaultWebSessionManager sessionManager() {
		DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
		SessionDAO sessionDAO = sessionDAO();
		if (sessionDAO != null) {
			sessionManager.setSessionDAO(sessionDAO);
		}
		sessionManager.setCacheManager(cacheManager());
		sessionManager.setGlobalSessionTimeout(shiroProperties().getTimeout());
		sessionManager.setDeleteInvalidSessions(true);
		sessionManager.setSessionValidationSchedulerEnabled(true);
		sessionManager.setDeleteInvalidSessions(true);
		return sessionManager;
	}

	@Bean
	public DefaultWebSecurityManager securityManager() {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		// 设置realm.
		securityManager.setRealm(defaultUserRealm());
		// 自定义session管理 使用redis
		securityManager.setSessionManager(sessionManager());
		// 自定义缓存实现 使用redis
		securityManager.setCacheManager(cacheManager());
		return securityManager;
	}

	@Bean
	public AuthorizationAttributeSourceAdvisor getAuthorizationAttributeSourceAdvisor() {
		AuthorizationAttributeSourceAdvisor aasa = new AuthorizationAttributeSourceAdvisor();
		aasa.setSecurityManager(securityManager());
		return new AuthorizationAttributeSourceAdvisor();
	}

	@Bean
	public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
		DefaultAdvisorAutoProxyCreator daap = new DefaultAdvisorAutoProxyCreator();
		daap.setProxyTargetClass(true);
		return daap;
	}

	@Bean
	@ConditionalOnMissingBean
	public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();

		// 必须设置 SecurityManager
		shiroFilterFactoryBean.setSecurityManager(securityManager);

		// 如果不设置默认会自动寻找Web工程根目录下的"/login.jsp"页面
		shiroFilterFactoryBean.setLoginUrl("/login");
		// 登录成功后要跳转的链接
		shiroFilterFactoryBean.setSuccessUrl("/index");
		// 未授权界面;
		shiroFilterFactoryBean.setUnauthorizedUrl("/403");

		// 自定义拦截器
		// Map<String, Filter> filtersMap = new LinkedHashMap<String, Filter>();
		// shiroFilterFactoryBean.setFilters(filtersMap);

		// 权限控制map.
		Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();

		filterChainDefinitionMap.put("/login", "anon");
		filterChainDefinitionMap.put("/**", "authc");
		shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
		return shiroFilterFactoryBean;
	}

	@Override
	public void setEnvironment(Environment environment) {
		this.env = environment;
	}

}
