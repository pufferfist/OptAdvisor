package utf8.citicup.restController;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import utf8.citicup.domain.entity.User;
import utf8.citicup.restService.RestUserService;
import utf8.citicup.restService.exception.RestIntervalServerError;
import utf8.citicup.restService.exception.RestInvalidRequestException;
import utf8.citicup.restService.exception.RestUnauthorizedException;

import java.util.Map;

@Controller
@RequestMapping("api/")
@ResponseStatus(HttpStatus.OK)
public class RestUserController {
    @Autowired
    private RestUserService service;

    @PostMapping("user")
    public void signUp(@RequestBody User user) throws RestInvalidRequestException {
        service.signUp(user);
    }

    @GetMapping("user")
    public User getInfo() {
        String username = SecurityUtils.getSubject().getPrincipal().toString();
        return service.getInfo(username);
    }

    @PutMapping("user")
    public void modifyInfo(@RequestBody User user) throws RestUnauthorizedException {
        String username = SecurityUtils.getSubject().getPrincipal().toString();
        service.modifyInfo(username, user);
    }

    @DeleteMapping("user/{username}")
    public void deleteInfo(@PathVariable String username) {
        service.deleteUser(username);
    }

    @GetMapping("user/username")
    public Boolean isUsernameExists(String username) {
        return service.isUsernameExists(username);
    }

    @PutMapping("user/password")
    public void resetPassword(@RequestBody Map<String, String> params) throws RestInvalidRequestException, RestUnauthorizedException {
        String username = SecurityUtils.getSubject().getPrincipal().toString();
        if (!params.containsKey("oldPassword") || !params.containsKey("newPassword"))
            throw new RestInvalidRequestException("请按照文档传入指定参数");
        service.resetPassword(username, params.get("oldPassword"), params.get("newPassword"));
    }

    @PostMapping("session")
    public void login(@RequestBody Map<String, String> params) throws RestInvalidRequestException, RestUnauthorizedException {
        if (!params.containsKey("username") || !params.containsKey("password"))
            throw new RestInvalidRequestException("请按照文档传入指定参数");
        String password = new Sha256Hash(params.get("password")).toString();
        String username = params.get("username");
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        token.setRememberMe(true);
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(token);
        } catch (UnknownAccountException uae) {
            throw new RestUnauthorizedException("用户名错误");
        } catch (IncorrectCredentialsException ice) {
            throw new RestUnauthorizedException("密码错误");
        }
        if (!subject.isAuthenticated()) {
            token.clear();
            throw new RestUnauthorizedException("登陆失败");
        }
    }

    @GetMapping("session")
    public void auth() throws RestUnauthorizedException {
        if (!SecurityUtils.getSubject().isAuthenticated())
            throw new RestUnauthorizedException("未登录");
    }

    @DeleteMapping("session")
    public void logout() {
        SecurityUtils.getSubject().logout();
    }

    @PostMapping("code")
    public void sendVerifyCode(@RequestBody Map<String, String> params) throws RestUnauthorizedException, RestIntervalServerError, RestInvalidRequestException {
        if (!params.containsKey("username"))
            throw new RestInvalidRequestException("未输入用户名");
        String username = params.get("username");
        String verifyCode = String.format("%04d", (int) (Math.random() * 9999));
        service.sendVerifyCode(username, verifyCode);

        Session session = SecurityUtils.getSubject().getSession();
        session.setAttribute("username", username);
        session.setAttribute("verifyCode", verifyCode);
    }

    @PutMapping("code/{code}")
    public void checkVerifyCode(@PathVariable String code, @RequestBody Map<String, String> params) throws RestInvalidRequestException, RestUnauthorizedException {
        if (!params.containsKey("password"))
            throw new RestInvalidRequestException("未传入新的密码");
        Session session = SecurityUtils.getSubject().getSession();
        if (null == session.getAttribute("username") || null == session.getAttribute("verifyCode"))
            throw new RestUnauthorizedException("未向该用户发送验证码");
        service.checkVerifyCode(session.getAttribute("username").toString(), code,
                session.getAttribute("verifyCode").toString(), params.get("password"));
    }
}
