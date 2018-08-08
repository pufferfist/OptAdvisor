package utf8.citicup.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import utf8.citicup.domain.entity.ResponseMsg;
import utf8.citicup.domain.entity.User;
import utf8.citicup.service.UserService;
import utf8.citicup.service.statusMsg.UserStatusMsg;

import java.util.Map;

@RestController
public class UserController {

    @Autowired
    private UserService userService;


    @PostMapping("login")
    public ResponseMsg login(@RequestBody Map<String, Object> params) {
        return userService.login(params.get("username").toString(), params.get("password").toString());
    }

    @PostMapping("user/logout")
    public ResponseMsg logout() {
        return userService.logout();
    }

    @PostMapping("signUp")
    public ResponseMsg signUp(@RequestBody User user) {
        return userService.signUp(user);
    }

    @PostMapping("sendVerifyCode")
    public ResponseMsg sendVerifyCode(@RequestBody Map<String, Object> params) {
        return userService.sendVerifyCode(params.get("username").toString());
    }

    @PostMapping("checkVerifyCode")
    public ResponseMsg checkVerifyCode(@RequestBody Map<String, Object> params) {
        return userService.checkVerifyCode(params.get("verifyCode").toString(), params.get("newPassword").toString());
    }

    @PostMapping("user/resetPassword")
    public ResponseMsg resetPassword(@RequestBody Map<String, Object> params) {
        return userService.resetPassword(params.get("oldPassword").toString(), params.get("newPassword").toString());
    }

    @PostMapping("user/modifyInfo")
    public ResponseMsg modifyInfo(@RequestBody User user) {
        return userService.modifyInfo(user);
    }

    @PostMapping("user/getInfo")
    public ResponseMsg getInfo() {
        return userService.getInfo();
    }

    @RequestMapping("auth")
    public ResponseMsg auth() {
        return UserStatusMsg.needToLogin;
    }
}
