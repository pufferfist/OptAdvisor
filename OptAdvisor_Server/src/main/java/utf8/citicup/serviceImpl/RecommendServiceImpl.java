package utf8.citicup.serviceImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import utf8.citicup.domain.entity.Option;
import utf8.citicup.domain.entity.ResponseMsg;
import utf8.citicup.service.RecommendService;
import utf8.citicup.service.util.GetData;

import java.io.IOException;
import java.util.*;
@Service
public class RecommendServiceImpl implements RecommendService {

    private static final double eps = 0.0001;

    /*用户输入数据*/
    private double M0;//托管资金总额
    private double k;//投资者允许最大亏损
    private double a;//投资者可接受的最大损失百分比(套期保值里的)
    private String T;//投资者预测价格有效时间
    private char combination;//A-H 八个选项选其一
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
//    private String[] expireTimeArray = {};//网上获取

    /*自设值*/
    private double dv;//股票分红率
    private double S[];
    private double eps1;

    /*计算得到的值*/
    private double d_1;
    private double d_2;
    private double M;
    private Logger logger = LoggerFactory.getLogger(RecommendService.class);

    /*新用到的结构类型*/
    class structD{
        Option[] optionCombination;//组合只有两种期权，第一个是买入，第二个是卖出（同时有买入卖出时）
        int[] buyAndSell;
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

        public double getGoal() {
            return goal;
        }
    }

    public void test(){
//        structD[] D = new structD[3];
//        D[0] = new structD();
//        D[0].goal = 2;
//        D[1] = new structD();
//        D[1].goal = 1;
//        D[2] = new structD();
//        D[2].goal = 3;
//        List <structD> X = Arrays.asList(D);
//        Collections.sort(X, new Comparator<structD>() {
//            @Override
//            public int compare(structD o1, structD o2) {
//                return Double.compare(o2.goal, o1.goal);
//            }
//        });
//        for(structD d : X){
//            System.out.println(d.goal);
//        }
        try {
            System.out.println("hello");
            upDataFromNet();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public RecommendServiceImpl(){
        dataSource = new GetData();
        this.T = "2018-08";
    }

    /*从网络获取所需的数据*/
    private void upDataFromNet() throws IOException {
        r = dataSource.get_r();
        t = Integer.parseInt(dataSource.get_expireAndremainder(T)[1]);
        S0 = dataSource.get_S0();//实时标的价格
        sigma = dataSource.get_Sigma();//实时波动率
        lastestOptionPrice = dataSource.get_LatestPrice();
        dv = 0;//股票分红率
        expiredMonths = dataSource.get_T();



        for (String expiredMonth : expiredMonths) {
            //region 根据行权名称得到相关数据
            chigh.put(expiredMonth, new ArrayList<Option>());
            clow.put(expiredMonth, new ArrayList<Option>());
            phigh.put(expiredMonth, new ArrayList<Option>());
            plow.put(expiredMonth, new ArrayList<Option>());

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
                    newOption.setId(cpOption);
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
///*
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

    private double betaValue(double delta, double price){
        return S0 * delta / price;
    }
//*/
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
        this.M0 = M0;
        this.k = k;
        this.T = T;
        this.combination = combination;
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

        /*货币基金与衍生品组合分配：*/
        this.M = M0 * (r * Math.ceil(t / 30.0) / 12 + k) / (1 + r * Math.ceil(t / 30.0) / 12);

        List<structD> D = firstStep(combination);
        structD maxGoalD = new structD();
        maxGoalD = secondStep(D);
        return new ResponseMsg(2002, "no match combination", null);
    }

    //期权组合第一步，计算出集合D及其相关内容
    private List<structD> firstStep(char choose){
        int buy = 0;//买入期权的个数为正,当为负数时，表示卖
        int sell = 0;//卖出期权的数 为负，当为正数时，表示买
        Option[] buyOptions = new Option[0];
        Option[] buyOptions1 = new Option[0];//对于D,E第一步有两种构建方式
        Option[] sellOptions = new Option[0];
        Option[] sellOptions1 = new Option[0];//对于D,E第一步有两种构建方式
        //region 选择买入及卖出的期权及其个数
        //TODO : B和G没有考虑
        switch (choose){
            //买入两个高价看涨期权，卖出一个低价看涨期权，使得delta=0；
            case 'A':buy = 2;sell = -1;
                buyOptions = chigh.get(T).toArray(new Option[0]);
                sellOptions = clow.get(T).toArray(new Option[0]);
                break;
            case 'B':break;

            //买入两支低价看跌期权，卖出一支高价看跌期权；
            case 'C':buy = 2;sell = -1;
                buyOptions = plow.get(T).toArray(new Option[0]);
                sellOptions = phigh.get(T).toArray(new Option[0]);
                break;

            //买入低价看涨期权，卖出高价看涨期权 或 买入低价看跌期权，卖出高价看跌期权
            case 'D':buy = 1;sell = -1;
                buyOptions = clow.get(T).toArray(new Option[0]);
                sellOptions = chigh.get(T).toArray(new Option[0]);
                buyOptions1 = plow.get(T).toArray(new Option[0]);
                sellOptions1 = phigh.get(T).toArray(new Option[0]);
                break;

            //买入高价看跌期权，卖出低价看跌期权 或 买入高价看涨期权，卖出低价看涨期权
            case 'E':buy = 1;sell = -1;
                buyOptions = phigh.get(T).toArray(new Option[0]);
                sellOptions = plow.get(T).toArray(new Option[0]);
                buyOptions1 = chigh.get(T).toArray(new Option[0]);
                sellOptions1 = clow.get(T).toArray(new Option[0]);
                break;

            //卖出一个低价看跌期权，买入两个高价看跌期权
            case 'F':buy = 2;sell = -1;
                buyOptions = phigh.get(T).toArray(new Option[0]);
                sellOptions = plow.get(T).toArray(new Option[0]);
                break;

            case 'G':break;

            //卖出两个高价看涨期权，买入一个低价看涨期权
            case 'H':buy = 1;sell = -2;
                buyOptions = clow.get(T).toArray(new Option[0]);
                sellOptions = chigh.get(T).toArray(new Option[0]);
                break;

             default:break;
        }
        //endregion
        
        List<structD> D = new ArrayList<structD>();
        generateD(D, sellOptions, sell, buyOptions, buy, choose);
        //D和E第一步有两种构造方式，两种构造方式得到的组合都放入D中
        if(choose == 'D' || choose == 'E'){
            generateD(D, sellOptions1, sell, buyOptions1, buy, choose);
        }
        return D;
    }

    //根据不同期权组合，生成集合D
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
                boolean isInD = false;
                if(choose == 'D' || choose == 'E')
                    isInD = true;
                else if(choose == 'H')
                    isInD = Math.abs(buy * j_delta + sell * i_delta) < eps;
                else
                    isInD = Math.abs(buy * j_delta + sell * i_delta) < eps && buy * j_vega + sell * i_vega > 0;

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
        D.sort(new Comparator<structD>() {
            @Override
            public int compare(structD o1, structD o2) {
                return Double.compare(o2.goal, o1.goal);
            }
        });
        return D.get(0);
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



/*
            //第三步


/*
            if(flag) {
                for (String m : month) {
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
            }*/
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
