package utf8.citicup.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import utf8.citicup.dataService.UserDataService;
import utf8.citicup.domain.entity.User;

import java.io.Serializable;

@Component
public class CustomRealm extends AuthorizingRealm {

    @Autowired
    private UserDataService userDataService;
    private Logger logger = LoggerFactory.getLogger(CustomRealm.class);

    private Cache<String, Serializable> cache;
    private SessionManager sessionManager;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        logger.info("-----Authorization-----");
        String username = (String) principals.getPrimaryPrincipal();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        String role = (null == userDataService.findById(username)) ? "anon" : "user";
        info.addRole(role);
        return info;
    }

    private Subject getSubjectBySessionId(Serializable sessionId) {
        return new Subject.Builder(new DefaultSecurityManager()).sessionId(sessionId).buildSubject();
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        logger.info("Doing authentication info");
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        User user = userDataService.findById(token.getUsername());
        if (null == user) {
            throw new UnknownAccountException("Unknown account.");
        } else if (null == user.getPassword() || !user.getPassword().equals(new String((char[]) token.getCredentials()))) {
            throw new IncorrectCredentialsException("Incorrect password.");
        }

        Serializable currentSessionId = SecurityUtils.getSubject().getSession().getId();
        Serializable cacheSessionId = cache.get(token.getUsername());
        if (null != cacheSessionId && cacheSessionId != currentSessionId) {
            Subject cacheSubject = this.getSubjectBySessionId(cacheSessionId);
            cacheSubject.logout();
        }
        cache.put(token.getUsername(), currentSessionId);

        return new SimpleAuthenticationInfo(token.getPrincipal(), user.getPassword(), getName());
    }

    void setSessionManager(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public void setCacheManager(CacheManager cacheManager) {
        this.cache = cacheManager.getCache("shiro_login");
    }
}
