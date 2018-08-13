package utf8.citicup.shiro;

import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
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
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        shiroFilterFactoryBean.setLoginUrl("/auth");  //若用户无权限,将跳转到该路径
        shiroFilterFactoryBean.setUnauthorizedUrl("/login");

        //拦截器设置
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        filterChainDefinitionMap.put("/user/private/**", "anon");
        filterChainDefinitionMap.put("/message/private/**", "anon");

        filterChainDefinitionMap.put("/user/**", "user");
        filterChainDefinitionMap.put("/recommend/**", "user");
        filterChainDefinitionMap.put("/portfolio/**", "user");
        filterChainDefinitionMap.put("/message/**", "user");

        filterChainDefinitionMap.put("/", "anon");
        //TODO

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

        Map<String, Filter> filtersMap = new HashMap<>();
        filtersMap.put("user", kickSessionControlFilter());
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

    private CustomAccessControlFilter kickSessionControlFilter(){
        CustomAccessControlFilter kickSessionControlFilter = new CustomAccessControlFilter();
        //使用cacheManager获取相应的cache来缓存用户登录的会话；用于保存用户—会话之间的关系的；
        //这里我们还是用之前shiro使用的redisManager()实现的cacheManager()缓存管理
        //也可以重新另写一个，重新配置缓存时间之类的自定义缓存属性
        kickSessionControlFilter.setCacheManager(cacheManager());
        //用于根据会话ID，获取会话进行踢出操作的；
        kickSessionControlFilter.setSessionManager(sessionManager());
        return kickSessionControlFilter;
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
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setCacheManager(cacheManager());
        securityManager.setSessionManager(sessionManager());
        securityManager.setRealm(customRealm());
        logger.info("Shiro security managing");
        return securityManager;
    }
}
