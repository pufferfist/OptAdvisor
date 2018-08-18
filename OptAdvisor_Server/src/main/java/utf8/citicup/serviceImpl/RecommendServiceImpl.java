package utf8.citicup.serviceImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import utf8.citicup.dataService.historyDataService.OptionBasicInfoDataService;
import utf8.citicup.dataService.historyDataService.OptionTsdDataService;
import utf8.citicup.dataService.historyDataService.TimeSeriesDataSerice;
import utf8.citicup.dataServiceImpl.historyDataServiceImpl.OptionBasicInfoDataServiceImpl;
import utf8.citicup.dataServiceImpl.historyDataServiceImpl.OptionTsdDataServiceImpl;
import utf8.citicup.dataServiceImpl.historyDataServiceImpl.TimeSeriesDataServiceImpl;
import utf8.citicup.domain.entity.Option;
import utf8.citicup.domain.entity.RecommendOption1;
import utf8.citicup.domain.entity.RecommendOption2;
import utf8.citicup.domain.entity.ResponseMsg;
import utf8.citicup.domain.historyEntity.OptionBasicInfo;
import utf8.citicup.domain.historyEntity.OptionTsd;
import utf8.citicup.domain.historyEntity.TimeSeriesData;
import utf8.citicup.service.RecommendService;
import utf8.citicup.service.util.GetData;
import utf8.citicup.service.util.StatusMsg;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

@Service
public class RecommendServiceImpl implements RecommendService {

    private static final double eps = 0.025;
    private static final double zero = 0.001;
    /*用户输入值*/
    private String T;//投资者预测价格有效时间
    private double p1;
    private double p2;
    private double sigma1;
    private double sigma2;
    private double w1;
    private double w2;

    /*网络获取数据*/
    private GetData dataSource;
    private double lastestOptionPrice;//最新行权价
    private double S0;//实时标的价格[7]
    private double r;//固定年化收益率[5]
    private double sigma;//实时波动率[6]
    private int t;//距离期权剩余时间（天)
    private String[] expiredMonths;//期权月份4个
    private Map<String, ArrayList<Option>> chigh = new Hashtable<>();
    private Map<String, ArrayList<Option>> clow = new Hashtable<>();
    private Map<String, ArrayList<Option>> phigh = new Hashtable<>();
    private Map<String, ArrayList<Option>> plow = new Hashtable<>();
    private List<Option> cpar = new ArrayList<>();//平价看涨
    private List<Option> ppar = new ArrayList<>();//平价看跌
    private List<Option> chighShallow = new ArrayList<>();//浅度高价看涨
    private List<Option> plowShallow = new ArrayList<>();//浅度低价看跌
    private List<Option> chighDeep = new ArrayList<>();//深度低价看跌
    private List<Option> plowDeep = new ArrayList<>();//深度低价看跌
//    private String[] expireTimeArray = {};//网上获取

    /*自设值*/
    private double dv;//股票分红率
    private double S[];
    private double eps1;
    private String[] month={"2015-02"};

    /*计算得到的值*/
    private double d_1;
    private double d_2;
    private double M;
    private Logger logger = LoggerFactory.getLogger(RecommendService.class);

    /*回测数据库*/
    @Autowired
    private OptionTsdDataService optionTsdDataService;
    @Autowired
    private OptionBasicInfoDataService optionBasicInfoDataService;
    @Autowired
    private TimeSeriesDataSerice timeSeriesDataSerice;

    /*新用到的结构类型*/
    class structD{
        Option[] optionCombination;//组合只有两种期权，第一个是买入，第二个是卖出（同时有买入卖出时）
        int[] buyAndSell;
        int rank; //组合中有好几种构造，用来记载是构造几？
        double p0;
        double pb;
        double z_delta;
        double z_gamma;
        double z_vega;
        double z_theta;
        double z_rho;
        int num;//购买的份数
        double E;//
        double beta;//组合风险值
        double goal;
    }

    private void test(){
        try {
//            System.out.println("hello");
            List <structD> D = new ArrayList<>();
            upDataFromNet();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        List<int[]> D = new ArrayList<>();
//        D.add(new int[]{1,2,3,4});
//        D.add(new int[]{4,5,6});
//        D.add(new int[]{7,8,9});
//        D.add(new int[]{0});
//        List <Integer> array = new ArrayList<>();
//        re(D, 0, array);
//        Calendar c= Calendar.getInstance();
//        int nowYear = c.get(Calendar.YEAR);
//        int nowMonth = c.get(Calendar.MONTH);
//        int nowDay = c.get(Calendar.DATE);
//
//        String m = "2015-3";
//        String[] str = m.split("-");
//        int year = Integer.parseInt(str[0]);                //得到回测年月
//        int month = Integer.parseInt(str[1]);
//
//        int difference = calculateDifference(nowYear, nowMonth, nowDay);  //得到今天距离最近的第四个星期三所差的天数
//        String startDate = calculateDate(year, month, difference);       //得到回测月的起始日期
//        System.out.println(T);
//        int stage = calculateFirstFew(T);                            //得到是在第几个阶段
//        System.out.println(stage);
//        String endDate = calculateBackTestExpiryDate(startDate, stage);//得到回测的终止日期
//        endDate = "2015/4/22";
//        System.out.println(startDate);
//        System.out.println(endDate);
//        List<OptionTsd> one = new ArrayList<>();
//
//
//        one = optionTsdDataService.complexFindRange(startDate, endDate, false, -eps, eps);
//        System.out.println(one.size());
//        System.out.println(nowDay);
    }

    private void re(List <int[]> D, int index, List<Integer> array){
        if(index == D.size())
        {
            int sum = 0;
            for(int x : array){
                sum = x + sum * 10;
            }
            System.out.println(sum);
        }
        else
        {
            for(int X : D.get(index)){
                array.add(X);
                re(D, index + 1, array);
                array.remove(index);
            }
        }
    }

    //回测到某个月返回的日期
    private String calculateDate(int year, int month, int difference){
        int date = calculateDateFrom1(year,month);
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
            int days = calculateDaysInMonth(year, month);
            int day = days-temp+1;
            result = String.valueOf(year)+"/"+String.valueOf(month)+"/"+String.valueOf(day);
        }
        return result;
    }

    //计算一共差几天
    private int calculateDifference(int year, int month, int day){
        int date = calculateDateFrom1(year,month);
        if(day<date) return date-day;
        else{
            date = calculateDateFrom1(year,month+1);
            int daysInMonth = calculateDaysInMonth(year, month);
            return daysInMonth-day+date+1;
        }
    }

    //计算第四个星期三距离1号差几天
    private int calculateDateFrom1(int year, int month){
        int startDay = 1;
        if(1 == month || 2 == month){
            month += 12;
            year--;
        }
        int WeekDay = (startDay + 1 + 2 * month + 3 * (month + 1) / 5 + year + year / 4 - year / 100 + year / 400) % 7;
        if(WeekDay<=3) return 24-WeekDay;
        else return 31-WeekDay;
    }

    //计算一个月有几天
    private int calculateDaysInMonth(int year, int month){
        if(month == 2) return isLeapYear(year)? 29:28;
        else return (int) Math.ceil(Math.abs(month-7.5)%2+30);
    }

    //计算是否为闰年
    private boolean isLeapYear(int year){
        return ((year%4==0 && year%100!=0) || year%400==0);
    }

    //计算是第几个阶段的期权
    private int calculateFirstFew(String T){
        return Arrays.binarySearch(expiredMonths,T)+1;
    }

    //计算期权回测时到期日
    private String calculateBackTestExpiryDate(String date, int firstFew){
        String[] dates = date.split("/");
        int year = Integer.parseInt(dates[0]);//得到传入的年月日 和 阶段
        int month = Integer.parseInt(dates[1]);
        int day = Integer.parseInt(dates[2]);

        int thirdWednesday = calculateDateFrom1(year, month)+1;
        //判断是不是这个月第四个星期三之后
        if(day >= thirdWednesday){
            year += (month+1)/13;
            month = (month+1)%13;
            if(month == 0) month++;
        }
        //不清楚第四个星期三当天是什么情况，先暂时定第四个星期三当天的第一个阶段是下个月的第四个星期三
        if(firstFew<=2){
            year += (month+firstFew-1) /13;
            month = (month+firstFew-1) %13;
            if(month==0) month++;
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
        int expiryDay = calculateDateFrom1(year,month)+1;  //计算当时的第四个星期三是几号

        return String.valueOf(year)+"/"+String.valueOf(month)+"/"+String.valueOf(expiryDay);
    }

    public RecommendServiceImpl(){
        dataSource = new GetData();
        S = new double[21];
        int i = 0;
        double temp = 2;
        DecimalFormat df = new DecimalFormat("0.00");
        while(temp <= 3){
            temp = Double.valueOf(df.format(temp));
            S[i] = temp;
            temp += 0.05;
            i++;
        }
        dv = 0;//股票分红率
        T = "2018-09";
    }

    /*从网络获取所需的数据*/
    private void upDataFromNet() throws IOException {
        r = dataSource.get_r();
        t = Integer.parseInt(dataSource.get_expireAndremainder(T)[1]);
        S0 = dataSource.get_S0();//实时标的价格
        sigma = dataSource.get_Sigma();//实时波动率
        lastestOptionPrice = dataSource.get_LatestPrice();
        expiredMonths = dataSource.get_T();
        for (String expiredMonth : expiredMonths) {
            //region 根据行权名称得到相关数据
            chigh.put(expiredMonth, new ArrayList<>());
            clow.put(expiredMonth, new ArrayList<>());
            phigh.put(expiredMonth, new ArrayList<>());
            plow.put(expiredMonth, new ArrayList<>());

            String[][] Optionss = dataSource.get_contract(expiredMonth);
            String expireTime = dataSource.get_expireAndremainder(expiredMonth)[0];
//            System.out.println(expiredMonth);
            for(int i = 0; i < 2; i++) {
                for (String cpOption : Optionss[i]) {
                    int cp;
                    if(i == 0)
                        cp = 1;
                    else
                        cp = -1;

                    //region 得到某个期权的属性 并且将值放入一个实体Option中
                    double[] attributes = dataSource.get_Attributes(cpOption);
                    double k = attributes[0];
                    double price1 = attributes[1];
                    double price2 = attributes[2];
                    double yclose = attributes[3];
                    double delta = attributes[4];
                    double gamma = attributes[5];
                    double theta = attributes[6];
                    double vega = attributes[7];

                    Option newOption = new Option();
                    newOption.setTradeCode(cpOption);
                    newOption.setK(k);
                    newOption.setCp(cp);
                    newOption.setPrice1(price1);
                    newOption.setPrice2(price2);
                    newOption.setYclose(yclose);
                    newOption.setExpireTime(expireTime);
                    newOption.setDelta(delta);
                    newOption.setGamma(gamma);
                    newOption.setTheta(theta);
                    newOption.setVega(vega);
//                    GreekCharacteValue(newOption, Integer.parseInt(dataSource.get_expireAndremainder(expiredMonth)[1]));
                    //endregion

                    //region 判断cp 和 low high
                    if(cp == 1){
                        if(k >= this.lastestOptionPrice){
                            chigh.get(expiredMonth).add(newOption);
//                            System.out.println(newOption.toString());
                        }
                        else{
                            clow.get(expiredMonth).add(newOption);
                        }
                    }
                    else{
                        if(k >= this.lastestOptionPrice){
                            phigh.get(expiredMonth).add(newOption);
                        }
                        else{
                            plow.get(expiredMonth).add(newOption);
                        }
                    }
                    //endregion
                }
            }
            //endregion
        }
    }

    private void parShallowDeep(){
        Option[] chigh_T = chigh.get(T).toArray(new Option[0]);
        Option[] clow_T = clow.get(T).toArray(new Option[0]);
        Option[] phigh_T = phigh.get(T).toArray(new Option[0]);
        Option[] plow_T = plow.get(T).toArray(new Option[0]);
        
        //平价看涨
        sievesImpl(cpar, chigh_T,S0 - eps, S0 + eps);
        sievesImpl(cpar, clow_T,S0 - eps, S0 + eps);
        //平价看跌
        sievesImpl(ppar, phigh_T,S0 - eps, S0 + eps);
        sievesImpl(ppar, plow_T,S0 - eps, S0 + eps);
        //浅度高价看涨
        sievesImpl(chighShallow, chigh_T, S0, S0 + 2 * eps);
        //浅度低价看跌
        sievesImpl(plowShallow, plow_T, S0 - 2 * eps, S0);
        //深度高价看涨
        sievesImpl(chighDeep, chigh_T,S0 + 2 * eps, 5);
        //深度低价看跌
        sievesImpl(plowDeep, plow_T, 0,S0 - 2 * eps);
    }

    //筛选出平价的期权
    private void sievesImpl(List<Option> dst, Option[] a, double x, double y){
        for (Option anA : a) {
            if (anA.getK() > x && anA.getK() < y)
                dst.add(anA);
        }
    }

    /*标准正态分布的分布函数*/
    private double normcdf(double a) {
        //TODO:标准正态分布函数
        double p = 0.2316419;
        double b1 = 0.31938153;
        double b2 = -0.356563782;
        double b3 = 1.781477937;
        double b4 = -1.821255978;
        double b5 = 1.330274429;

        double x = Math.abs(a);
        double t = 1/(1+p*x);

        double val = 1 - (1/(Math.sqrt(2*Math.PI)) * Math.exp(-1*Math.pow(a, 2)/2)) * (b1*t + b2 * Math.pow(t,2) + b3*Math.pow(t,3) + b4 * Math.pow(t,4) + b5 * Math.pow(t,5) );

        if ( a < 0 )
        {
            val = 1- val;
        }

        return val;
    }

    /*正态分布概率函数*/
    private double p(double x) {
        //TODO：正态分布概率函数
        return 1 / Math.sqrt(2 * Math.PI) * Math.exp(-1 * Math.pow(x, 2) / 2);
    }

    private double normpdf(double x){
        //TODO: normpdf为标准正态分布的概率密度函数
        return 0;
    }

    private double Options(int cp, double k) {
        d_1 = Math.log(S0 / k) + (r - dv + 0.5 * Math.pow(sigma, 2) * t) / sigma / Math.pow(t, 0.5);
        d_2 = d_1 - (sigma * (Math.pow(t, 0.5)));
        return cp * S0 * Math.exp(-1 * dv * t) * normcdf(cp * d_1) -
                cp * k * Math.exp(-1 * r * t) * normcdf(cp * d_2);
    }
    /*
    //计算希腊值，此处的t不是全局的t
    private void GreekCharacteValue(Option A, int t){
        //unfinished 希腊值计算未完成
        double a = t / 365.0;
        d_1 = (Math.log(S0/A.getK()) + (r - dv + 0.5 * Math.pow(sigma, 2)) * a)/ sigma /Math.sqrt(a);
        d_2 = d_1 - sigma * (Math.sqrt(a));
//        System.out.println("cp:" + A.getCp() + "a:" + a + " sigma:" + sigma + " S0:" + S0+ " k:" + A.getK()+ " r:" + r + " d_1:" + d_1);
//        System.out.println("d_1:" + d_1 + " d_2:" + d_2);
        A.setDelta(A.getCp() * normcdf(A.getCp() * d_1));
        A.setGamma((-1 * S0 * normpdf(A.getCp() * d_1) * sigma / (2 * (Math.pow(a, 0.5))) - A.getCp() * r * A.getK() * Math.exp(-1 * r * a) * normcdf(A.getCp() * d_2)) / 365);
        A.setVega(normpdf(A.getCp() * d_1) / (S0 * sigma * Math.pow(a, 0.5)));
        A.setTheta((S0 * (Math.pow(a, 0.5)) * normpdf(A.getCp() * d_1)) / 100);
        A.setRho((A.getK() * A.getCp() * a * Math.exp(-1 * r * a) * normcdf(A.getCp() * d_2)) / 100);
    }
    */
    private double betaValue(double delta, double price){
        return S0 * delta / price;
    }

    private double[] Interest(int cp, double k, double price) {
        int n = S.length;
        double[] C = new double[n];
        if (t != 0) {
            for (int i = 0; i < n; i++) {
                double temp = Options(cp, k);
                C[i] = 10000 * (temp - price);
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

    private double Expected(double[] C_new){
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

    private double goalValue(int num, double E, double M, double beta, double min_beta, double max_beta, double max_numE, double min_numE){
        return w1 * ((num * E) / M - min_numE / M) / (max_numE / M - min_numE / M) +
                w2 * (max_beta - beta) / (max_beta - min_beta);
    }

    @Override
    public ResponseMsg recommendPortfolio(double M0, double k, String T, char combination, double p1, double p2, double sigma1, double sigma2, int w1, int w2) {
        //参数都是由用户输入的
        /*用户输入数据*/
        //托管资金总额
        this.T = T;
        this.p1 = p1;
        this.p2 = p2;
        this.sigma1 = sigma1;
        this.sigma2 = sigma2;
        this.w1 = w1 / 100.0;
        this.w2 = w2 / 100.0;

        /*实时数据的获取*/
        try {
            this.upDataFromNet();
        } catch (IOException e) {
            return new ResponseMsg(2000, "实时数据错误", null);
        }
        parShallowDeep();
        /*货币基金与衍生品组合分配：*/
        this.M = M0 * (r * Math.ceil(t / 30.0) / 12 + k) / (1 + r * Math.ceil(t / 30.0) / 12);

        List<structD> D = firstStep(combination);
        structD maxGoalD;
        maxGoalD = secondStep(D);
        List<Double> profits = thirdStep(maxGoalD, combination);
        String[] StringProfits = new String[profits.size()];
        for(int i = 0; i < profits.size();i++){
            StringProfits[i] = Double.toString(profits.get(i));
        }
        String[][] graph = new String[][]{month, StringProfits};
        RecommendOption1 recommendOption1 = new RecommendOption1(maxGoalD.optionCombination, maxGoalD.buyAndSell, maxGoalD.num, maxGoalD.p0,
                maxGoalD.pb, maxGoalD.z_delta, maxGoalD.z_gamma, maxGoalD.z_vega, maxGoalD.z_theta, maxGoalD.z_rho, maxGoalD.E / M, maxGoalD.beta,graph );
        return new ResponseMsg(2000, "recommend Portfolio finished", recommendOption1);
    }

    //期权组合第一步，计算出集合D及其相关内容
    private List<structD> firstStep(char choose){
        List<Option[]> buyAndSellOptions = new ArrayList<>();
        List<Integer> buyAndSell = new ArrayList<>();
        //region 选择买入及卖出的期权及其个数
        switch (choose){
            //买入两个高价看涨期权，卖出一个低价看涨期权，使得delta=0；
            case 'A':buyAndSell.add(2);buyAndSell.add(-1);
                buyAndSellOptions.add(chigh.get(T).toArray(new Option[0]));
                buyAndSellOptions.add(clow.get(T).toArray(new Option[0]));
                break;

            //同时买入平价看涨和平价看跌
            case 'B':buyAndSell.add(1);buyAndSell.add(1);
                buyAndSellOptions.add(cpar.toArray(new Option[0]));
                buyAndSellOptions.add(ppar.toArray(new Option[0]));
                break;

            //买入两支低价看跌期权，卖出一支高价看跌期权；
            case 'C':buyAndSell.add(2);buyAndSell.add(-1);
                buyAndSellOptions.add(plow.get(T).toArray(new Option[0]));
                buyAndSellOptions.add(phigh.get(T).toArray(new Option[0]));
                break;

            //买入低价看涨期权，卖出高价看涨期权
            case 'D':buyAndSell.add(1);buyAndSell.add(-1);
                buyAndSellOptions.add(clow.get(T).toArray(new Option[0]));
                buyAndSellOptions.add(chigh.get(T).toArray(new Option[0]));
                break;

            //买入高价看跌期权，卖出低价看跌期权
            case 'E':buyAndSell.add(1);buyAndSell.add(-1);
                buyAndSellOptions.add(phigh.get(T).toArray(new Option[0]));
                buyAndSellOptions.add(plow.get(T).toArray(new Option[0]));
                break;

            //卖出一个低价看跌期权，买入两个高价看跌期权
            case 'F':buyAndSell.add(2);buyAndSell.add(-1);
                buyAndSellOptions.add(phigh.get(T).toArray(new Option[0]));
                buyAndSellOptions.add(plow.get(T).toArray(new Option[0]));
                break;

            //卖出平价看涨期权，同时卖出平价看跌期权
            case 'G':buyAndSell.add(-1);buyAndSell.add(-1);
                buyAndSellOptions.add(cpar.toArray(new Option[0]));
                buyAndSellOptions.add(ppar.toArray(new Option[0]));
                break;

            //卖出两个高价看涨期权，买入一个低价看涨期权
            case 'H':buyAndSell.add(1);buyAndSell.add(-2);
                buyAndSellOptions.add(clow.get(T).toArray(new Option[0]));
                buyAndSellOptions.add(chigh.get(T).toArray(new Option[0]));
                break;

            default:break;
        }
        //endregion

        List<structD> D = new ArrayList<>();
        List<Option> array = new ArrayList<>();
        int []buySell = new int[buyAndSell.size()];
        for(int i = 0;i < buyAndSell.size();i++)
            buySell[i] = buyAndSell.get(i);
        generateD(D, buyAndSellOptions, 0, buySell, array, choose, 1);
//        generateD(D, sellOptions, sell, buyOptions, buy, choose);
        //region B D E G第一步有两种构造方式，两种构造方式得到的组合都放入D中
        if(choose == 'B' || choose == 'D' || choose == 'E' || choose == 'G'){
            buyAndSell.clear();
            buyAndSellOptions.clear();
            switch (choose){
                //买入一个高价看涨和低价看跌
                case 'B':buyAndSell.add(1);buyAndSell.add(1);
                    buyAndSellOptions.add(chigh.get(T).toArray(new Option[0]));
                    buyAndSellOptions.add(plow.get(T).toArray(new Option[0]));
                    break;

                //买入低价看跌，卖出高价看跌
                case 'D':buyAndSell.add(1);buyAndSell.add(-1);
                    buyAndSellOptions.add(plow.get(T).toArray(new Option[0]));
                    buyAndSellOptions.add(phigh.get(T).toArray(new Option[0]));
                    break;

                //买入高价看涨，卖出低价看涨
                case 'E':buyAndSell.add(1);buyAndSell.add(-1);
                    buyAndSellOptions.add(chigh.get(T).toArray(new Option[0]));
                    buyAndSellOptions.add(clow.get(T).toArray(new Option[0]));
                    break;

                //卖出高价看涨，卖出低价看跌
                case 'G':buyAndSell.add(1);buyAndSell.add(-1);
                    buyAndSellOptions.add(chigh.get(T).toArray(new Option[0]));
                    buyAndSellOptions.add(plow.get(T).toArray(new Option[0]));
                    break;

                default:break;
            }
            buySell = new int[buyAndSell.size()];
            for(int i = 0;i < buyAndSell.size();i++)
                buySell[i] = buyAndSell.get(i);
            generateD(D, buyAndSellOptions, 0, buySell, array, choose, 2);
        }
        //endregion

        //region G四种构造方式
        if(choose == 'G'){
            buyAndSell.clear();
            buyAndSellOptions.clear();
            //卖出平价看涨和平价看跌，买入高价看涨和低价看跌
            buyAndSell.add(-1);
            buyAndSell.add(-1);
            buyAndSell.add(1);
            buyAndSell.add(1);
            buyAndSellOptions.add(cpar.toArray(new Option[0]));
            buyAndSellOptions.add(ppar.toArray(new Option[0]));
            buyAndSellOptions.add(chigh.get(T).toArray(new Option[0]));
            buyAndSellOptions.add(plow.get(T).toArray(new Option[0]));
            buySell = new int[buyAndSell.size()];
            for(int i = 0;i < buyAndSell.size();i++)
                buySell[i] = buyAndSell.get(i);
            generateD(D, buyAndSellOptions, 0, buySell, array, choose, 3);

            //卖出浅度高价看涨和浅度低价看跌，同时买入深度高价看涨和深度低价看跌期权
            buyAndSell.clear();
            buyAndSellOptions.clear();
            buyAndSell.add(-1);
            buyAndSell.add(-1);
            buyAndSell.add(1);
            buyAndSell.add(1);
            buyAndSellOptions.add(chighShallow.toArray(new Option[0]));
            buyAndSellOptions.add(plowShallow.toArray(new Option[0]));
            buyAndSellOptions.add(chighDeep.toArray(new Option[0]));
            buyAndSellOptions.add(plowDeep.toArray(new Option[0]));
            buySell = new int[buyAndSell.size()];
            for(int i = 0;i < buyAndSell.size();i++)
                buySell[i] = buyAndSell.get(i);
            generateD(D, buyAndSellOptions, 0, buySell, array, choose, 4);
        }
        //endregion
        return D;
    }

    //根据不同期权组合，生成集合D 1.0 不能适合G的三四
    private void generateD(List<structD> D, Option[] sellOptions, int sell,Option[] buyOptions, int buy,char choose){
        for (Option aSellOption : sellOptions) {
            double i_delta = aSellOption.getDelta();
            double i_gamma = aSellOption.getGamma();
            double i_vega = aSellOption.getVega();
            double i_theta = aSellOption.getTheta();
            double i_rho = aSellOption.getRho();
            double i_price = aSellOption.getPrice2();
            //对于B，G里面有全买和全卖的情况
            if(sell > 0)
                i_price = aSellOption.getPrice1();
            double i_yclose = aSellOption.getYclose();
            double i_k = aSellOption.getK();
            for (Option aBuyOption : buyOptions) {
                double j_delta = aBuyOption.getDelta();
                double j_gamma = aBuyOption.getGamma();
                double j_vega = aBuyOption.getVega();
                double j_theta = aBuyOption.getTheta();
                double j_rho = aBuyOption.getRho();
                double j_price = aBuyOption.getPrice1();
                double j_k = aBuyOption.getK();
                double j_yclose = aBuyOption.getYclose();
                //对于B，G里面有全买和全卖的情况
                if(buy < 0)
                    j_price = aBuyOption.getPrice2();

                //不同选择有不同的判断进入集合D的条件
                //i是卖出的，j是买入的()
                //TODO：G的三四没有考虑
                boolean isInD;
                if(choose == 'D' || choose == 'E')
                    isInD = true;
                else if(choose == 'H')
                    isInD = Math.abs(buy * j_delta + sell * i_delta) < zero;
                else
                    isInD = Math.abs(buy * j_delta + sell * i_delta) < zero && buy * j_vega + sell * i_vega > 0;

                if(isInD){
                    double p0 = 0;
                    double pb = 0;
                    if(buy > 0)
                        p0 += buy * j_price;
                    else{
                        if(aBuyOption.getCp() == -1)
                            pb += Math.abs(buy) * Math.min(j_k, j_price + Math.max((0.12 * j_yclose - (j_yclose - j_price)),0.07 * j_k));
                        else
                            pb += Math.abs(buy) * (j_price + Math.max((0.12 * j_yclose - (j_yclose - j_k)),0.07 * j_yclose));
                    }

                    if(sell > 0)
                        p0 += sell * i_price;
                    else{
                        if(aSellOption.getCp() == -1)
                            pb = Math.abs(sell) * Math.min(i_k, i_price + Math.max((0.12 * i_yclose - (i_yclose - i_price)),0.07 * i_k));
                        else
                            pb = Math.abs(sell) * (i_price + Math.max((0.12 * i_yclose - (i_yclose - i_k)),0.07 * i_yclose));
                    }
                    p0 = p0 + pb;

                    structD d = new structD();
                    d.p0 = p0;
                    d.pb = pb;
                    d.z_delta = buy * j_delta + sell * i_delta;
                    if(choose == 'A'|| choose == 'B')
                        d.z_delta = 0;
                    d.z_gamma= buy * j_gamma + sell * i_gamma;
                    d.z_vega= buy * j_vega + sell * i_vega;
                    d.z_theta= buy * j_theta + sell * i_theta;
                    d.z_rho= buy * j_rho + sell * i_rho;
                    d.optionCombination = new Option[]{aBuyOption, aSellOption};
                    d.buyAndSell = new int[]{buy, sell};
                    D.add(d);
                }
            }
        }
    }

    //根据不同期权组合，生成集合D 2.0
    private void generateD(List<structD> D, List <Option[]> buyAndSellOptions, int index, int[] buyAndSell, List<Option> array, char choose, int rank){
        if(index == buyAndSellOptions.size()){
            double sumDelta = 0;
            double sumVega = 0;
            for(int i = 0 ;i < array.size(); i++){
                sumDelta += buyAndSell[i] * array.get(i).getDelta();
                sumVega += buyAndSell[i] * array.get(i).getVega();
            }
            boolean isInD;
            if(choose == 'D' || choose == 'E')
                isInD = true;
            else if(choose == 'H')
                isInD = Math.abs(sumDelta) < zero;
            else
                isInD = Math.abs(sumDelta) < zero && sumVega > 0;
            if(isInD){
                double p0 = 0,pb = 0;
                for(int i = 0;i < buyAndSell.length; i++){
                    Option one = array.get(i);
                    if(buyAndSell[i] > 0){
                        p0 += buyAndSell[i] * one.getPrice1();
                    }
                    else{
                        if(one.getCp() == -1)
                            pb = Math.abs(buyAndSell[i]) * Math.min(one.getK(), one.getPrice2() + Math.max((0.12 * one.getYclose() - (one.getYclose() - one.getPrice2())),0.07 * one.getK()));
                        else
                            pb = Math.abs(buyAndSell[i]) * (one.getPrice2() + Math.max((0.12 * one.getYclose() - (one.getYclose() - one.getK())),0.07 * one.getYclose()));
                    }
                }
                p0 += pb;

                structD d = new structD();
                d.p0 = p0;
                d.pb = pb;
                double sumGamma = 0;
                double sumTheta = 0;
                double sumRho = 0;
                for(int i = 0 ;i < array.size(); i++){
                    sumGamma += buyAndSell[i] * array.get(i).getGamma();
                    sumTheta += buyAndSell[i] * array.get(i).getTheta();
                    sumRho += buyAndSell[i] * array.get(i).getRho();
                }
                d.z_delta = sumDelta;
                if(choose == 'A'|| choose == 'B')
                    d.z_delta = 0;
                d.z_gamma = sumGamma;
                d.z_rho = sumRho;
                d.z_theta = sumTheta;
                d.z_vega = sumVega;
                d.optionCombination = array.toArray(new Option[0]);
                d.buyAndSell = buyAndSell;
                d.rank = rank;
                D.add(d);
            }
        }
        else{
            for(Option option:buyAndSellOptions.get(index)){
                array.add(option);
                generateD(D, buyAndSellOptions, index + 1, buyAndSell, array, choose, rank);
                array.remove(index);
            }
        }
    }

    //期权组合第二步，在集合D中寻找goal值最高的组合
    private structD secondStep(List<structD> D){
        /*第二步
         * 1.D中的 optionCombination 是一个期权组合
         * 2.buyAndSell是与上面对应的买卖的个数，买为正，卖为负
         * 3.这个步骤对所有组合都适应
         * */
        double max_numE = Double.MIN_VALUE;
        double min_numE = Double.MAX_VALUE;
        double max_beta = Double.MIN_VALUE;
        double min_beta = Double.MAX_VALUE;
        for (structD z : D) {
            z.num = (int) (M / z.p0);
            if(max_numE < z.num){
                max_numE = z.num;
            }
            if(min_numE > z.num){
                min_numE = z.num;
            }
            List <double[]> C = new ArrayList<>();
            for(int i = 0;i < z.buyAndSell.length;i++){
                double price = z.optionCombination[i].getPrice1();
                if(z.buyAndSell[i] < 0)
                    price = z.optionCombination[i].getPrice2();
                double[] C_i =Interest(z.optionCombination[i].getCp(), z.optionCombination[i].getK(), price);
                C.add(C_i);
            }
            double[] C_new = new double[C.get(0).length];
            for(int i = 0;i < C.get(0).length; i++)
            {
                C_new[i] = 0;
                for(int j = 0; j < C.size(); j++){
                    C_new[i] += z.buyAndSell[j] * C.get(j)[i];
                }
            }
            z.E = Expected(C_new);
            z.beta = 0;
            for(int i = 0;i < z.buyAndSell.length; i++){
                double price = z.optionCombination[i].getPrice1();
                if(z.buyAndSell[i] < 0)
                    price = z.optionCombination[i].getPrice2();
                z.beta += z.buyAndSell[i] * betaValue(z.optionCombination[i].getDelta(), price);
            }
            if(z.beta > max_beta)
                max_beta = z.beta;
            if(z.beta < min_beta)
                min_beta = z.beta;
        }
        for (structD z : D) {
            z.goal = goalValue(z.num, z.E, this.M, z.beta, min_beta, max_beta, max_numE, min_numE);
        }
        //对 goal 排序(从高到低)
        D.sort((o1, o2) -> Double.compare(o2.goal, o1.goal));
        return D.get(0);
    }

    //期权组合第三步，回测
    private List <Double> thirdStep(structD goalD, char choose){
        //看涨为true，看跌为false
        List <Boolean> cOrP = new ArrayList<>();
        //0为low，1为high，2为par，3为highShallow，4为lowShallow，5为highDeep，6为lowDeep
        List <Integer> lowOrHigh = new ArrayList<>();
        //region 先根据choose和goalD判断cp 和 low, high
        switch (choose){
            case 'A':cOrP.add(true);cOrP.add(true);
                lowOrHigh.add(1);lowOrHigh.add(0);
                break;

            case 'B':
                switch (goalD.rank){
                    case 1:cOrP.add(true);cOrP.add(false);
                        lowOrHigh.add(2);lowOrHigh.add(2);
                        break;
                    case 2:cOrP.add(true);cOrP.add(false);
                        lowOrHigh.add(1);lowOrHigh.add(0);
                }
                break;


            case 'C':cOrP.add(false);cOrP.add(false);
                lowOrHigh.add(0);lowOrHigh.add(1);
                break;

            case 'D':switch (goalD.rank){
                case 1:cOrP.add(true);cOrP.add(true);
                    lowOrHigh.add(0);lowOrHigh.add(1);
                    break;
                case 2:cOrP.add(false);cOrP.add(false);
                    lowOrHigh.add(0);lowOrHigh.add(1);
            }
                break;

            case 'E':switch (goalD.rank){
                case 1:cOrP.add(false);cOrP.add(false);
                    lowOrHigh.add(1);lowOrHigh.add(0);
                    break;
                case 2:cOrP.add(true);cOrP.add(true);
                    lowOrHigh.add(1);lowOrHigh.add(0);
            }
                break;

            case 'F':cOrP.add(false);cOrP.add(false);
                lowOrHigh.add(1);lowOrHigh.add(0);
                break;
                
            case 'G':switch (goalD.rank){
                case 1:cOrP.add(true);cOrP.add(false);
                    lowOrHigh.add(2);lowOrHigh.add(2);
                    break;
                case 2:cOrP.add(true);cOrP.add(false);
                    lowOrHigh.add(1);lowOrHigh.add(0);
                    break;
                case 3:cOrP.add(true);cOrP.add(false);cOrP.add(true);cOrP.add(false);
                    lowOrHigh.add(2);lowOrHigh.add(2);lowOrHigh.add(1);lowOrHigh.add(0);
                    break;
                    //0为low，1为high，2为par，3为highShallow，4为lowShallow，5为highDeep，6为lowDeep
                case 4:cOrP.add(true);cOrP.add(false);cOrP.add(true);cOrP.add(false);
                    lowOrHigh.add(3);lowOrHigh.add(4);lowOrHigh.add(5);lowOrHigh.add(6);
                    break;

            }
                break;
            case 'H':break;
            default:break;
        }//endregion

        Calendar c= Calendar.getInstance();
        int nowYear = c.get(Calendar.YEAR);
        int nowMonth = c.get(Calendar.MONTH) + 1;
        int nowDay = c.get(Calendar.DATE);

        List <Double> profits = new ArrayList<>();
        int numberOfDot = 1;
        for(String m: month){
            String[] str = m.split("-");
            int year = Integer.parseInt(str[0]);                //得到回测年月
            int month = Integer.parseInt(str[1]);

            int difference = calculateDifference(nowYear, nowMonth, nowDay);  //得到今天距离最近的第四个星期三所差的天数
            String startDate = calculateDate(year, month, difference);       //得到回测月的起始日期
            int stage = calculateFirstFew(T);                            //得到是在第几个阶段
            String endDate = calculateBackTestExpiryDate(startDate, stage);//得到回测的终止日期

            List <OptionTsd[]> backTestData = new ArrayList<>();
            //region根据条件筛选backTestData
            for(int i = 0; i < cOrP.size();i++){
                List<OptionTsd> one = new ArrayList<>();
                if(lowOrHigh.get(i) == 0 || lowOrHigh.get(i) == 1){
                    one = optionTsdDataService.complexFind(startDate, endDate, cOrP.get(i), lowOrHigh.get(i));
                }
                else
                {
                    switch (lowOrHigh.get(i)){
                        case 2:one = optionTsdDataService.complexFindRange(startDate, endDate, cOrP.get(i), -1*eps, eps);
                            break;
                        case 3:one = optionTsdDataService.complexFindRange(startDate, endDate, cOrP.get(i), 0, 2*eps);
                            break;
                        case 4:one = optionTsdDataService.complexFindRange(startDate, endDate, cOrP.get(i), -2 * eps, 0);
                            break;
                        case 5:one = optionTsdDataService.complexFindRange(startDate, endDate, cOrP.get(i), 2 * eps, 5);
                            break;
                        case 6:one = optionTsdDataService.complexFindRange(startDate, endDate, cOrP.get(i), -5, -2 * eps);
                            break;
                    }
                }
                backTestData.add(one.toArray(new OptionTsd[0]));
            }//endregion
            List<OptionTsd> array = new ArrayList<>();
            backTest(backTestData, 0, array, profits, goalD);
            while(profits.size() != numberOfDot){
                logger.info("warning : 期权组合回测出现问题");
                if(profits.size() < numberOfDot){
                    profits.add(0.0);
                }
                else {
                    profits.remove(profits.size() - 1);
                }
            }
            numberOfDot ++;
        }
        return profits;
    }

    private void backTest(List<OptionTsd[]> data, int index, List<OptionTsd> array, List <Double> profits, structD goal){
        if(index == data.size()){
            boolean flag = true;
            for(int i = 0;i < array.size();i++){
                double backK = optionBasicInfoDataService.findByCodeName(array.get(i).getCodeName()).getPrice();
                double backS0 = timeSeriesDataSerice.findByLastTradeDate(array.get(i).getLatestDate()).getClosePrice();
                flag = flag && ((backK - backS0) - (goal.optionCombination[i].getK() - S0) <= eps);
            }
            double profit = 0;
            if(flag){
                for(int i = 0;i < array.size();i++){
                    double backPrice = array.get(i).getClosePrice();
                    double backClose = optionTsdDataService.findByCodeNameAndLatestDate(array.get(i).getCodeName(), optionBasicInfoDataService.findByCodeName(array.get(i).getCodeName()).getEndDate()).getClosePrice();

                    profit += goal.buyAndSell[i] * (backClose - backPrice);
                }
                profits.add(profit);
            }
        }
        else {
            for(OptionTsd each:data.get(index)){
                array.add(each);
                backTest(data, index + 1, array, profits, goal);
                array.remove(index);
            }
        }
    }

    @Override
    public ResponseMsg hedging(int N0, double a, double sExp, String T) {
        try {
            upDataFromNet();
        } catch (IOException e) {
            e.printStackTrace();
            return StatusMsg.IOExceptionOccurs;
        }
        Option[] plowT = new Option[plow.get(T).size()];
        Option[] phighT = new Option[phigh.get(T).size()];
        this.plow.get(T).toArray(plowT);
        this.phigh.get(T).toArray(phighT);
        int N = (int) (N0*a);
        double pAsset = lastestOptionPrice;
        //第一步
        Option[] ListD1 = calculateD(plowT,sExp,N,pAsset);
        Option[] ListD2 = calculateD(phighT,sExp,N,pAsset);

        //第二步
        Option i1 = maxLoss(ListD1,sExp,N,pAsset);
        Option i2 = maxLoss(ListD2,sExp,N,pAsset);
        double iK1 = i1.getK();
        double iK2 = i2.getK();
        double iK;
        Option optionI;
        boolean flag;  //判断是第一种还是第二种情况
        if(iK1>iK2){ iK=iK2; optionI = i2; flag = false;}
        else { iK = iK1; optionI = i1; flag = true;}

        //第三步
        String[][] rtn;
        if(flag) rtn = hedgingBackTest(0,N,iK,pAsset,T);
        else rtn = hedgingBackTest(1,N,iK,pAsset,T);
        RecommendOption2 recommendOption2 = new RecommendOption2(optionI, iK, rtn);
        return new ResponseMsg(0, "Hedging success", recommendOption2);
    }

    private Option[] calculateD(Option[] list,double sExp,int N,double pAsset){
        ArrayList<Option> D = new ArrayList<>();
        for (Option i : list) {
            double iK = i.getK();
            if (iK > sExp) {
                double iDelta = i.getDelta();
                double iPrice1 = i.getPrice1();
                int iNum = (int)Math.ceil(N /(10000*Math.abs(iDelta)));
                if((N * (pAsset - sExp)) > (((N * pAsset) - (((iNum * 10000) - N) * (iK - sExp)) - (N * iK)) + (iNum * 10000 * iPrice1))){
                    D.add(i);
                }
            }
        }
        return D.toArray(new Option[0]);
    }

    private Option maxLoss(Option[] ListD,double sExp, int N, double pAsset){
        double cost;
        double maxLoss = Double.MAX_VALUE;
        Option rtn=null;
        for(Option i:ListD){
            double iK = i.getK();
            double iDelta = i.getDelta();
            double iPrice1 = i.getPrice1();
            int iNum = (int)Math.ceil(N /(10000*Math.abs(iDelta)));
            cost = iNum*10000*iPrice1;
            double temp = ((N * pAsset) - (((iNum * 10000) - N) * (iK - sExp)) - (N * iK)) + cost;
            if(temp<maxLoss){
                maxLoss = temp;
                rtn = i;
            }
        }

        return rtn;
    }

    private String[][] hedgingBackTest(int findType, int N, double iK, double pAsset, String T){
        Calendar c= Calendar.getInstance();
        int nowYear = c.get(Calendar.YEAR);
        int nowMonth = c.get(Calendar.MONTH)+1;
        int nowDay = c.get(Calendar.DATE);


        String[][] rtn = new String[4][36]; //返回四行的数组
        int index = 0; //用于计算数组的角标
        for (String m : month) {
            String[] str = m.split("-");
            int year = Integer.parseInt(str[0]);                //得到回测年月
            int month = Integer.parseInt(str[1]);

            int difference = calculateDifference(nowYear, nowMonth, nowDay);  //得到今天距离最近的第四个星期三所差的天数
            String startDate = calculateDate(year, month, difference);       //得到回测月的起始日期
            int stage = calculateFirstFew(T);                            //得到是在第几个阶段
            String endDate = calculateBackTestExpiryDate(startDate, stage);//得到回测的终止日期

            List<OptionTsd> backTestOptions = optionTsdDataService.complexFind(startDate, endDate, false, findType);//0代表low 1代表high,找到符合条件的期权列表


            TimeSeriesData ETF50findByLastTradeDate = timeSeriesDataSerice.findByLastTradeDate(startDate);
            double assetClose1 = ETF50findByLastTradeDate.getClosePrice();    //查询ETF50timeSeries得到起始日和结束日的收盘价
            ETF50findByLastTradeDate = timeSeriesDataSerice.findByLastTradeDate(endDate);
            double assetClose2 = ETF50findByLastTradeDate.getClosePrice();

            double totalLoss = 0;
            if (backTestOptions.isEmpty()) {
                continue;
            }
            else{
                for (OptionTsd backTestI : backTestOptions) {
                    String backTestOptionCodeName = backTestI.getCodeName();    //得到期权代码
                    OptionBasicInfo secondSearchByCodeName = optionBasicInfoDataService.findByCodeName(backTestOptionCodeName); //第二次查询得到i'_k行权价格,得到这一行信息
                    double backTestK = secondSearchByCodeName.getPrice();    //得到i'_k

                    double backTestClose1 = backTestI.getClosePrice();      //起始日期权收盘价
                    OptionTsd endDateMsg = optionTsdDataService.findByCodeNameAndLatestDate(backTestOptionCodeName, endDate);
                    double backTestClose2 = endDateMsg.getClosePrice();    //结束日期权收盘价


                    if ((iK - pAsset) - (backTestK - assetClose1) <= eps) {
                        double backTestIDelta = backTestI.getDelta();
                        double backTestINum = (int) (N / (10000 * Math.abs(backTestIDelta))) + 1;
                        if (assetClose2 < backTestK) {
                            totalLoss = N * backTestClose1 + (backTestINum * 10000 - N) * (backTestClose1 - backTestClose2) + N * (assetClose1 - backTestK);
                        } else {
                            totalLoss = backTestClose1 * backTestINum * 10000 + N * (assetClose1 - assetClose2);
                        }
                    }
                }
            }

            double holdDouble = N*(assetClose1-assetClose2);
            double unholdDouble = totalLoss;
            double lossDifferenceDouble = unholdDouble - holdDouble;

            String hold = Double.toString(holdDouble);
            String unhold = Double.toString(totalLoss);
            String lossDifference = Double.toString(lossDifferenceDouble);

            rtn[0][index] = m;
            rtn[1][index] = hold;
            rtn[2][index] = unhold;
            rtn[3][index] = lossDifference;
            index++;
        }
        return rtn;
    }

    @Override
    public ResponseMsg customPortfolio(Option[] list) {
        return null;
    }

    @Scheduled(initialDelay = 1000, fixedRate = 3 * 1000)
    public void task() {
        test();
    }
}
