package utf8.citicup.restService.impl;

import org.springframework.stereotype.Service;
import utf8.citicup.domain.entity.User;
import utf8.citicup.restService.RestUserService;
import utf8.citicup.restService.exception.RestInvalidRequestException;

@Service
public class RestUserServiceImpl implements RestUserService {
    @Override
    public void signUp(String username, String password) throws RestInvalidRequestException {
        throw new RestInvalidRequestException("Username exists");
    }

    @Override
    public Boolean isUsernameExists(String username) {
        return null;
    }

    @Override
    public void sendVerifyCode(String username, String verifyCode) {
    }

    @Override
    public void checkVerifyCode(String username, String inputCode, String verifyCode, String newPassword) {

    }

    @Override
    public void resetPassword(String username, String oldPassword, String newPassword) {

    }

    @Override
    public void modifyInfo(String username, User user) {

    }

    @Override
    public User getInfo(String username) {
        return null;
    }

    @Override
    public void deleteUser(String username) {

    }
}
