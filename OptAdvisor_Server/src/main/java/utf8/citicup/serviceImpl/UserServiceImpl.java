package utf8.citicup.serviceImpl;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utf8.citicup.domain.entity.ResponseMsg;
import utf8.citicup.domain.entity.User;
import utf8.citicup.service.UserService;


public class UserServiceImpl implements UserService {
    private Logger logger = LoggerFactory.getLogger(UserService.class);

    @Override
    public ResponseMsg login(String username, String password) {
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        token.setRememberMe(true);
        logger.info("token: " + ReflectionToStringBuilder.toString(token));
        Subject subject = SecurityUtils.getSubject();
        try {
            logger.info("try to login");
            subject.login(token);
            logger.info("success.");
        } catch (UnknownAccountException uae) {
            return new ResponseMsg(1001, "Unknown account.");
        } catch (IncorrectCredentialsException ice) {
            return new ResponseMsg(1002, "Incorrect password.");
        }
        if (subject.isAuthenticated()) {
            logger.info("login success authenticated.");
        } else {
            token.clear();
        }
        return new ResponseMsg(1000, "login success.");
    }

    @Override
    public ResponseMsg logout(String username) {
        SecurityUtils.getSubject().logout();
        return new ResponseMsg(1011, "logout success.");
    }

    @Override
    public ResponseMsg signUp(String username, String password, String name, String birthday, String telephone,
                              String email, String gender, String avatar, int w1, int w2) {
        return null;
    }

    @Override
    public ResponseMsg forgetPassword(String username, String verifyCode, String newPassword) {
        return null;
    }

    @Override
    public ResponseMsg resetPassword(String username, String oldUsername, String newPassword) {
        return null;
    }

    @Override
    public ResponseMsg modifyInfo(User user) {
        return null;
    }

    @Override
    public User getInfo(String username) {
        return null;
    }

    @Override
    public String getRole(String username) {
        if (username.equals("suun")) {
            return "user";
        } else {
            return "anon";
        }
    }

    @Override
    public String getPassword(String username) {
        logger.info("username: " + username);
        if (username.equals("suun")) {
            return "suunPassword";
        } else {
            return null;
        }
    }
}
