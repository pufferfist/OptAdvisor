package utf8.citicup.service.util;

import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PolySms {
    private static final String DEF_CHARSET = "UTF-8";
    private static final int DEF_CONN_TIMEOUT = 30000;
    private static final int DEF_READ_TIMEOUT = 30000;
    //配置您申请的KEY
    private static final String modelId = "92682";
    private static final String APP_KEY = "369ec5c8ef83bdfde7190381c0898fd3";

    //2.发送短信
    public static Map<String, Object> sendSms(String phoneNumber, String verifyCode) throws IOException {
        String result;
        String url = "http://v.juhe.cn/sms/send";//请求接口地址
        Map<String, Object> params = new HashMap<>();//请求参数
        params.put("mobile", phoneNumber);//接收短信的手机号码
        params.put("tpl_id", modelId);//短信模板ID，请参考个人中心短信模板设置
        params.put("tpl_value", "#code#=" + verifyCode + "&#m#=5");//变量名和变量值对。如果你的变量名或者变量值中带有#&=中的任意一个特殊符号，请先分别进行urlencode编码后再传递，<a href="http://www.juhe.cn/news/index/id/50" target="_blank">详细说明></a>
        params.put("key", APP_KEY);//应用App (应用详细页查询)
        params.put("dtype", "");//返回数据的格式,xml或json，默认json
        result = net(url, params);

//        Gson gson = new Gson();
//        Map<String, Object> map = new HashMap<>();
//        return (Map<String, Object>) gson.fromJson(result, map.getClass());
        return new JSONObject(result).toMap();
    }

    /**
     * @param strUrl 请求地址
     * @param params 请求参数
     * @return 网络请求字符串
     */
    private static String net(String strUrl, Map<String, Object> params) throws IOException {
        HttpURLConnection conn = null;
        BufferedReader reader = null;
        String rs = null;
        try {
            StringBuilder sb = new StringBuilder();
            strUrl = strUrl + "?" + urlencode(params);
            URL url = new URL(strUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            String userAgent = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.66 Safari/537.36";
            conn.setRequestProperty("User-agent", userAgent);
            conn.setUseCaches(false);
            conn.setConnectTimeout(DEF_CONN_TIMEOUT);
            conn.setReadTimeout(DEF_READ_TIMEOUT);
            conn.setInstanceFollowRedirects(false);
            conn.connect();
            if (Objects.equals("GET", "POST")) {
                try {
                    DataOutputStream out = new DataOutputStream(conn.getOutputStream());
                    out.writeBytes(urlencode(params));
                } catch (Exception e) {
                    e.printStackTrace();
                    // TODO: handle exception
                }
            }
            InputStream is = conn.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, DEF_CHARSET));
            String strRead;
            while ((strRead = reader.readLine()) != null) {
                sb.append(strRead);
            }
            rs = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
        return rs;
    }

    //将map型转为请求参数型
    private static String urlencode(Map<String, Object> data) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Object> i : data.entrySet()) {
            try {
                sb.append(i.getKey()).append("=").append(URLEncoder.encode(i.getValue() + "", "UTF-8")).append("&");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}