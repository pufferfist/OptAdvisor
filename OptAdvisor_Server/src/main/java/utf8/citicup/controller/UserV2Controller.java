package utf8.citicup.controller;

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
import org.springframework.web.bind.annotation.*;
import utf8.citicup.domain.entity.ResponseMsg;
import utf8.citicup.domain.entity.User;
import utf8.citicup.service.UserService;
import utf8.citicup.service.util.StatusMsg;

import java.util.Map;

@RestController
public class UserV2Controller {
    @Autowired
    private UserService userService;

    private Logger logger = LoggerFactory.getLogger(UserController.class);


    @PostMapping("session")
    public ResponseMsg login(@RequestBody Map<String, Object> params) {
        return commonLogin(params, logger);
    }

    static ResponseMsg commonLogin(@RequestBody Map<String, Object> params, Logger logger) {
        String username = params.get("username").toString();
        String password = params.get("password").toString();
        logger.info(username + ": " + password);
        password = new Sha256Hash(password).toString();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        token.setRememberMe(true);
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(token);
        } catch (UnknownAccountException uae) {
            return StatusMsg.unknownUsername;
        } catch (IncorrectCredentialsException ice) {
            return StatusMsg.incorrectPassword;
        }
        if (!subject.isAuthenticated()) {
            token.clear();
            return StatusMsg.notAuthenticated;
        }
        return StatusMsg.loginSuccess;
    }

    @DeleteMapping("session")
    public ResponseMsg logout() {
        SecurityUtils.getSubject().logout();
        return StatusMsg.logoutSuccess;
    }

    @GetMapping("code")
    public ResponseMsg sendVerifyCode(String username) {
        String verifyCode = String.format("%04d", (int) (Math.random() * 9999));
        ResponseMsg ret = userService.sendVerifyCode(username, verifyCode);
        if (StatusMsg.sendVerifyCodeSuccess == ret) {
            Session session = SecurityUtils.getSubject().getSession();
            session.setAttribute("username", username);
            session.setAttribute("verifyCode", verifyCode);
        }
        return ret;
    }

    @PostMapping("code")
    public ResponseMsg checkVerifyCode(@RequestBody Map<String, Object> params) {
        Session session = SecurityUtils.getSubject().getSession();
        ResponseMsg ret = userService.checkVerifyCode(session.getAttribute("username"), session.getAttribute("verifyCode"),
                params.get("code").toString(), params.get("password").toString());
        if (StatusMsg.checkCodeAndSetPasswordSuccess == ret || StatusMsg.unknownUsername == ret) {
            session.removeAttribute("username");
            session.removeAttribute("verifyCode");
        }
        return ret;
    }

    @PutMapping("user/password")
    public ResponseMsg resetPassword(@RequestBody Map<String, Object> params) {
        String username = SecurityUtils.getSubject().getPrincipal().toString();
        return userService.resetPassword(username, params.get("oldPassword").toString(), params.get("newPassword").toString());
    }

    @PostMapping("user")
    public ResponseMsg signUp(@RequestBody User user) {
        return userService.signUp(user);
    }

    @GetMapping("user/username")
    public ResponseMsg isUsernameUsed(String search){
        return userService.isUsernameUsed(search);
    }

    @PutMapping("user")
    public ResponseMsg modifyInfo(@RequestBody User user) {
        String currentUsername = SecurityUtils.getSubject().getPrincipal().toString();
        return userService.modifyInfo(currentUsername, user);
    }

    @GetMapping("user")
    public ResponseMsg getInfo() {
        String username = SecurityUtils.getSubject().getPrincipal().toString();
        return userService.getInfo(username);
    }

    @DeleteMapping("admin/user/{username}")
    public ResponseMsg deleteUser(@PathVariable String username) {
        return userService.deleteUser(username);
    }

    @GetMapping(value = "session")
    public ResponseMsg auth() {
        if (SecurityUtils.getSubject().isAuthenticated())
            return StatusMsg.isLoggedIn;
        else
            return StatusMsg.needToLogin;
    }
}
