package utf8.citicup.shiro;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

    private Logger logger = LoggerFactory.getLogger(ShiroConfig.class);

    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        shiroFilterFactoryBean.setLoginUrl("/auth");  //若用户无权限,将跳转到该路径
        shiroFilterFactoryBean.setUnauthorizedUrl("/login");

        //拦截器设置
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        filterChainDefinitionMap.put("/user/**", "user");
        filterChainDefinitionMap.put("/recommend/**", "user");
        filterChainDefinitionMap.put("/portfolio/**", "user");
        filterChainDefinitionMap.put("/", "anon");
        //TODO

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        logger.info("Shiro Configuration filter initializing");
        return shiroFilterFactoryBean;
    }

    @Bean
    public CustomRealm customRealm() {
        return new CustomRealm();
    }

    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(customRealm());
        logger.info("Shiro security managing");
        return securityManager;
    }

}
