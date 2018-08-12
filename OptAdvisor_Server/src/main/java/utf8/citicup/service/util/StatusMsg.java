package utf8.citicup.service.util;

import utf8.citicup.domain.entity.ResponseMsg;

public class StatusMsg {
    public static ResponseMsg loginSuccess = new ResponseMsg(0, "Login success");
    public static ResponseMsg unknownUsername = new ResponseMsg(1001, "Unknown username");
    public static ResponseMsg incorrectPassword = new ResponseMsg(1002, "Incorrect password");
    public static ResponseMsg notAuthenticated = new ResponseMsg(1003, "Not authenticated");

    public static ResponseMsg logoutSuccess = new ResponseMsg(0, "Logout success");

    public static ResponseMsg signUpSuccess = new ResponseMsg(0, "Sign up success");
    public static ResponseMsg usernameExists = new ResponseMsg(1004, "Username exists");

    public static ResponseMsg sendVerifyCodeSuccess = new ResponseMsg(0, "Send verify code success");
    public static ResponseMsg sendVerifyCodeFail = new ResponseMsg(1011, "Send verify code fail");
    public static ResponseMsg sendVerifyCodeException = new ResponseMsg(1012, "Send verify code occurs IOException");

    public static ResponseMsg neverSendCode = new ResponseMsg(1013, "Never send verify code or verify code has expired");
    public static ResponseMsg checkCodeAndSetPasswordSuccess = new ResponseMsg(0, "Check verify code and set new password success");
    public static ResponseMsg checkVerifyCodeFail = new ResponseMsg(1014, "Wrong verify code");

    public static ResponseMsg updatePasswordSuccess = new ResponseMsg(1005, "Update new password success");
    public static ResponseMsg modifyInfoSuccess = new ResponseMsg(1006, "Modify info success");
    public static ResponseMsg usernameNotMatchSession = new ResponseMsg(1007, "Username does not match current session");

    public static ResponseMsg needToLogin = new ResponseMsg(1008, "Need to login first");


    public static ResponseMsg setMessageReadSuccess = new ResponseMsg(0, "Set message read success");
    public static ResponseMsg MessageNotMatchUser = new ResponseMsg(4001, "Message does not match the user");

    public static ResponseMsg putMessageSuccess = new ResponseMsg(0, "Put message success");

}
