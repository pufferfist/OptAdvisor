package utf8.citicup.restService.impl;

import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utf8.citicup.dataService.UserDataService;
import utf8.citicup.domain.entity.User;
import utf8.citicup.restService.RestUserService;
import utf8.citicup.restService.exception.RestIntervalServerError;
import utf8.citicup.restService.exception.RestInvalidRequestException;
import utf8.citicup.restService.exception.RestUnauthorizedException;
import utf8.citicup.service.util.PolySms;

import java.io.IOException;
import java.util.Map;

@Service
public class RestUserServiceImpl implements RestUserService {

    @Autowired
    private UserDataService dataService;

    @Override
    public void signUp(User user) throws RestInvalidRequestException {
        user.setPassword(new Sha256Hash(user.getPassword()).toString());
        User resultOfFind = dataService.findById(user.getUsername());
        if (null == resultOfFind)
            dataService.save(user);
        else throw new RestInvalidRequestException("用户名已存在");
    }

    @Override
    public Boolean isUsernameExists(String username) {
        return null != dataService.findById(username);
    }

    @Override
    public void sendVerifyCode(String username, String verifyCode) throws RestUnauthorizedException, RestIntervalServerError {
        User user = this.getInfo(username);
        if (null == user)
            throw new RestUnauthorizedException("未知用户名");
        try {
            Map<String, Object> map = PolySms.sendSms(user.getTelephone(), verifyCode);
            if (map.get("error_code").toString().equals("0"))
                return;
        } catch (IOException ignored) {
        }
        throw new RestIntervalServerError("发送验证码失败");
    }

    @Override
    public void checkVerifyCode(String username, String inputCode, String verifyCode, String newPassword) throws RestUnauthorizedException, RestInvalidRequestException {
        newPassword = new Sha256Hash(newPassword).toString();
        if (!inputCode.equals(verifyCode))
            throw new RestUnauthorizedException("验证码错误");
        if (null == dataService.findById(username))
            throw new RestUnauthorizedException("未知用户名");
        dataService.updatePassword(username, newPassword);
    }

    @Override
    public void resetPassword(String username, String oldPassword, String newPassword) throws RestUnauthorizedException {
        oldPassword = new Sha256Hash(oldPassword).toString();
        newPassword = new Sha256Hash(newPassword).toString();
        User user = dataService.findById(username);
        if (null == user)
            throw new RestUnauthorizedException("未知用户名");
        if (!user.getPassword().equals(oldPassword))
            throw new RestUnauthorizedException("密码错误");
        dataService.updatePassword(username, newPassword);
    }

    @Override
    public void modifyInfo(String username, User user) throws RestUnauthorizedException {
        if(!username.equals(user.getUsername()))
            throw new RestUnauthorizedException("用户名与Session 不匹配");
        User srcUser = this.getInfo(username);
        user.setPassword(srcUser.getPassword());
        user.setW1(srcUser.getW1());
        user.setW2(srcUser.getW2());
        user.setPassword(srcUser.getPassword());
        if (!dataService.updateUser(user))
            throw new RestUnauthorizedException("未知用户名");
    }

    @Override
    public User getInfo(String username) {
        return dataService.findById(username);
    }

    @Override
    public void deleteUser(String username) {
        dataService.delete(username);
    }
}
