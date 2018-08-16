package utf8.citicup.restService;

import utf8.citicup.domain.entity.User;
import utf8.citicup.restService.exception.RestInvalidRequestException;

public interface RestUserService {
    void signUp(String username, String password) throws RestInvalidRequestException;

    Boolean isUsernameExists(String username);

    void sendVerifyCode(String username, String verifyCode);

    void checkVerifyCode(String username, String inputCode, String verifyCode, String newPassword);

    void resetPassword(String username, String oldPassword, String newPassword);

    void modifyInfo(String username, User user);

    User getInfo(String username);

    void deleteUser(String username);
}
