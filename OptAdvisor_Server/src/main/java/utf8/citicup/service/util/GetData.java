package utf8.citicup.service.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetData {
    private final String USER_AGENT = "Mozilla/5.0";

    private Logger logger = LoggerFactory.getLogger(GetData.class);
    /*
    public static void main(String[] args) throws IOException {
        GetData http = new GetData();

//        for(int i=0; i<4; i++) data[i] = data[i].replace("-","");
//        String[] T = http.get_T();

//        spider http = new spider();             //到期日和剩余天数

//        String[] data1 = http.get_expireAndremainder("2018-08");
//        for(int i=0; i<2; i++) System.out.println(data1[i]);

//        String[][] data2 = http.get_contract("2018-08");
//        System.out.println(Arrays.deepToString(data2));

//        double LatestPrice = http.get_LatestPrice();
//        System.out.println(LatestPrice);


//        double S0 = http.get_S0();
//        System.out.println(S0);

        double result5 = http.get_r();
        System.out.println(result5);


    }
*/

    private String getDataFromURL(String url) throws IOException {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");//设置为GET方法
        con.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = con.getResponseCode();

//        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(),"gbk"));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return response.toString();//String类，包含整个json数据
    }

    public String[] get_T() throws IOException {
        String url = "http://stock.finance.sina.com.cn/futures/api/openapi.php/StockOptionService.getStockName";
        String result = getDataFromURL(url);

        String pattern = "[0-9]{4}-[0-9]{2}";//匹配日期的正则表达式
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(result);

        String[] data = new String[4];//将四个日期放入数组中
        int i = 0;
        while (m.find()) {
            String temp = m.group();
            if (i > 0) data[i - 1] = temp;
            i++;
        }

//        System.out.println(Arrays.toString(data));
        return data;
    }

    public String[] get_expireAndremainder(String date) throws IOException {


        String url1 = "http://stock.finance.sina.com.cn/futures/api/openapi.php/StockOptionService.getRemainderDay?date=" + date;
        String result = getDataFromURL(url1);

        String[] data = new String[2];
        String expireDay = "\"expireDay\":\"(.*?)\"|\"remainderDays\":(.*?),\""; //匹配到期日
        Pattern r = Pattern.compile(expireDay);
        Matcher m = r.matcher(result);
        int i = 0;
        while (m.find()) {
            String temp = m.group(i + 1);
//            System.out.println(temp);
            data[i] = temp;
            i++;
        }
        return data;
    }

    public String[][] get_contract(String date) throws IOException {
        date = date.replace("-", "");
        date = date.substring(2);
        String url = "http://hq.sinajs.cn/list=OP_UP_510050" + date + ",OP_DOWN_510050" + date;
        String result = getDataFromURL(url);


        String[] output = result.split(";", 2);
        String result1 = output[0];
        String result2 = output[1];

        String contract1 = "([A-Z]{3}_OP_.*?),";
        Pattern r1 = Pattern.compile(contract1);
        Matcher m1 = r1.matcher(result1);

        String[] data1 = new String[30];
        int i = 0;
        while (m1.find()) {
            String temp = m1.group(1);
//            System.out.println(temp);
            data1[i] = temp;
            i++;
        }


        String contract2 = "([A-Z]{3}_OP_.*?),";
        Pattern r2 = Pattern.compile(contract2);
        Matcher m2 = r2.matcher(result2);

        String[] data2 = new String[30];
        int j = 0;
        while (m2.find()) {
            String temp = m2.group(1);
//            System.out.println(temp);
            data2[j] = temp;
            j++;
        }


        String[][] rtn = new String[2][];
        for (int k = 0; k < 2; k++) {
            if (k == 0) {
                rtn[k] = new String[i];
                System.arraycopy(data1, 0, rtn[k], 0, i);
            } else {
                rtn[k] = new String[j];
                System.arraycopy(data2, 0, rtn[k], 0, j);
            }

        }


        return rtn;
    }

    public double get_LatestPrice() throws IOException {
        String url = "http://hq.sinajs.cn/list=s_sh510050";
        String result = getDataFromURL(url);

        double LatestPrice = 0;

        String pattern = "50ETF,([0-9]\\.[0-9]{3})";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(result);

        if (m.find()) {
            LatestPrice = Double.valueOf(m.group(1));
        }
        return LatestPrice;
    }

    public double get_S0() throws IOException {
        String url = "http://hq.sinajs.cn/list=f_510050";
        String result = getDataFromURL(url);

        double S0 = 0;
        String pattern = "50ETF,([0-9]\\.[0-9]{3})";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(result);

        if (m.find()) {
            S0 = Double.valueOf(m.group(1));
        }

        return S0;
    }

    public double get_r() throws IOException {
        String pageurl = "http://www.shibor.org/shibor/web/html/shibor.html";

        StringBuffer sb = new StringBuffer();

        URL url = new URL(pageurl);
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), "utf-8"));
        String line;
        while ((line = in.readLine()) != null) {
            sb.append(line);
            sb.append("\n");
        }
        in.close();

        String result = sb.toString();
        String pattern = "<td width=\"30%\" align=\"center\">(.*?)</td>";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(result);


        double rtn = 0;
        while (m.find()) {
//            System.out.println(m.group(1));
            rtn = Double.valueOf(m.group(1));
        }


        return rtn;
    }

    public double get_Sigma() throws IOException {
        String url = "http://www.optbbs.com/d/csv/d/data.csv?v=";
        Date d = new Date();
        long time = d.getTime();
        url = url + time;
        String result = getDataFromURL(url);
        String[] output = result.split(",");
        String regex = ".*?\\..*?";
        Pattern p=Pattern.compile(regex);

        String s="0";
        for(int i=output.length-1;i>=0;i--){
            s = output[i];
            Matcher m=p.matcher(s);
            if(m.matches()){
                break;
            }
        }
        return Double.valueOf(s);
    }


    public double[] get_Attributes(String contract) throws IOException {
        String url = "http://hq.sinajs.cn/list="+contract;
        String result = getDataFromURL(url);
        String pattern = "=\"(.*?)\"";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(result);
        String atb=null;
        if(m.find()) atb = m.group(1);
        String[] output = atb.split(",");

        double ExercisePrice = Double.valueOf(output[7]);
        double Price1 = Double.valueOf(output[1]);
        double latestPrice = Double.valueOf(output[2]);
        double Price2 = Double.valueOf(output[3]);
        double y_close = Double.valueOf(output[8]);

        contract = contract.replace("OP","SO");
        url = "http://hq.sinajs.cn/list="+contract;
        result = getDataFromURL(url);
        pattern = "=\"(.*?)\"";
        r = Pattern.compile(pattern);
        m = r.matcher(result);
        String greekValue = null;
        if(m.find()) greekValue = m.group(1);
        output = greekValue.split(",");
//        System.out.println(Arrays.toString(output));
        double delta = Double.valueOf(output[5]);
        double gamma = Double.valueOf(output[6]);
        double theta = Double.valueOf(output[7]);
        double vega = Double.valueOf(output[8]);

        return new double[]{ExercisePrice, Price1, Price2, y_close,delta,gamma,theta,vega,latestPrice};

    }

    public String[] getShortNameAndCodeName(String contract) throws IOException {
        contract = contract.replace("OP","ZL");
        String url = "http://hq.sinajs.cn/list="+contract;
        String result = getDataFromURL(url);

        String[] output = result.split(",");
        String temp = output[0];
        String[] out = temp.split("\"");
        String shortName = out[1];
        String codeName = output[18];
        return new String[]{shortName,codeName};
    }

}
