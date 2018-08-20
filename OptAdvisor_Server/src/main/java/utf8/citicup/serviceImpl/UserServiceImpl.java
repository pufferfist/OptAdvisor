package utf8.citicup.serviceImpl;

import org.apache.shiro.crypto.hash.Sha256Hash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utf8.citicup.dataService.UserDataService;
import utf8.citicup.domain.entity.ResponseMsg;
import utf8.citicup.domain.entity.User;
import utf8.citicup.service.UserService;
import utf8.citicup.service.util.PolySms;
import utf8.citicup.service.util.StatusMsg;

import java.io.IOException;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDataService userDataService;
    private Logger logger = LoggerFactory.getLogger(UserService.class);

    @Override
    public ResponseMsg signUp(User user) {
        user.setPassword(new Sha256Hash(user.getPassword()).toString());
        User resultOfFind = userDataService.findById(user.getUsername());
        if (null == resultOfFind) {
            userDataService.save(user);
            return StatusMsg.signUpSuccess;
        } else {
            return StatusMsg.usernameExists;
        }
    }

    @Override
    public ResponseMsg isUsernameUsed(String username) {
        User resultOfFind=userDataService.findById(username);
        return new ResponseMsg(0, "Find if username exists success", null != resultOfFind);
    }

    @Override
    public ResponseMsg sendVerifyCode(String username, String verifyCode) {
        User user = getUser(username);
        if (null == user) {
            return StatusMsg.unknownUsername;
        } else {
            try {
                Map<String, Object> map = PolySms.sendSms(user.getTelephone(), verifyCode);
                if (map.get("error_code").toString().equals("0"))
                    return StatusMsg.sendVerifyCodeSuccess;
                else
                    return StatusMsg.sendVerifyCodeFail;
            } catch (IOException e) {
                logger.warn("Send verify code occurs IOException");
                return StatusMsg.sendVerifyCodeException;
            }
        }
    }

    @Override
    public ResponseMsg checkVerifyCode(Object username, Object srcVerifyCode, String verifyCode, String newPassword) {
        newPassword = new Sha256Hash(newPassword).toString();
        if (null == srcVerifyCode) {
            return StatusMsg.neverSendCode;
        } else if (srcVerifyCode.equals(verifyCode)) {
            if (userDataService.updatePassword(username.toString(), newPassword))
                return StatusMsg.checkCodeAndSetPasswordSuccess;
            else
                return StatusMsg.unknownUsername;
        } else {
            return StatusMsg.checkVerifyCodeFail;
        }
//        Session session = SecurityUtils.getSubject().getSession();
//        Object srcVerifyCode = session.getAttribute("verifyCode");
//        Object username = session.getAttribute("username");
//        if (null == srcVerifyCode) {
//            return StatusMsg.neverSendCode;
//        } else if (srcVerifyCode.equals(verifyCode)) {
//            session.removeAttribute("verifyCode");
//            session.removeAttribute("username");
//            if (userDataService.updatePassword(username.toString(), newPassword))
//                return StatusMsg.checkCodeAndSetPasswordSuccess;
//            else
//                return StatusMsg.unknownUsername;
//        } else {
//            return StatusMsg.checkVerifyCodeFail;
//        }
    }

    public ResponseMsg resetPassword(String username, String oldPassword, String newPassword) {
        oldPassword = new Sha256Hash(oldPassword).toString();
        newPassword = new Sha256Hash(newPassword).toString();
        User user = userDataService.findById(username);
        if (null == user) {
            return StatusMsg.unknownUsername;
        } else if (!user.getPassword().equals(oldPassword)) {
            return StatusMsg.incorrectPassword;
        } else if (userDataService.updatePassword(username, newPassword)) {
            return StatusMsg.updatePasswordSuccess;
        } else {
            return StatusMsg.unknownUsername;
        }
    }

    @Override
    public ResponseMsg modifyInfo(String currentUsername, User user) {
        if (!currentUsername.equals(user.getUsername()))
            return StatusMsg.usernameNotMatchSession;
        User srcUser = getUser(currentUsername);
        user.setW1(srcUser.getW1());
        user.setW2(srcUser.getW2());
        user.setPassword(srcUser.getPassword());
        if (userDataService.updateUser(user))
            return StatusMsg.modifyInfoSuccess;
        else
            return StatusMsg.unknownUsername;
    }

    @Override
    public ResponseMsg getInfo(String username) {
        return new ResponseMsg(0, "Get user info success", this.getUser(username));
    }

    @Override
    public ResponseMsg deleteUser(String username) {
        userDataService.delete(username);
        return StatusMsg.deleteUserSuccess;
    }

    // method below is not created for controller

    @Override
    public User getUser(String username) {
        return userDataService.findById(username);
    }

}
