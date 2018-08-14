package utf8.citicup.shiro;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.mgt.FilterChainManager;
import org.apache.shiro.web.filter.mgt.FilterChainResolver;
import org.apache.shiro.web.mgt.WebSecurityManager;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanInitializationException;

public class RestShiroFilterFactoryBean extends ShiroFilterFactoryBean {
    private Logger logger = LoggerFactory.getLogger(RestShiroFilterFactoryBean.class);

    public RestShiroFilterFactoryBean() {
        super();
    }

    @Override
    protected AbstractShiroFilter createInstance() throws Exception {
        logger.info("Creating shiro filter instance");
        SecurityManager securityManager = this.getSecurityManager();
        if (null == securityManager)
            throw new BeanInitializationException("Security manager property must be set.");
        if (!(securityManager instanceof WebSecurityManager))
            throw new BeanInitializationException("The security manager does not implement the WebSecurityManager interface.");

        FilterChainManager filterChainManager = this.createFilterChainManager();
        RestPathMatchingFilterChainResolver chainResolver = new RestPathMatchingFilterChainResolver();
        chainResolver.setFilterChainManager(filterChainManager);
        return new RestShiroFilterFactoryBean.SpringShiroFilter((WebSecurityManager) securityManager, chainResolver);
    }

    private static final class SpringShiroFilter extends AbstractShiroFilter {
        SpringShiroFilter(WebSecurityManager webSecurityManager, FilterChainResolver resolver) {
            super();
            if (webSecurityManager == null) {
                throw new IllegalArgumentException("WebSecurityManager property cannot be null.");
            }
            setSecurityManager(webSecurityManager);
            if (resolver != null) {
                setFilterChainResolver(resolver);
            }
        }
    }
}
