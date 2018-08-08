package utf8.citicup.service;

import utf8.citicup.domain.entity.ResponseMsg;
import utf8.citicup.domain.entity.User;

public interface UserService {
    ResponseMsg login(String username, String password);

    ResponseMsg logout(String username);

    ResponseMsg signUp(User user);

    ResponseMsg sendVerifyCode(String phoneNumber);

    ResponseMsg checkVerifyCode(String verifyCode);

    ResponseMsg resetPassword(String username , String oldUsername, String newPassword);

    ResponseMsg modifyInfo(User user);

    User getInfo(String username);

    String getRole(String username);

}
