package utf8.citicup.serviceImpl;

import org.springframework.stereotype.Service;
import utf8.citicup.domain.entity.Option;
import utf8.citicup.domain.entity.RecommendOption1;
import utf8.citicup.domain.entity.ResponseMsg;
import utf8.citicup.service.RecommendService;
import utf8.citicup.service.util.GetData;

import javax.persistence.criteria.CriteriaBuilder;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

@Service
public class RecommendServiceImpl implements RecommendService {
    /*用户输入数据*/
    private double M0;//托管资金总额
    private double k;//投资者允许最大亏损
    private double a;//投资者可接受的最大损失百分比
    private String T;//投资者预测价格有效时间
    private char combination;//A-H 八个选项选其一
    private double p1;
    private double p2;
    private double sigma1;
    private double sigma2;

    /*网络获取数据*/
    private GetData dataSource;
    private double lastestOptionPrice;//最新行权价
    private double S0;//实时标的价格[7]
    private double r;//固定年化收益率[5]
    private double sigma;//实时波动率[6]
    private int t;//距离期权剩余时间（天)
    private String[] expiredMonths;//期权月份4个
    private Map<String, ArrayList<Option>> chigh = new Hashtable<String, ArrayList<Option>>();
    private Map<String, ArrayList<Option>> clow = new Hashtable<String, ArrayList<Option>>();
    private Map<String, ArrayList<Option>> phigh = new Hashtable<String, ArrayList<Option>>();
    private Map<String, ArrayList<Option>> plow = new Hashtable<String, ArrayList<Option>>();
//    private String[] expireTimeArray = {};//网上获取

    /*自设值*/
    private double dv;//股票分红率
    private double S[];
    private double eps;
    private double eps1;

    /*计算得到的值*/
    private double d_1;
    private double d_2;
    private double M;
    private double delta;
    private double gamma;
    private double vega;
    private double theta;
    private double rho;
    private double beta;
    String[] month;

    /*新用到的结构类型*/
    class structD{
        public Option[] optionCombination;
        public double p0;
        public double pb;
        public double z_delta;
        public double z_gamma;
        public double z_vega;
        public double z_theta;
        public double z_rho;
    }

    //回测到某个月返回的日期
    private String caculateDate(int year, int month,int difference){
        int date = caculateDateFrom1(year,month);
        String result;
        if(date>=difference) {
            result =String.valueOf(year)+"/"+String.valueOf(month)+"/"+String.valueOf(date+1-difference);
        }
        else {
            int temp = difference-date;
            month--;
            if(month<=0){
                year--;
                month+=12;
            }
            int days = caculateDaysInMonth(year, month);
            int day = days-temp+1;
            result = String.valueOf(year)+"/"+String.valueOf(month)+"/"+String.valueOf(day);
        }
        return result;
    }

    //计算一共差几天
    private int caculataDifference(int year, int month, int day){
        int date = caculateDateFrom1(year,month);
        if(day<date) return date-day;
        else{
            date = caculateDateFrom1(year,month+1);
            int daysInMonth = caculateDaysInMonth(year, month);
            return daysInMonth-day+date+1;
        }
    }

    //计算第四个星期三距离1号差几天
    private int caculateDateFrom1(int year, int month){
        int WeekDay = -1;
        int startDay = 1;
        if(1 == month || 2 == month){
            month += 12;
            year--;
        }
        WeekDay = (startDay + 1 + 2 * month + 3 * (month + 1) / 5 + year + year / 4 - year / 100 + year / 400) % 7;
        if(WeekDay<=3) return 24-WeekDay;
        else return 31-WeekDay;
    }

    //计算一个月有几天
    private int caculateDaysInMonth(int year, int month){
        if(month == 2) return isLeapYear(year)? 29:28;
        else return (int) Math.ceil(Math.abs(month-7.5)%2+30);
    }

    //计算是否为闰年
    private boolean isLeapYear(int year){
        return ((year%4==0 && year%100!=0) || year%400==0);
    }

    //计算是第几个阶段的期权
    private int caculateFirstFew(String T){
        return Arrays.binarySearch(expiredMonths,T)+1;
    }

    //计算期权回测时到期日
    private String caculateBackTestExpiryDate(String date, int firstFew){
        String[] dates = date.split("/");
        int year = Integer.parseInt(dates[0]);//得到传入的年月日 和 阶段
        int month = Integer.parseInt(dates[1]);
        int day = Integer.parseInt(dates[2]);

        int thirdWednesday = caculateDateFrom1(year, month)+1;
        //判断是不是这个月第四个星期三之后
        if(day >= thirdWednesday){
            year += (month+1)/13;
            month = (month+1)%13;
        }
        //不清楚第四个星期三当天是什么情况，先暂时定第四个星期三当天的第一个阶段是下个月的第四个星期三
        if(firstFew<=2){
            year += (month+firstFew) /13;
            month = (month+firstFew) %13;
        }else {                                                 //找季月
            month+=2;
            int temp = firstFew-2;      //找接下来的几个季月
            while (temp!=0){
                year += (month+1) /13;
                month = (month+1)%13;
                if(month==0) month++;
                if(month %3 == 0){ temp--;}
            }
        }
        int expiryDay = caculateDateFrom1(year,month)+1;  //计算当时的第四个星期三是几号

        return String.valueOf(year)+"/"+String.valueOf(month)+"/"+String.valueOf(expiryDay);
    }



    public RecommendServiceImpl(){
        dataSource = new GetData();
    }

    /*从网络获取所需的数据*/
    public void upDataFromNet() throws IOException {
        r = dataSource.get_r();
        S0 = dataSource.get_S0();//实时标的价格
        sigma = dataSource.getSigma();//实时波动率
        lastestOptionPrice = dataSource.get_LatestPrice();
        dv = 0;//股票分红率
        expiredMonths = dataSource.get_T();
        String[][] Optionss = dataSource.get_contract(expiredMonths[0]);
//        String[] cOptionsName = Optionss[0];
//        String[] pOptionsName = Optionss[1];
//        System.out.println(d[0].p0);
    }

    /*标准正态分布函数*/
    private double normcdf(double num) {
        return 0;
    }

    /*正态分布概率函数*/
    private double p() {
        return 0;
    }

    private double p(double num) {return 0;}

    private double Options(int cp, double S0, double k, int t, double r, double sigma, double dv) {
        this.t = t;
        this.d_1 = Math.log(S0 / k) + (r - dv + 0.5 * Math.pow(sigma, 2) * t) / sigma / Math.pow(t, 0.5);
        this.d_2 = d_1 - (sigma * (Math.pow(t, 0.5)));
        return cp * S0 * Math.exp(-1 * dv * t) * normcdf(cp * d_1) -
                cp * k * Math.exp(-1 * r * t) * normcdf(cp * d_2);
    }
/*
    //计算希腊值
    private void GreekCharacteValue(){
        d_1 = (Math.log(S0/k) + (r - dv + 0.5 * Math.pow(sigma, 2) * t));
        d_2 = d_1 - sigma * (Math.pow(t, 0.5));
        delta = cp_sign * normcdf(cp_sign * d_1);
        gamma = (-1 * S0 * normcdf(cp * d_1) * sigma / (2 * (Math.pow(t, 0.5))) - cp * r * k * Math.exp(-1 * r * t) * normcdf(cp * d_2)) / 365;
        vega = normcdf(cp * d_1) / (S0 * sigma * Math.pow(t, 0.5));
        theta = (S0 * (Math.pow(t, 0.5)) * normcdf(cp * d_1)) / 100;
        rho = (k * cp * t * Math.exp(-1 * r * t) * normcdf(cp * d_2)) / 100;
        beta = (S0 * delta) / price;
    }
*/
    private double[] Interest(int cp, double[] S, double k, int t, double r, double sigma, double dv, double price) {
        this.t = t;
        int n = S.length;
        double[] C = new double[n];
        if (t != 0) {
            for (int i = 0; i < n; i++) {
                double temp = Options(cp, S[i], k, t, r, sigma, dv);
                C[i] = 10000 * (a - price);
            }
        } else {
            for (int i = 0; i < n; i++) {
                if (cp == 1) {
                    C[i] = 10000 * (Math.max(S[i] - k, 0) - price);
                }
                if (cp == -1){
                    C[i] = 10000 * (Math.max(k - S[i], 0) - price);
                }
            }
        }
        return C;
    }

    /*此算法有问题*/
    private double Expected(double[] C_new, double[] S, double p1, double p2, double sigma1, double sigma2){
        int n = S.length;
        double[] X = new double[n];
        for(int i = 0;i < n;i++){
            double s = S[i];
            double x = Math.log(s/S0)-Math.log((p1 + p2)/(2 * S0));
            X[i] = x;
        }
        double sum = 0;
        for(int i = 0;i < n;i++){
            sum += p(X[i]);//为了计算概率, p(x)是x处的概率密度
        }
        double E = 0;
        for(int i = 0;i < n;i++){
            double c = C_new[i];
            E += c*p(X[i])/sum;
        }
        return E;
    }

    @Override
    public ResponseMsg recommendPortfolio(double M0, double k, double a, String T, char combination, double p1, double p2, double sigma1, double sigma2, int w1, int w2) {
        //参数都是由用户输入的
        this.M0 = M0;
        this.k = k;
        this.a = a;
        this.T = T;
        this.combination = combination;
        this.p1 = p1;
        this.p2 = p2;
        this.sigma1 = sigma1;
        this.sigma2 = sigma2;

        /*实时数据的获取*/
        try {
            this.upDataFromNet();
        } catch (IOException e) {
            return new ResponseMsg(2000, "实时数据错误", null);
        }

        /*货币基金与衍生品组合分配：*/
        this.M = M0 * (r * Math.ceil(t / 30.0) / 12 + a) / (1 + r * Math.ceil(t / 30.0) / 12);
        /*Options*/
        switch (combination) {
            case 'A':
                return this.combinationA();
            case 'B':
                return this.combinationB();
            case 'C':
                return this.combinationC();
            case 'D':
                return this.combinationD();
            case 'E':
                return this.combinationE();
            case 'F':
                return this.combinationF();
            case 'G':
                return this.combinationG();
            case 'H':
                return this.combinationH();
            default:
                return new ResponseMsg(2002, "no match combination", null);
        }
    }


    private ResponseMsg combinationA() {
        Option[] clow_T = new Option[clow.get(T).size()];
        clow.get(T).toArray(clow_T);
        Option[] chigh_T = new Option[chigh.get(T).size()];
        chigh.get(T).toArray(chigh_T);
        List<structD> D = new ArrayList<structD>();

        /*第一步*/
        for (Option aClow_T : clow_T) {
            double i_delta = aClow_T.getDelta();
            double i_gamma = aClow_T.getGamma();
            double i_vega = aClow_T.getVega();
            double i_theta = aClow_T.getTheta();
            double i_rho = aClow_T.getRho();
            double i_price2 = aClow_T.getPrice2();
            double i_yclose = aClow_T.getYclose();
            double i_k = aClow_T.getK();
            for (Option aChigh_T : chigh_T) {
                double j_delta = aChigh_T.getDelta();
                double j_gamma = aChigh_T.getGamma();
                double j_vega = aChigh_T.getVega();
                double j_theta = aChigh_T.getTheta();
                double j_rho = aChigh_T.getRho();
                double j_price1 = aChigh_T.getPrice1();
                if (Math.abs(2 * j_delta - i_delta) < 0.0001 && (2 * j_vega - i_vega) > 0){
                    double p0 = 2*j_price1;
                    double pb = i_price2 + Math.max((0.12 * i_yclose - (i_yclose - i_k)), 0.07 * i_yclose);
                    p0 = p0 + pb;
                    //将这个组合（即买入两份j,卖出一份i）的其他希腊值放入一个集合D中
                    structD d = new structD();
                    d.p0 = p0;
                    d.pb = pb;
                    d.z_delta=2*j_delta-i_delta;
                    d.z_gamma=2*j_gamma-i_gamma;
                    d.z_vega=2*j_vega-i_vega;
                    d.z_theta=2*j_theta-i_theta;
                    d.z_rho=2*j_rho-i_rho;
                    d.optionCombination = new Option[]{aClow_T, aChigh_T};
                    D.add(d);
                }
            }
        }
        return new ResponseMsg(0, "combination A return", new RecommendOption1());
    }

    private ResponseMsg combinationB() {
        return new ResponseMsg(0, "combination B return", new RecommendOption1());
    }

    private ResponseMsg combinationC() {
        return new ResponseMsg(0, "combination C return", new RecommendOption1());
    }

    private ResponseMsg combinationD() {
        return new ResponseMsg(0, "combination D return", new RecommendOption1());
    }

    private ResponseMsg combinationE() {
        return new ResponseMsg(0, "combination E return", new RecommendOption1());
    }

    private ResponseMsg combinationF() {
        return new ResponseMsg(0, "combination F return", new RecommendOption1());
    }

    private ResponseMsg combinationG() {
        return new ResponseMsg(0, "combination G return", new RecommendOption1());
    }

    private ResponseMsg combinationH() {
        return new ResponseMsg(0, "combination H return", new RecommendOption1());
    }

    @Override
    public ResponseMsg hedging(int N0, double a, double s_exp, String T) {
        try {
            upDataFromNet();
            Option[] plow_T = new Option[plow.get(T).size()];
            Option[] phigh_T = new Option[phigh.get(T).size()];
            this.plow.get(T).toArray(plow_T);
            this.phigh.get(T).toArray(phigh_T);
            int N = (int) (N0*a);
            double p_asset = lastestOptionPrice;

            //第一步
            Option[] List_D1 = calcute_D(plow_T,s_exp,N,p_asset);
            Option[] List_D2 = calcute_D(phigh_T,s_exp,N,p_asset);

            System.out.println(Arrays.toString(List_D1));
            System.out.println(Arrays.toString(List_D2));

            //第二步
            Option i1 = max_loss(List_D1,s_exp,N,p_asset);
            Option i2 = max_loss(List_D2,s_exp,N,p_asset);
            double i_k1 = i1.getK();
            double i_k2 = i2.getK();
            double i_k;
            Option optionI = new Option();

            if(i_k1>i_k2){ i_k=i_k2; optionI = i2;}
            else { i_k = i_k1; optionI = i1;}

//            int reaminDays = Integer.parseInt(dataSource.get_expireAndremainder(T)[1]);   tttt

            String[] nowStr = T.split("-");
            int nowYear = Integer.parseInt(nowStr[0]);
            int nowMonth = Integer.parseInt(nowStr[1]);
            Calendar c= Calendar.getInstance();
            int nowDay = c.get(Calendar.DATE);







            //第三步



            if(flag) {
                for (String m : month) {
                    String[] str = m.split("-");
                    int year = Integer.parseInt(str[0]);                //得到回测年月
                    int month = Integer.parseInt(str[1]);

                    int difference = caculataDifference(nowYear,nowMonth,nowDay);  //得到今天距离最近的第四个星期三所差的天数
                    String startDate =caculateDate(year,month,difference);       //得到回测月的起始日期
                    int stage = caculateFirstFew(T);                            //得到是在第几个阶段
                    String endDate = caculateBackTestExpiryDate(startDate,stage);//得到回测的终止日期



                    Option[] bt_plow = get_from_dataBase(m);
                    for (Option bt_i : bt_plow) {
                        double bt_i_k = bt_i.getK();
                        if ((i_k - p_asset) - (bt_i_k - asset_close1) <= eps) {
                            double total_loss;
                            double bt_i_delta = bt_i.getDelta();
                            double bt_i_num = (int) (N / (10000 * Math.abs(bt_i_delta))) + 1;
                            if (asset_close2 < bt_i_k) {
                                total_loss = N * bt_i_close1 + (bt_i_num * 10000 - N) * (bt_i_close1 - bt_i_close2) + N * (asset_close1 - bt_i_k);
                            } else {
                                total_loss = bt_i_close1 * bt_i_num * 10000 + N * (asset_close1 - asset_close2);
                            }
                        }
                    }
                }
            }
            else {
                for(String m:month){
                    Option[] bt_phigh = get_from_dataBase(m);
                    for(Option bt_i:bt_phigh){
                        double bt_i_k = bt_i.getK();
                        if((i_k-p_asset)-(bt_i_k-asset_close1) <= eps){
                            double total_loss;
                            double bt_i_delta = bt_i.getDelta();
                            double bt_i_num = (int) (N / (10000 * Math.abs(bt_i_delta))) + 1;
                            if (asset_close2 < bt_i_k) {
                                total_loss = N * bt_i_close1 + (bt_i_num * 10000 - N) * (bt_i_close1 - bt_i_close2) + N * (asset_close1 - bt_i_k);
                            }
                        }
                    }
                }
            }
            } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    private Option[] calcute_D(Option[] list,double s_exp,int N,double p_asset){
        ArrayList<Option> D = new ArrayList<Option>();
        for (Option i : list) {
            double i_k = i.getK();
            if (i_k > s_exp) {
                double i_delta = i.getDelta();
                double i_price1 = i.getPrice1();
                int i_num = (int)Math.ceil(N /(10000*Math.abs(i_delta)));
                if((N * (p_asset - s_exp)) > (((N * p_asset) - (((i_num * 10000) - N) * (i_k - s_exp)) - (N * i_k)) + (i_num * 10000 * i_price1))){
                    D.add(i);
                }
            }
        }
        return (Option[])D.toArray();
    }

    private Option max_loss(Option[] List_D,double s_exp, int N, double p_asset){
        double cost;
        double max_loss = Double.MAX_VALUE;
        Option rtn=null;
        for(Option i:List_D){
            double i_k = i.getK();
            double i_delta = i.getDelta();
            double i_price1 = i.getPrice1();
            int i_num = (int)Math.ceil(N /(10000*Math.abs(i_delta)));
            cost = i_num*10000*i_price1;
            double temp = ((N * p_asset) - (((i_num * 10000) - N) * (i_k - s_exp)) - (N * i_k)) + cost;
            if(temp<max_loss){
                max_loss = temp;
                rtn = i;
            }
        }

        return rtn;
    }

    @Override
    public ResponseMsg customPortfolio(Option[] list) {
        return null;
    }
}
