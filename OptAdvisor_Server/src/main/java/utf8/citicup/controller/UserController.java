package utf8.citicup.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import utf8.citicup.domain.entity.ResponseMsg;
import utf8.citicup.domain.entity.User;
import utf8.citicup.service.UserService;
import utf8.citicup.service.util.StatusMsg;

import java.util.Map;

import static utf8.citicup.controller.UserV2Controller.commonLogin;

@RestController
@ResponseStatus(HttpStatus.OK)
public class UserController {

    @Autowired
    private UserService userService;

    private Logger logger = LoggerFactory.getLogger(UserController.class);


    @PostMapping("login")
    public ResponseMsg login(@RequestBody Map<String, Object> params) {
        return commonLogin(params, logger);
    }

    @PostMapping("user/logout")
    public ResponseMsg logout() {
        SecurityUtils.getSubject().logout();
        return StatusMsg.logoutSuccess;
    }

    @PostMapping("signUp")
    public ResponseMsg signUp(@RequestBody User user) {
        return userService.signUp(user);
    }

    @PostMapping("isUsernameUsed")
    public ResponseMsg isUsernameUsed(@RequestBody User user){
        return userService.isUsernameUsed(user.getUsername());
    }

    @PostMapping("sendVerifyCode")
    public ResponseMsg sendVerifyCode(@RequestBody Map<String, Object> params) {
        String username = params.get("username").toString();
        String verifyCode = Integer.toString((int) (Math.random() * 9999));
        ResponseMsg ret = userService.sendVerifyCode(username, verifyCode);
        if (StatusMsg.sendVerifyCodeSuccess == ret) {
            Session session = SecurityUtils.getSubject().getSession();
            session.setAttribute("username", username);
            session.setAttribute("verifyCode", verifyCode);
        }
        return ret;
    }

    @PostMapping("checkVerifyCode")
    public ResponseMsg checkVerifyCode(@RequestBody Map<String, Object> params) {
        Session session = SecurityUtils.getSubject().getSession();
        ResponseMsg ret = userService.checkVerifyCode(session.getAttribute("username"), session.getAttribute("verifyCode"),
                params.get("verifyCode").toString(), params.get("newPassword").toString());
        if (StatusMsg.checkCodeAndSetPasswordSuccess == ret || StatusMsg.unknownUsername == ret) {
            session.removeAttribute("username");
            session.removeAttribute("verifyCode");
        }
        return ret;
    }

    @PostMapping("user/resetPassword")
    public ResponseMsg resetPassword(@RequestBody Map<String, Object> params) {
        String username = SecurityUtils.getSubject().getPrincipal().toString();
        return userService.resetPassword(username, params.get("oldPassword").toString(), params.get("newPassword").toString());
    }

    @PostMapping("user/modifyInfo")
    public ResponseMsg modifyInfo(@RequestBody User user) {
        String currentUsername = SecurityUtils.getSubject().getPrincipal().toString();
        return userService.modifyInfo(currentUsername, user);
    }

    @PostMapping("user/getInfo")
    public ResponseMsg getInfo() {
        String username = SecurityUtils.getSubject().getPrincipal().toString();
        return userService.getInfo(username);
    }

    @PostMapping("user/private/deleteUser")
    public ResponseMsg deleteUser(@RequestBody Map<String, Object> params) {
        return userService.deleteUser(params.get("username").toString());
    }

    @RequestMapping(value = "auth")
    public ResponseMsg auth() {
        if (SecurityUtils.getSubject().isAuthenticated())
            return StatusMsg.isLoggedIn;
        else
            return StatusMsg.needToLogin;
    }
}
