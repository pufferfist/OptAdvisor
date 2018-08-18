package utf8.citicup.restService;

import utf8.citicup.domain.entity.User;
import utf8.citicup.restService.exception.RestIntervalServerError;
import utf8.citicup.restService.exception.RestInvalidRequestException;
import utf8.citicup.restService.exception.RestUnauthorizedException;

public interface RestUserService {
    void signUp(User user) throws RestInvalidRequestException;

    Boolean isUsernameExists(String username);

    void sendVerifyCode(String username, String verifyCode) throws RestUnauthorizedException, RestIntervalServerError;

    void checkVerifyCode(String username, String inputCode, String verifyCode, String newPassword) throws RestUnauthorizedException, RestInvalidRequestException;

    void resetPassword(String username, String oldPassword, String newPassword) throws RestUnauthorizedException;

    void modifyInfo(String username, User user) throws RestUnauthorizedException;

    User getInfo(String username);

    void deleteUser(String username);
}
