package utf8.citicup.serviceImpl;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utf8.citicup.dataService.UserDataService;
import utf8.citicup.domain.entity.ResponseMsg;
import utf8.citicup.domain.entity.User;
import utf8.citicup.service.UserService;
import utf8.citicup.service.statusMsg.UserStatusMsg;
import utf8.citicup.service.util.PolySms;

import java.io.IOException;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDataService userDataService;
    private Logger logger = LoggerFactory.getLogger(UserService.class);

    @Override
    public ResponseMsg login(String username, String password) {
        logger.info(username + ": " + password);
        password = new Sha256Hash(password).toString();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        token.setRememberMe(true);
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(token);
        } catch (UnknownAccountException uae) {
            return UserStatusMsg.unknownUsername;
        } catch (IncorrectCredentialsException ice) {
            return UserStatusMsg.incorrectPassword;
        }
        if (!subject.isAuthenticated()) {
            token.clear();
            return UserStatusMsg.notAuthenticated;
        }
        return UserStatusMsg.loginSuccess;
    }

    @Override
    public ResponseMsg logout() {
        SecurityUtils.getSubject().logout();
        return UserStatusMsg.logoutSuccess;
    }

    @Override
    public ResponseMsg signUp(User user) {
        user.setPassword(new Sha256Hash(user.getPassword()).toString());
        if (userDataService.addUser(user)) {
            return UserStatusMsg.signUpSuccess;
        } else {
            return UserStatusMsg.usernameExists;
        }
    }

    @Override
    public ResponseMsg sendVerifyCode(String username) {
        User user = getUser(username);
        Session session = SecurityUtils.getSubject().getSession();
        String randomNumber = Integer.toString((int) (Math.random() * 9999));
        if (null == user) {
            return UserStatusMsg.unknownUsername;
        } else {
            try {
                Map<String, Object> result = PolySms.sendSms(user.getTelephone(), randomNumber);
                if (result.get("error_code").toString().equals("0")) {
                    session.setAttribute("verifyCode", randomNumber);
                    session.setAttribute("username", username);
                    return UserStatusMsg.sendVerifyCodeSuccess;
                } else {
                    return UserStatusMsg.sendVerifyCodeFail;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return UserStatusMsg.sendVerifyCodeException;
            }
        }
    }

    @Override
    public ResponseMsg checkVerifyCode(String verifyCode, String newPassword) {
        Session session = SecurityUtils.getSubject().getSession();
        Object srcVerifyCode = session.getAttribute("verifyCode");
        Object username = session.getAttribute("username");
        if (null == srcVerifyCode) {
            return UserStatusMsg.neverSendCode;
        } else if (srcVerifyCode.equals(verifyCode)) {
            session.removeAttribute("verifyCode");
            session.removeAttribute("username");
            if (userDataService.updatePassword(username.toString(), newPassword))
                return UserStatusMsg.checkCodeAndSetPasswordSuccess;
            else
                return UserStatusMsg.unknownUsername;
        } else {
            return UserStatusMsg.checkVerifyCodeFail;
        }
    }

    public ResponseMsg resetPassword(String oldPassword, String newPassword) {
        String username = SecurityUtils.getSubject().getPrincipal().toString();
        oldPassword = new Sha256Hash(oldPassword).toString();
        newPassword = new Sha256Hash(newPassword).toString();
        User user = userDataService.findById(username);
        if (null == user) {
            return UserStatusMsg.unknownUsername;
        } else if (!user.getPassword().equals(oldPassword)) {
            return UserStatusMsg.incorrectPassword;
        } else if (userDataService.updatePassword(username, newPassword)) {
            return UserStatusMsg.updatePasswordSuccess;
        } else {
            return UserStatusMsg.unknownUsername;
        }
    }

    @Override
    public ResponseMsg modifyInfo(User user) {
        user.setPassword(new Sha256Hash(user.getPassword()).toString());
        String currentUsername = SecurityUtils.getSubject().getPrincipal().toString();
        if (!currentUsername.equals(user.getUsername()))
            return UserStatusMsg.usernameNotMatchSession;
        else if (userDataService.updateUser(user))
            return UserStatusMsg.modifyInfoSuccess;
        else
            return UserStatusMsg.unknownUsername;
    }

    @Override
    public ResponseMsg getInfo() {
        String username = SecurityUtils.getSubject().getPrincipal().toString();
        return new ResponseMsg(0, "Get user info success", getUser(username));
    }

    // method below is not created for controller

    @Override
    public User getUser(String username) {
        return userDataService.findById(username);
    }

    @Override
    public String getRole(String username) {
        if (null == getUser(username))
            return "anon";
        else
            return "user";
    }

}
