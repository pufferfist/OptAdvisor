package utf8.citicup.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import utf8.citicup.domain.entity.ResponseMsg;
import utf8.citicup.domain.entity.User;
import utf8.citicup.service.UserService;

import java.util.Map;

@RestController
public class UserController {

    @Autowired
    private UserService userService;
    private Logger logger = LoggerFactory.getLogger(UserController.class);


    @PostMapping("login")
    public ResponseMsg login(@RequestBody Map<String, Object> params) {
        return userService.login(params.get("username").toString(), params.get("password").toString());
    }

    @PostMapping("user/logout")
    public ResponseMsg logout(@RequestBody Map<String, Object> params) {
        return userService.logout(params.get("username").toString());
    }

    @PostMapping("signUp")
    public ResponseMsg signUp(@RequestBody User user) {
        return userService.signUp(user.getUsername(), user.getPassword(), user.getName(), user.getBirthday(),
                user.getTelephone(), user.getEmail(), user.getGender(), user.getAvatarPath(), user.getW1(),
                user.getW2());
    }

    @PostMapping("sendVerifyCode")
    public ResponseMsg sendVerifyCode(@RequestBody Map<String, Object> params) {
//        String phoneNumber = userService.getInfo(params.get("username").toString()).getTelephone();
        String phoneNumber = params.get("username").toString();
        return userService.sendVerifyCode(phoneNumber);
    }

    @PostMapping("checkVerifyCode")
    public ResponseMsg checkVerifyCode(@RequestBody Map<String, Object> params) {
        String verifyCode = params.get("verifyCode").toString();
        return userService.checkVerifyCode(verifyCode);
    }

    @PostMapping("user/resetPassword")
    public ResponseMsg resetPassword(String username, String oldPassword, String newPassword) {
        return userService.resetPassword(username, oldPassword, newPassword);
    }

    @PostMapping("user/modifyInfo")
    public ResponseMsg modifyInfo(User user) {
        return userService.modifyInfo(user);
    }

    @PostMapping("user/getInfo")
    public User getInfo(String username) {
        return userService.getInfo(username);
    }

    @RequestMapping("auth")
    public ResponseMsg auth() {
        return new ResponseMsg(1010, "need to login");
    }
}
