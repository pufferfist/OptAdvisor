package utf8.citicup.shiro;

import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utf8.citicup.service.util.JsonParse;
import utf8.citicup.service.util.StatusMsg;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class myFormAuthenticationFilter extends FormAuthenticationFilter {

    private Logger logger = LoggerFactory.getLogger(myFormAuthenticationFilter.class);

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        logger.info("oooooooooooooo");
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().print(JsonParse.objectToJsonString(StatusMsg.needToLogin));
        return false;
    }
}
