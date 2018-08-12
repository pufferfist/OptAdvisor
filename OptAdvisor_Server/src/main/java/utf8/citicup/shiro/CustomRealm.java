package utf8.citicup.shiro;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import utf8.citicup.dataService.UserDataService;
import utf8.citicup.domain.entity.User;

@Component
public class CustomRealm extends AuthorizingRealm {

    @Autowired
    private UserDataService userDataService;
    private Logger logger = LoggerFactory.getLogger(CustomRealm.class);

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        logger.info("-----Authorization-----");
        String username = (String) principals.getPrimaryPrincipal();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        String role = (null == userDataService.findById(username)) ? "anon" : "user";
        info.addRole(role);
        return info;
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
        return new SimpleAuthenticationInfo(token.getPrincipal(), user.getPassword(), getName());
    }

}
