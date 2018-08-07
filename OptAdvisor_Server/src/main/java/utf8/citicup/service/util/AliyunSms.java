package utf8.citicup.service.util;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;

public class AliyunSms {
    private static final String domain = "dysmsapi.aliyuncs.com";

    private static final String accessKeyId = "LTAIJOaRFuB6O1Jf";
    private static final String accessKeySecret = "TfcjHAqKGqmBSUNM6nP4MF7ZQZhYui";

    private final IAcsClient acsClient;

    public AliyunSms() {
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        String product = "Dysmsapi";

        try {
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        } catch (ClientException e) {
            e.printStackTrace();
        }
        acsClient = new DefaultAcsClient(profile);
    }


    public SendSmsResponse sendSms(String phoneNumber, String verifyCode) throws ClientException {
        SendSmsRequest request = new SendSmsRequest();
        request.setPhoneNumbers(phoneNumber);
        request.setSignName("智能数据决策工作室");
        request.setTemplateCode("SMS_134327463");
        request.setTemplateParam("{\"code\":\"" + verifyCode + "\"}");
        System.out.println(request.getPhoneNumbers());
        return acsClient.getAcsResponse(request);
    }
}
