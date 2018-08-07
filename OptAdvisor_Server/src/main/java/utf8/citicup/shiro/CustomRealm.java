package utf8.citicup.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utf8.citicup.service.UserService;
import utf8.citicup.serviceImpl.UserServiceImpl;

import java.util.HashSet;
import java.util.Set;

public class CustomRealm extends AuthorizingRealm {

    private UserService userService = new UserServiceImpl();
    private Logger logger = LoggerFactory.getLogger(CustomRealm.class);

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        logger.info("-----Authorization-----");
        String username = (String) SecurityUtils.getSubject().getPrincipal();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

        String role = userService.getRole(username);
        Set<String> set = new HashSet<>();
        set.add(role);
        info.setRoles(set);
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        String password = userService.getPassword(token.getUsername());
        if(null == password) {
            throw new UnknownAccountException("Unknown account.");
        } else if(!password.equals(new String((char[])token.getCredentials()))) {
            throw new IncorrectCredentialsException("Incorrect password.");
        }
        return new SimpleAuthenticationInfo(token.getPrincipal(), password, getName());
    }
}
