package utf8.citicup.shiro;

import org.apache.shiro.web.filter.mgt.FilterChainManager;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class RestPathMatchingFilterChainResolver extends PathMatchingFilterChainResolver {
    private Logger logger = LoggerFactory.getLogger(RestPathMatchingFilterChainResolver.class);

    RestPathMatchingFilterChainResolver() {
        super();
    }

    public RestPathMatchingFilterChainResolver(FilterConfig filterConfig) {
        super(filterConfig);
    }

    @Override
    public FilterChain getChain(ServletRequest request, ServletResponse response, FilterChain originalChain) {
        FilterChainManager filterChainManager = getFilterChainManager();
        if (!filterChainManager.hasChains())
            return null;
        String requestUri = getPathWithinApplication(request);
        String[] urls = null;
        for (String pathPattern : filterChainManager.getChainNames()) {
            urls = pathPattern.split("--");
            if (urls.length == 2) {
                if (WebUtils.toHttp(request).getMethod().toUpperCase().equals(urls[1].toUpperCase())) {
                    pathPattern = urls[0];
                }
            }
            if (pathMatches(pathPattern, requestUri)) {
                if (logger.isTraceEnabled())
                    logger.trace("Matched path pattern [" + pathPattern + "] for requestUri [" +
                            requestUri + "]. Utilizing corresponding filter chain");
                if (urls.length == 2)
                    pathPattern = pathPattern.concat("--").concat(WebUtils.toHttp(request).getMethod().toUpperCase());
                return filterChainManager.proxy(originalChain, pathPattern);
            }
        }
//        return super.getChain(request, response, originalChain);
        return null;
    }
}
