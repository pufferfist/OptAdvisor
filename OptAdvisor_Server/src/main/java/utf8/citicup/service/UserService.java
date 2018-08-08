package utf8.citicup.service;

import utf8.citicup.domain.entity.ResponseMsg;
import utf8.citicup.domain.entity.User;

public interface UserService {
    ResponseMsg login(String username, String password);

    ResponseMsg logout();

    ResponseMsg signUp(User user);

    ResponseMsg sendVerifyCode(String phoneNumber);

    ResponseMsg checkVerifyCode(String verifyCode, String newPassword);

    ResponseMsg resetPassword(String oldUsername, String newPassword);

    ResponseMsg modifyInfo(User user);

    ResponseMsg getInfo();

    User getUser(String username);

    String getRole(String username);

}
