package com.systemDemo.config;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.DispatcherType;

import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;

import com.systemDemo.shiro.CustomRealm;
import com.systemDemo.shiro.RedisSessionDao;

@Configuration
public class ShiroConfiguration {
	
	@Value("${shiro.loginUrl}")
	private String masterLoginUrl;

	@Value("${shiro.jessionid}")
	private String jessionId;
	
	//自定义Realm
    @Bean
    public CustomRealm myShiroRealm() {
    	CustomRealm myShiroRealm = new CustomRealm();
    	myShiroRealm.setCredentialsMatcher(credentialsMatcher());
        return myShiroRealm;
    }
    
   //凭证匹配器
    @Bean
    public CredentialsMatcher credentialsMatcher() {
    	HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
    	credentialsMatcher.setHashAlgorithmName("md5");
    	credentialsMatcher.setHashIterations(1);
        return credentialsMatcher;
    }

    //Filter工厂，设置对应的过滤条件和跳转条件
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        
        //登录
        shiroFilterFactoryBean.setLoginUrl("/tologin");
        //首页
        //shiroFilterFactoryBean.setSuccessUrl("/index");
        //错误页面，认证不通过跳转
        shiroFilterFactoryBean.setUnauthorizedUrl("/error");
        // 拦截器.
        Map<String,String> map = new HashMap<String, String>();
        //登出
        map.put("/static/**", "anon");
        map.put("/sys/vcode","anon");
        map.put("/sys/getMenus","anon");
        map.put("/sys/login","anon");
        map.put("/sys/index","anon");
        map.put("/druid/**","anon");
        map.put("/account/active","anon");
		map.put("/loginOut","logout");
        //对所有用户认证
        map.put("/**","authc");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(map);
        return shiroFilterFactoryBean;
    }
    
    //权限管理，配置主要是Realm的管理认证
    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(myShiroRealm());
        securityManager.setSessionManager(sessionManager());
        return securityManager;
    }
    
   //会话管理器
    @Bean
    public SessionManager sessionManager() {
    	DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
    	sessionManager.setGlobalSessionTimeout(86400000);
    	sessionManager.setDeleteInvalidSessions(true);
    	sessionManager.setSessionDAO(getRedisSessionDao());
		sessionManager.setSessionValidationSchedulerEnabled(true);
		sessionManager.setDeleteInvalidSessions(true);
		sessionManager.setSessionIdCookie(getSessionIdCookie());
        return sessionManager;
    }
    
    @Bean
	public RedisSessionDao getRedisSessionDao(){
		return new RedisSessionDao();
	}
    
    /**
	 * 给shiro的sessionId默认的JSSESSIONID名字改掉
	 * @return
	 */
	@Bean(name="sessionIdCookie")
	public SimpleCookie getSessionIdCookie(){
		SimpleCookie simpleCookie = new SimpleCookie(jessionId);
		return simpleCookie;
	}

    //加入注解的使用，不加入这个注解不生效
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }
    
    /**
	 * FilterRegistrationBean
	 * @return
	 */
	@Bean
	public FilterRegistrationBean filterRegistrationBean() {
		FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
        filterRegistration.setFilter(new DelegatingFilterProxy("shiroFilter")); 
        filterRegistration.setEnabled(true);
        filterRegistration.addUrlPatterns("/*"); 
        filterRegistration.setDispatcherTypes(DispatcherType.REQUEST);
        return filterRegistration;
	}
	
	/**
	 * 该类如果不设置为static，@Value注解就无效，原因未知
	 * @return
	 */
	@Bean
	public static LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
		return new LifecycleBeanPostProcessor();
	}
}
