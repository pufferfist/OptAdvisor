package utf8.citicup.service;

import utf8.citicup.domain.entity.ResponseMsg;
import utf8.citicup.domain.entity.User;

public interface UserService {

    ResponseMsg signUp(User user);

    boolean isUsernameUsed(String username);

    ResponseMsg sendVerifyCode(String username, String verifyCode);

    ResponseMsg checkVerifyCode(Object username, Object srcVerifyCode, String verifyCode, String newPassword);

    ResponseMsg resetPassword(String username, String oldUsername, String newPassword);

    ResponseMsg modifyInfo(String currentUsername, User user);

    ResponseMsg getInfo(String username);

    ResponseMsg deleteUser(String username);

    User getUser(String username);

}
