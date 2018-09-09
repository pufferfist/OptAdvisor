package utf8.citicup.serviceImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import utf8.citicup.dataService.PortfolioDataService;
import utf8.citicup.dataService.UserDataService;
import utf8.citicup.dataService.historyDataService.OptionBasicInfoDataService;
import utf8.citicup.dataService.historyDataService.OptionTsdDataService;
import utf8.citicup.dataService.historyDataService.TimeSeriesDataSerice;
import utf8.citicup.domain.entity.*;
import utf8.citicup.domain.historyEntity.OptionBasicInfo;
import utf8.citicup.domain.historyEntity.OptionTsd;
import utf8.citicup.domain.historyEntity.TimeSeriesData;
import utf8.citicup.service.RecommendService;
import utf8.citicup.service.util.GetData;
import utf8.citicup.service.util.PolySms;
import utf8.citicup.service.util.StatusMsg;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static utf8.citicup.domain.common.Type.RECOMMEND_PORTFOLIO;
import static utf8.citicup.service.util.StatusMsg.noEligibleOptions;

@Service
public class RecommendServiceImpl implements RecommendService {

    private static final double eps = 0.1;
    private static final double zero = 0.1;
    /*用户输入值*/
    private String T;//投资者预测价格有效时间
    private double M0;
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

    /*判断是否正在获取网络信息*/
    private boolean isUpdataRunning;

    String[] getMonth() {
        return month;
    }

    private String[] month={"2015-3","2015-4","2015-5","2015-6","2015-7","2015-8","2015-9","2015-10","2015-11","2015-12","2016-1","2016-2","2016-3","2016-4","2016-5","2016-6","2016-7","2016-8","2016-9","2016-10","2016-11","2016-12","2017-1","2017-2","2017-3","2017-4","2017-5","2017-6","2017-7","2017-8","2017-9","2017-10","2017-11","2017-12","2018-1","2018-2","2018-3"};
//    private String[] month={"2015-3","2015-4","2015-5","2018-2"};

    /*计算得到的值*/

    private double M;
    private Logger logger = LoggerFactory.getLogger(RecommendService.class);

    /*回测数据库*/
    @Autowired
    private OptionTsdDataService optionTsdDataService;
    @Autowired
    private OptionBasicInfoDataService optionBasicInfoDataService;
    @Autowired
    private TimeSeriesDataSerice timeSeriesDataSerice;
    @Autowired
    private PortfolioDataService portfolioDataService;
    @Autowired
    private UserDataService userDataService;

    /*新用到的结构类型*/
    class structD{
        Option[] optionCombination;//组合中的期权
        int[] buyAndSell;//对应的买入还是卖出
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
        double returnOnAssets;
    }

    void setP1(double p1) {
        this.p1 = p1;
    }

    void setP2(double p2) {
        this.p2 = p2;
    }

    void setSigma1(double sigma1) {
        this.sigma1 = sigma1;
    }

    void setSigma2(double sigma2) {
        this.sigma2 = sigma2;
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
        Calendar c = Calendar.getInstance();
        c.set(year,month,0);
        return c.get(Calendar.DAY_OF_MONTH);

    }

    //计算是第几个阶段的期权
    private int calculateFirstFew(String T){
        return Arrays.asList(expiredMonths).indexOf(T) + 1;
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
        S = new double[5001];
        int i = 0;
        double temp = 0.0;
        DecimalFormat df = new DecimalFormat("0.000");
        while(temp <= 5.0){
            temp = Double.valueOf(df.format(temp));
            S[i] = temp;
            temp += 0.001;
            i++;
        }
        dv = 0;//股票分红率
        M = 10000;
        this.isUpdataRunning = true;
    }

    /*从网络获取所需的数据*/
    public void upDataFromNet() throws IOException {
        chigh.clear();
        clow.clear();
        phigh.clear();
        plow.clear();
        r = dataSource.get_r();
//        S0 = dataSource.get_S0();//实时标的价格
        sigma = dataSource.get_Sigma();//实时波动率
        lastestOptionPrice = dataSource.get_LatestPrice();
        S0 = lastestOptionPrice;
        expiredMonths = dataSource.get_T();
        for (String expiredMonth : expiredMonths) {
            if (expiredMonth != null) {
                //region 根据行权名称得到相关数据
                chigh.put(expiredMonth, new ArrayList<>());
                clow.put(expiredMonth, new ArrayList<>());
                phigh.put(expiredMonth, new ArrayList<>());
                plow.put(expiredMonth, new ArrayList<>());

                String[][] Optionss = dataSource.get_contract(expiredMonth);
                String expireTime = dataSource.get_expireAndremainder(expiredMonth)[0];
//            System.out.println(expiredMonth);
                for (int i = 0; i < 2; i++) {
                    for (String cpOption : Optionss[i]) {
                        int cp;
                        if (i == 0)
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
                        double realTimePrice = attributes[8];

                        Option newOption = new Option();
                        newOption.setOptionCode(cpOption);
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
                        newOption.setRealTimePrice(realTimePrice);

                        String[] nameAndCode = dataSource.getShortNameAndCodeName(newOption.getOptionCode());
                        newOption.setTradeCode(nameAndCode[1]);
                        newOption.setName(nameAndCode[0]);
//                    GreekCharacteValue(newOption, Integer.parseInt(dataSource.get_expireAndremainder(expiredMonth)[1]));
                        //endregion

                        //region 判断cp 和 low high
                        if (cp == 1) {
                            if (k >= this.lastestOptionPrice) {
                                chigh.get(expiredMonth).add(newOption);
                            } else {
                                clow.get(expiredMonth).add(newOption);
                            }
                        } else {
                            if (k >= this.lastestOptionPrice) {
                                phigh.get(expiredMonth).add(newOption);
                            } else {
                                plow.get(expiredMonth).add(newOption);
                            }
                        }
                        //endregion
                    }
                }
                //endregion
            }
        }
    }

    void setOptionAttributes(Option newOption) throws IOException {
        double[] attributes = dataSource.get_Attributes(newOption.getOptionCode());
        double k = attributes[0];
        double price1 = attributes[1];
        double price2 = attributes[2];
        double yclose = attributes[3];
        double delta = attributes[4];
        double gamma = attributes[5];
        double theta = attributes[6];
        double vega = attributes[7];
        double realTimePrice = attributes[8];

        newOption.setK(k);
        newOption.setPrice1(price1);
        newOption.setPrice2(price2);
        newOption.setYclose(yclose);
        newOption.setDelta(delta);
        newOption.setGamma(gamma);
        newOption.setTheta(theta);
        newOption.setVega(vega);
        newOption.setRealTimePrice(realTimePrice);
        //endregion
    }

    private void addAttributesToDOption(Option one) throws IOException {
        //添加期权名称、成交价、交易代码
        String[] nameAndCode = dataSource.getShortNameAndCodeName(one.getOptionCode());
        one.setTradeCode(nameAndCode[1]);
        one.setName(nameAndCode[0]);
        if(one.getType() < 0)
            one.setTransactionPrice(one.getPrice2());
        else
            one.setTransactionPrice(one.getPrice1());
    }

    private void parShallowDeep(){
        Option[] chigh_T = chigh.get(T).toArray(new Option[0]);
        Option[] clow_T = clow.get(T).toArray(new Option[0]);
        Option[] phigh_T = phigh.get(T).toArray(new Option[0]);
        Option[] plow_T = plow.get(T).toArray(new Option[0]);

        cpar.clear();
        ppar.clear();
        chighShallow.clear();
        plowShallow.clear();
        chighDeep.clear();
        plowDeep.clear();

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
        double theta = Math.pow(((sigma1 + sigma2) / 2), 2);
        return 1 / (Math.sqrt(2 * Math.PI * theta)) * Math.exp(-1 * Math.pow(x, 2) / (2 * theta));
    }

    private double normpdf(double x){
        //TODO: normpdf为标准正态分布的概率密度函数
        return 0;
    }

    private double Options(int cp, double S0, double k) {
        double t = this.t / 365.0;
        double d_1 = Math.log(S0 / k) + (r - dv + 0.5 * Math.pow((sigma1 + sigma2) / 2, 2) * t) / ((sigma1 + sigma2) / 2) / Math.sqrt(t);
        double d_2 = d_1 - (((sigma1 + sigma2) / 2) * (Math.sqrt(t)));
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
                double temp = Options(cp, S[i], k);
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
        RecommendOption1 recommendOption1;
        try {
            recommendOption1 = mainRecommendPortfolio(M0, k, T, combination, p1, p2, sigma1, sigma2, w1, w2);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseMsg(2000, "网络获取失败", null);
        }
        return new ResponseMsg(0, "recommend Portfolio finished", recommendOption1);
    }

    private RecommendOption1 mainRecommendPortfolio(double M0, double k, String T, char combination, double p1, double p2, double sigma1, double sigma2, int w1, int w2) throws IOException {
        //参数都是由用户输入的
        /*用户输入数据*/
        this.T = T;
        this.M0 = M0;
        this.p1 = p1;
        this.p2 = p2;
        this.sigma1 = sigma1 / 100.0;
        this.sigma2 = sigma2 / 100.0;
        this.w1 = w1 / 100.0;
        this.w2 = w2 / 100.0;
        k = k / 100.0;
        /*实时数据的获取*/

//        while (isUpdataRunning);

        if(isUpdataRunning){
            upDataFromNet();
            isUpdataRunning = false;
        }
        parShallowDeep();
        t = Integer.parseInt(dataSource.get_expireAndremainder(T)[1]);

        /*货币基金与衍生品组合分配：*/
        this.M = M0 * (r * Math.ceil(t / 30.0) / 12 + k) / (1 + r * Math.ceil(t / 30.0) / 12);

        List<structD> D = firstStep(combination);
        logger.info("first step is finished");
        System.out.println(D.size());
        structD maxGoalD;

        maxGoalD = secondStep(D);

        logger.info("second step is finished");
        for(int i = 0;i < maxGoalD.optionCombination.length;i++){
            maxGoalD.optionCombination[i].setType(maxGoalD.buyAndSell[i]);
            if(maxGoalD.optionCombination[i].getType() < 0)
                maxGoalD.optionCombination[i].setTransactionPrice(maxGoalD.optionCombination[i].getPrice2());
            else
                maxGoalD.optionCombination[i].setTransactionPrice(maxGoalD.optionCombination[i].getPrice1());
        }
        List<Double> profits = thirdStep(maxGoalD, combination);
        logger.info("third step back test finished");
        double[] assertOnReturns = assertReturns(maxGoalD, profits);
        String[] StringProfits = new String[profits.size()];
        String[] StringAssertOnReturns = new String[profits.size()];
        for(int i = 0; i < profits.size();i++){
            StringProfits[i] = Double.toString(profits.get(i));
            StringAssertOnReturns[i] = Double.toString(assertOnReturns[i]);
        }
        String[][] graph = new String[][]{month, StringProfits, StringAssertOnReturns};
        System.out.println(Arrays.deepToString(graph[0]));
        System.out.println(Arrays.deepToString(graph[1]));
        System.out.println(Arrays.deepToString(graph[2]));
        return new RecommendOption1(maxGoalD.optionCombination, maxGoalD.buyAndSell, maxGoalD.num, maxGoalD.p0,
                maxGoalD.pb, maxGoalD.z_delta, maxGoalD.z_gamma, maxGoalD.z_vega, maxGoalD.z_theta, maxGoalD.z_rho,
                maxGoalD.E / M, maxGoalD.returnOnAssets,maxGoalD.beta,graph, M0, k, sigma1, sigma2, p1, p2);
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

            //卖出2个低价看跌期权，买入1个高价看跌期权
            case 'F':buyAndSell.add(1);buyAndSell.add(-2);
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
                case 'G':buyAndSell.add(-1);buyAndSell.add(-1);
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
            else if(choose == 'G')
                isInD = Math.abs(sumDelta) < zero && sumVega < 0;
            else if(choose == 'F')
                isInD = sumVega < 0;
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
                p0 *= 10000;
                pb *= 10000;
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
            if(max_numE < z.num * z.E){
                max_numE = z.num * z.E;
            }
            if(min_numE > z.num * z.E){
                min_numE = z.num * z.E;
            }
            z.beta = 0;
            z.E *= z.num;
            for(int i = 0;i < z.buyAndSell.length; i++){
                double price = z.optionCombination[i].getPrice1();
                if(z.buyAndSell[i] < 0)
                    price = z.optionCombination[i].getPrice2();
                z.beta += z.buyAndSell[i] * betaValue(z.optionCombination[i].getDelta(), price);
            }
            z.beta = Math.abs(z.beta);
            if(z.beta > max_beta)
                max_beta = z.beta;
            if(z.beta < min_beta)
                min_beta = z.beta;
        }
        for (structD z : D) {
            if(z.E < 0)
                z.goal = 0;
            else
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
            case 'H':cOrP.add(true);cOrP.add(true);
                lowOrHigh.add(0);lowOrHigh.add(1);
                break;

            case 'W':
                for(Option each : goalD.optionCombination){
                    if(each.getCp() == 1)
                        cOrP.add(true);
                    else
                        cOrP.add(false);
                    if(each.getK() > S0)
                        lowOrHigh.add(1);
                    else
                        lowOrHigh.add(0);
                }
                break;
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
            int storeDifference = difference;
            boolean isEmpty = false;
            int offset = 0;
            do {
                if(profits.size() < numberOfDot) {
                   if(isEmpty){
//                        logger.info(m + " warning : 没找到数据");
                        if(storeDifference < 15)
                            difference++;
                        else
                            difference--;
                        offset++;
                    }
                    String startDate = calculateDate(year, month, difference);       //得到回测月的起始日期
                    int stage = calculateFirstFew(T);                            //得到是在第几个阶段

                    String endDate = calculateBackTestExpiryDate(startDate, stage);//得到回测的终止日期

                    List<OptionTsd[]> backTestData = new ArrayList<>();
                    //region根据条件筛选backTestData
                    for (int i = 0; i < cOrP.size(); i++) {
                        List<OptionTsd> one = new ArrayList<>();
                        if (lowOrHigh.get(i) == 0 || lowOrHigh.get(i) == 1) {
                            one = optionTsdDataService.complexFind(startDate, endDate, cOrP.get(i), lowOrHigh.get(i));
                        } else {
                            switch (lowOrHigh.get(i)) {
                                case 2:
                                    one = optionTsdDataService.complexFindRange(startDate, endDate, cOrP.get(i), -1 * eps, eps);
                                    break;
                                case 3:
                                    one = optionTsdDataService.complexFindRange(startDate, endDate, cOrP.get(i), 0, 2 * eps);
                                    break;
                                case 4:
                                    one = optionTsdDataService.complexFindRange(startDate, endDate, cOrP.get(i), -2 * eps, 0);
                                    break;
                                case 5:
                                    one = optionTsdDataService.complexFindRange(startDate, endDate, cOrP.get(i), 2 * eps, 5);
                                    break;
                                case 6:
                                    one = optionTsdDataService.complexFindRange(startDate, endDate, cOrP.get(i), -5, -2 * eps);
                                    break;
                            }
                        }
                        backTestData.add(one.toArray(new OptionTsd[0]));
                    }//endregion
                    List<OptionTsd> array = new ArrayList<>();
                    List<Boolean> flagArray = new ArrayList<>();
                    backTest(backTestData, 0, array, profits, goalD, flagArray);
                    if(offset >= 5 && profits.size()== numberOfDot - 1){
                        profits.add(0.0);
                    }
                }
                else{
//                    logger.info(m + " warning : 数据过多");
                    profits.remove(profits.size() - 1);
                }
                if(profits.size() == numberOfDot - 1){
                    isEmpty = true;
                }
            }while(profits.size() != numberOfDot);
            numberOfDot ++;
        }
        return profits;
    }

    private void backTest(List<OptionTsd[]> data, int index, List<OptionTsd> array, List <Double> profits, structD goal, List<Boolean> flagArray) {
        if(index == data.size()){
            boolean flag = true;
            for(int i = 0;i < array.size();i++){
                double backK = optionBasicInfoDataService.findByCodeName(array.get(i).getCodeName()).getPrice();//一定能得到
                double backS0 = timeSeriesDataSerice.findByLastTradeDate(array.get(i).getLatestDate()).getClosePrice();//
                flag = Math.abs(backK - backS0 - (goal.optionCombination[i].getK() - S0)) <= eps;
                if(!flag){
                    flagArray.set(i, false);
                    break;
                }
            }
            double profit = 0;
            if(flag){
                for(int i = 0;i < array.size();i++){
//                    double backK = optionBasicInfoDataService.findByCodeName(array.get(i).getCodeName()).getPrice();
//                    double backS0 = timeSeriesDataSerice.findByLastTradeDate(array.get(i).getLatestDate()).getClosePrice();
//                    double iK = goal.optionCombination[i].getK();
//                    double s0 = S0;

                    OptionTsd oneOptionTsd = optionTsdDataService.findByCodeNameAndLatestDate(array.get(i).getCodeName(),
                            optionBasicInfoDataService.findByCodeName(array.get(i).getCodeName()).getEndDate());//可能会找不到发生null
                    Double backPrice = array.get(i).getClosePrice();//有些日期没有收盘价，会报null错
                    Double backClose = backPrice;
                    if(oneOptionTsd != null){
                        backClose = oneOptionTsd.getClosePrice();
                    }
                    if(backClose != null && backPrice!= null){
                        profit += goal.buyAndSell[i] * (backClose - backPrice);
                    }
                    else{
                        profit += 0.0;
                    }
                }
                profits.add(profit * 10000);
            }
        }
        else {
            flagArray.add(true);
            for(OptionTsd each:data.get(index)){
                flagArray.set(index, true);
                array.add(each);
                backTest(data, index + 1, array, profits, goal, flagArray);
                array.remove(index);

                boolean flag = true;
                for(int i = 0;i < index;i++){
                    flag = flag && flagArray.get(i);
                }
                if(!flag)
                    break;
            }
            flagArray.remove(index);
        }
    }

    private double[] assertReturns(structD goal, List<Double> profit){
        goal.returnOnAssets = (goal.E + (M0 - M) * r * Math.ceil(t / 30.0) / 12) / M0;

        double[] assertGraph = new double[profit.size()];

        for(int i = 0; i < profit.size();i++){
            assertGraph[i] = profit.get(i) + (M0 - M) * r * Math.ceil(t / 30.0) / 12;
        }
        return assertGraph;
    }

    @Override
    public ResponseMsg hedging(int N0, double a, double sExp, String T) {

        RecommendOption2 recommendOption2;
        try {
            recommendOption2 = mainHedging(N0, a, sExp, T);
            if(recommendOption2 == null){
                return noEligibleOptions;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return StatusMsg.IOExceptionOccurs;
        }

        return new ResponseMsg(0, "Hedging success", recommendOption2);
    }

    private RecommendOption2 mainHedging(int N0, double a, double sExp, String T) throws IOException {
//        upDataFromNet();
        if(isUpdataRunning){
            upDataFromNet();
            isUpdataRunning = false;
        }
        this.sigma1 = this.sigma2 = this.sigma;
        this.T = T;
        upDataFromNet();
        Option[] plowT = new Option[plow.get(T).size()];
//        logger.info("plowT的长度是" + String.valueOf(plowT.length));
        Option[] phighT = new Option[phigh.get(T).size()];
        this.plow.get(T).toArray(plowT);
        this.phigh.get(T).toArray(phighT);
        int N = (int) (N0*a);
        double pAsset = lastestOptionPrice;
        //第一步
        Option[] ListD1 = calculateD(plowT,sExp,N,pAsset);
        Option[] ListD2 = calculateD(phighT,sExp,N,pAsset);

        //第二步
        Option i1 = maxLoss(ListD1,sExp,N0, a, pAsset);
        Option i2 = maxLoss(ListD2,sExp,N0, a, pAsset);


        double maxLoss;
        double iNum;
        Option optionI;
        String[][] rtn;
        if(i1==null && i2==null){
            return null;
        }
        if(i1 == null){
            return calculateReconmmendOption2(i2,sExp,N0,a,pAsset);
        }
        if(i2 == null){
            return calculateReconmmendOption2(i1,sExp,N0,a,pAsset);
        }

        double[] result1 = calcaulateMaxLoss(i1,sExp,N0,a,pAsset);
        double maxLoss1 = result1[0];
        double iNum1 = result1[1];
        double[] resutl2 = calcaulateMaxLoss(i2,sExp,N0,a,pAsset);
        double maxLoss2 = resutl2[0];
        double iNum2 = resutl2[1];
        if(maxLoss1>maxLoss2){ maxLoss=maxLoss2; optionI = i2;iNum = iNum2;}
        else { maxLoss = maxLoss1; optionI = i1;iNum = iNum1;}
        optionI.setType(1);
        addAttributesToDOption(optionI);
        //第三步
        rtn = hedgingCalculate(optionI,N0,a,pAsset);


        return new RecommendOption2(optionI, maxLoss,iNum, rtn);
    }

    private RecommendOption2 calculateReconmmendOption2(Option optionI, double sExp, int N0, double a, double pAsset) throws IOException {
        String[][] rtn;
        double maxLoss;
        double iNum;
        double[] result = calcaulateMaxLoss(optionI,sExp,N0,a,pAsset);
        maxLoss = result[0];
        iNum = result[1];
        optionI.setType(1);
        addAttributesToDOption(optionI);
        rtn = hedgingCalculate(optionI,N0,a,pAsset);
        return new RecommendOption2(optionI, maxLoss,iNum, rtn);
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

    private double[] calcaulateMaxLoss(Option i, double sExp, int N0,double a, double pAsset){
        int N = (int)(N0 * a);
        double cost;
        double iK = i.getK();
        double iDelta = i.getDelta();
        double iPrice1 = i.getPrice1();
        int iNum = (int)Math.ceil(N /(10000*Math.abs(iDelta)));
        cost = iNum*10000*iPrice1;
        return new double[]{((N * pAsset) - (((iNum * 10000) - N) * (iK - sExp)) - (N * iK)) + cost,iNum};
    }

    private Option maxLoss(Option[] ListD,double sExp,int N0, double a, double pAsset){
        int N = (int)(N0 * a);
        double cost;
        double maxLoss = Double.MAX_VALUE;
        Option rtn=null;
        for(Option i:ListD){
            double iK = i.getK();
            double iDelta = i.getDelta();
            double iPrice1 = i.getPrice1();
            int iNum = (int)Math.ceil(N /(10000*Math.abs(iDelta)));
            cost = iNum*10000*iPrice1;
            double temp = ((N * pAsset) - (((iNum * 10000) - N) * (iK - sExp)) - (N * iK)) + cost + N0 * (1-a) * (pAsset - sExp);
            if(temp<maxLoss){
                maxLoss = temp;
                rtn = i;
            }
//            logger.info("temp is " + temp);
//            logger.info("iPrice1 is " + iPrice1);

        }
//        logger.info("maxLoss is " + maxLoss);
        return rtn;
    }

    private String[][] hedgingCalculate(Option I, int N0, double a, double pAsset){
//        logger.info("step into hedgingCalculate");
        int N = (int)(N0 * a);
        double iK = I.getK();
//        logger.info("iK is " + iK);

        double iDelta = I.getDelta();
        int iNum = (int)Math.ceil(N / (10000 * Math.abs(iDelta)));
//        logger.info("iNum is " + iNum);
        double iPrice1 = I.getPrice1();

        List<Double> loss1 = new ArrayList<>();
        List<Double> loss2 = new ArrayList<>();
        List<Double> abscissa = new ArrayList<>();

        int length = (int)(pAsset / 0.01);
        for(int i=0; i<length; i++){
            double temp = (double)(i)/100;
            abscissa.add(temp);
//            logger.info("i is" + temp);
            loss1.add(N0 * (pAsset - temp));

            Double temp1 = N*pAsset - (iNum * 10000) * Options(-1, temp, iK) - N* temp + iNum *10000 * iPrice1 + (N0-N)*(pAsset - temp);
            Double temp2 = N*pAsset - (iNum * 10000 - N) * (iK - temp) - N* iK + iNum *10000 *iPrice1 + (N0-N)*(pAsset - iK);
            if(temp > iK){
                loss2.add(temp1);
//                logger.info("temp1 is " + temp1);
            }else {
                loss2.add(temp2);
//                logger.info("temp2 is " + temp2);
            }
        }

        int size = abscissa.size();
        Double [] Abscissa = abscissa.toArray(new Double[size]);
        Double [] Loss1 = loss1.toArray(new Double[size]);
        Double [] Loss2 = loss2.toArray(new Double[size]);

        String[] rtnAbscissa = new String[size];
        String[] rtnLoss1 = new String[size];
        String[] rtnLoss2 = new String[size];

        for(int j=0; j<size;j++){
            rtnAbscissa[j] = Double.toString(Abscissa[j]);
            rtnLoss1[j] = Double.toString(Loss1[j]);
            rtnLoss2[j] = Double.toString(Loss2[j]);
        }


        String[][] rtn = new String[][]{rtnAbscissa,rtnLoss1,rtnLoss2};
//        logger.info("first " + Arrays.deepToString(rtn[0]));
//        logger.info("second " + Arrays.deepToString(rtn[1]));
//        logger.info("third " + Arrays.deepToString(rtn[2]));
//        logger.info("rtn is " + Arrays.deepToString(rtn));
        return rtn;

    }

    @Override
    public ResponseMsg customPortfolio(Option[] list)  {
        //传入的Option列表只有期权代码、到期时间、买入卖出、cp属性
        logger.info("DIY starting!");
        try {
            return new ResponseMsg(0, "custom portfolio finished",mainOneCustomPortfolio(list, 0));
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseMsg(2000, "网络获取失败", null);
        }
    }

    //sigma1 2 and p1 2 没有初始化
    //type为零：正常的DIY组合计算，type为1,：查看我的DIY组合，type为2：查看我的期权组合
    RecommendOption1 mainTwoCustomPortfolio(Option[] list, int type, double k, double M0) throws IOException {
        r = dataSource.get_r();
        t = Integer.parseInt(dataSource.get_expireAndremainder(list[0].getExpireTime())[1]);
        sigma = dataSource.get_Sigma();//实时波动率
        S0 = dataSource.get_LatestPrice();

        for (Option each :list) {
            setOptionAttributes(each);
        }
        logger.info("get data from net successfully");
        structD d = new structD();
        d.optionCombination = list;
        d.buyAndSell = new int[d.optionCombination.length];
        for(int i = 0;i < d.optionCombination.length;i++){
            d.buyAndSell[i] = d.optionCombination[i].getType();
        }
        List <structD> D = new ArrayList<>();
        generateD(D,new ArrayList<>(),0,d.buyAndSell, Arrays.asList(list),'D', 1);
        structD maxGoalD;
        maxGoalD = secondStep(D);
        logger.info("real time data finished");
        for(int i = 0;i < maxGoalD.optionCombination.length;i++){
            addAttributesToDOption(maxGoalD.optionCombination[i]);
        }
        List<Double> profits = new ArrayList<>();
        String[] StringProfits = new String[month.length];
        if(type == 0){
            profits = thirdStep(maxGoalD, 'W');
            logger.info("back test finished");
        }
        for(int i = 0; i < profits.size();i++){
            StringProfits[i] = Double.toString(profits.get(i));
        }
        double M = 50000;
        String[][] graph = new String[][]{month, StringProfits};
        if(type == 2){
            M = M0 * (r * Math.ceil(t / 30.0) / 12 + k) / (1 + r * Math.ceil(t / 30.0) / 12);
            maxGoalD.returnOnAssets = (maxGoalD.E + (M0 - M) * r * Math.ceil(t / 30.0) / 12) / M0;
        }
        return new RecommendOption1(maxGoalD.optionCombination, maxGoalD.buyAndSell, maxGoalD.num, maxGoalD.p0,
                maxGoalD.pb, maxGoalD.z_delta, maxGoalD.z_gamma, maxGoalD.z_vega, maxGoalD.z_theta, maxGoalD.z_rho,
                maxGoalD.E / M, maxGoalD.returnOnAssets, maxGoalD.beta, graph, M, k);
    }

    RecommendOption1 mainOneCustomPortfolio(Option[] list, int type) throws IOException {
        sigma1 = sigma2 = dataSource.get_Sigma();
        p1 = p2 = dataSource.get_LatestPrice();
        expiredMonths = dataSource.get_T();
        String expireT = null;
        if(list.length > 1) {
            expireT = list[1].getExpireTime();
            T = expireT.split("-")[0] + '-' + expireT.split("-")[1];
        }
        return mainTwoCustomPortfolio(list, type, 0, 0);
    }

    private void warning() throws IOException {
        double r = dataSource.get_r();
        List <Portfolio> allPortfolio = portfolioDataService.findAll();
        for (Portfolio anAllPortfolio : allPortfolio) {
            if (anAllPortfolio.isTrackingStatus() && anAllPortfolio.getType() == RECOMMEND_PORTFOLIO) {
                double M0 = anAllPortfolio.getM0();
                double k = anAllPortfolio.getK();
                double t;
                Date now = new Date();
                Date end = null;
                try {
                    end = new SimpleDateFormat("yyyy-MM-dd").parse(anAllPortfolio.getOptions()[0].getExpireTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                assert end != null;
                t = (end.getTime() - now.getTime()) * 1.0 / 1000 / 60 / 60 / 24;
                double M = M0 * (r * Math.ceil(t / 30.0) / 12 + k) / (1 + r * Math.ceil(t / 30.0) / 12);

                double loss = 0;
                for (int j = 0; j < anAllPortfolio.getOptions().length; j++) {
                    double price;
                    setOptionAttributes(anAllPortfolio.getOptions()[j]);
                    if (anAllPortfolio.getOptions()[j].getType() < 0)
                        price = anAllPortfolio.getOptions()[j].getPrice2();
                    else
                        price = anAllPortfolio.getOptions()[j].getPrice1();
                    loss += -1 * (anAllPortfolio.getOptions()[j].getRealTimePrice() - price) * anAllPortfolio.getOptions()[j].getType();
                }
                if (loss / M > k){
                    logger.info("警报！！");
                    PolySms.sendWarningSms(userDataService.findById(anAllPortfolio.getUsername()).getTelephone());
                }
                return;
                //报警！
            }
        }
    }

    @Scheduled(initialDelay = 1000, fixedRate = 10 * 60 * 1000)
    public void task() throws IOException {
        logger.info("-------------------");
        logger.info("正在更新网络数据");
        this.isUpdataRunning = true;
        upDataFromNet();
        this.isUpdataRunning = false;
        logger.info("网络数据更新完成");
        logger.info("-------------------");
        logger.info("警报检测");
        warning();
        logger.info("警报检测完成");
        logger.info("-------------------");

        int N0 = 50000;
        double a = 0.25;
        double sExp = 2.2;
        String T = "2018-10";

        ResponseMsg rspMsg = hedging(N0, a, sExp, T);
//        this.S = new double[]{2.391};
//        this.sigma = 23.98 / 100.0;
//        this.t = 46;
//        double x = Interest(1, 2.3, 0.2175)[0];
//        double y = Interest(1, 2.55, 0.061)[0];
//        double a = x - 2 * y;
    }
}