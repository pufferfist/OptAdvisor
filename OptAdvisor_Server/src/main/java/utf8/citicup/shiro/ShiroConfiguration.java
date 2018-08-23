package utf8.citicup.shiro;

import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.mgt.RememberMeManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.subject.SubjectContext;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.ServletContainerSessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfiguration {

    private Logger logger = LoggerFactory.getLogger(ShiroConfiguration.class);

    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new RestShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        shiroFilterFactoryBean.setLoginUrl("/auth");  //若用户无权限,将跳转到该路径
        shiroFilterFactoryBean.setUnauthorizedUrl("/login");

        //拦截器设置
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        filterChainDefinitionMap.put("/admin/**", "admin");

        filterChainDefinitionMap.put("/user/private/**", "anon");
        filterChainDefinitionMap.put("/message/private/**", "anon");

        filterChainDefinitionMap.put("/user/username/**", "anon");
        filterChainDefinitionMap.put("/user--POST", "anon");

        filterChainDefinitionMap.put("/user/**", "user");
        filterChainDefinitionMap.put("/recommend/**", "user");
        filterChainDefinitionMap.put("/portfolio/**", "user");
        filterChainDefinitionMap.put("/message/**", "user");

        filterChainDefinitionMap.put("/", "anon");
        //TODO

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

        Map<String, Filter> filtersMap = new HashMap<>();
        filtersMap.put("user", customAccessControlFilter());
        filtersMap.put("admin", customAccessControlFilter());
        shiroFilterFactoryBean.setFilters(filtersMap);
        logger.info("Shiro Configuration filter initializing");

        return shiroFilterFactoryBean;
    }

    private SessionManager sessionManager() {
        return new ServletContainerSessionManager();
    }

    private CacheManager cacheManager() {
        return new MemoryConstrainedCacheManager();
    }

    private CustomAccessControlFilter customAccessControlFilter(){
        return new CustomAccessControlFilter();
    }

    @Bean
    public RememberMeManager rememberMeManager() {
        CookieRememberMeManager rememberMeManager = new CookieRememberMeManager();
        rememberMeManager.setSerializer(new SimplePrincipalSerializer());
        SimpleCookie simpleCookie = new SimpleCookie();
        simpleCookie.setName("rememberMe");
        simpleCookie.setHttpOnly(true);
        rememberMeManager.setCookie(simpleCookie);
        return rememberMeManager;
    }

    @Bean
    public CustomRealm customRealm() {
        CustomRealm customRealm = new CustomRealm();
        customRealm.setCacheManager(cacheManager());
        customRealm.setSessionManager(sessionManager());
        return customRealm;
    }

    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager() {
            protected SubjectContext resolveSession(SubjectContext context) {
                if (context.resolveSession() == null) {
                    try {
                        Session session = resolveContextSession(context);
                        if (null != session)
                            context.setSession(session);
                    } catch (InvalidSessionException ignore) {
                    }
                }
                return context;
            }
        };
        securityManager.setCacheManager(cacheManager());
        securityManager.setSessionManager(sessionManager());
        securityManager.setRealm(customRealm());
        securityManager.setRememberMeManager(rememberMeManager());
        logger.info("Shiro security managing");
        return securityManager;
    }
}
