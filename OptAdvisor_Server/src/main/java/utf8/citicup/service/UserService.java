package utf8.citicup.service;

import utf8.citicup.domain.entity.ResponseMsg;
import utf8.citicup.domain.entity.User;

public interface UserService {
    ResponseMsg login(String username, String password);

    ResponseMsg logout();

    ResponseMsg signUp(User user);

    ResponseMsg sendVerifyCode(String username, String verifyCode);

    ResponseMsg checkVerifyCode(Object username, Object srcVerifyCode, String verifyCode, String newPassword);

    ResponseMsg resetPassword(String username, String oldUsername, String newPassword);

    ResponseMsg modifyInfo(String currentUsername, User user);

    ResponseMsg getInfo(String username);

    User getUser(String username);

    String getRole(String username);

}
