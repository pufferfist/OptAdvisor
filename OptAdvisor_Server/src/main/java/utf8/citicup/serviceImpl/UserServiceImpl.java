package utf8.citicup.serviceImpl;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utf8.citicup.dataService.UserDataService;
import utf8.citicup.domain.entity.ResponseMsg;
import utf8.citicup.domain.entity.User;
import utf8.citicup.service.UserService;
import utf8.citicup.service.util.PolySms;

import java.io.IOException;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDataService userDataService;
//    private AliyunSms aliyunSms = new AliyunSms();
//    private Logger logger = LoggerFactory.getLogger(UserService.class);

    @Override
    public ResponseMsg login(String username, String password) {
        password = new Sha256Hash(password).toString();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        token.setRememberMe(true);
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(token);
        } catch (UnknownAccountException uae) {
            return new ResponseMsg(1001, "Unknown account");
        } catch (IncorrectCredentialsException ice) {
            return new ResponseMsg(1002, "Incorrect password");
        }
        if (!subject.isAuthenticated()) {
            token.clear();
            return new ResponseMsg(1003, "Subject not authenticated");
        }
        return new ResponseMsg(0, "login success.");
    }

    @Override
    public ResponseMsg logout(String username) {
        SecurityUtils.getSubject().logout();
        return new ResponseMsg(0, "logout success.");
    }

    @Override
    public ResponseMsg signUp(User user) {
        user.setPassword(new Sha256Hash(user.getPassword()).toString());
        if (userDataService.addUser(user)) {
            return new ResponseMsg(0, "Sign up success");
        } else {
            return new ResponseMsg(1004, "Username exists");
        }
    }

    @Override
    public ResponseMsg sendVerifyCode(String phoneNumber) {
        Session session = SecurityUtils.getSubject().getSession();
        String randomNumber = Integer.toString((int) (Math.random() * 9999));
        session.setAttribute("verifyCode", randomNumber);
        /*
        try {
        SendSmsResponse sendSmsResponse = aliyunSms.sendSms(phoneNumber, randomNumber);
        if (sendSmsResponse.getCode().equals("OK")) {
        return new ResponseMsg(0, "Send verify code success");
        } else {
        return new ResponseMsg(1013, sendSmsResponse.getMessage());
        }
        } catch (ClientException e) {
        e.printStackTrace();
        return new ResponseMsg(1013, "Aliyun sms client error");
        }
        */
        try {
            Map<String, Object> result = PolySms.sendSms(phoneNumber, randomNumber);
            System.out.println(result);
            if (result.get("error_code").toString().equals("0")) {
                return new ResponseMsg(0, "Send verify code success");
            } else {
                return new ResponseMsg(1003, result.get("reason").toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseMsg(1004, "Send verify code occurs IOException");
        }
    }

    @Override
    public ResponseMsg checkVerifyCode(String verifyCode) {
        Session session = SecurityUtils.getSubject().getSession();
        Object srcVerifyCode = session.getAttribute("verifyCode");
        if (null == srcVerifyCode) {
            return new ResponseMsg(1011, "Never send verify code");
        } else if (srcVerifyCode.equals(verifyCode)) {
            session.removeAttribute("verifyCode");
            return new ResponseMsg(0, "Check verify code success");
        } else {
            return new ResponseMsg(1012, "Wrong verify code");
        }
    }

    @Override
    public ResponseMsg resetPassword(String username, String oldPassword, String newPassword) {
        User user = userDataService.findById(username);
        if (null == user) {
            return new ResponseMsg(1005, "User does not exists");
        } else if (!user.getPassword().equals(oldPassword)) {
            return new ResponseMsg(1002, "Incorrect password");
        } else if (userDataService.updatePassword(username, newPassword)) {
            return new ResponseMsg(0, "Update new password success");
        } else {
            return new ResponseMsg(1005, "User does not exists");
        }
    }

    @Override
    public ResponseMsg modifyInfo(User user) {
        if (userDataService.updateUser(user))
            return new ResponseMsg(0, "Modify info success");
        else
            return new ResponseMsg(1005, "User does not exists");
    }

    @Override
    public User getInfo(String username) {
        return userDataService.findById(username);
    }

    @Override
    public String getRole(String username) {
        if (null == getInfo(username))
            return "anon";
        else
            return "user";
    }

}
