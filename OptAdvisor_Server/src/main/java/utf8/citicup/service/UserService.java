package utf8.citicup.service;

import utf8.citicup.domain.entity.ResponseMsg;
import utf8.citicup.domain.entity.User;


public interface UserService {
    ResponseMsg login(String username, String password);

    ResponseMsg logout(String username);

    ResponseMsg signUp(String username, String password, String name, String birthday, String telephone,
                       String email, String gender, String avatar, int w1, int w2);

    ResponseMsg sendVerifyCode(String phoneNumber);

    ResponseMsg checkVerifyCode(String verifyCode);

    ResponseMsg resetPassword(String username , String oldUsername, String newPassword);

    ResponseMsg modifyInfo(User user);

    User getInfo(String username);

    String getRole(String username);

    String getPassword(String username);

}
