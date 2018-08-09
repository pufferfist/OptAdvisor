package utf8.citicup.serviceImpl;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import utf8.citicup.domain.entity.*;
import utf8.citicup.service.RecommendService;
import utf8.citicup.service.util.GetData;

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
    private Map<String, ArrayList<Option>> chigh = new Hashtable<String, ArrayList<Option>>();
    private Map<String, ArrayList<Option>> clow = new Hashtable<String, ArrayList<Option>>();
    private Map<String, ArrayList<Option>> phigh = new Hashtable<String, ArrayList<Option>>();
    private Map<String, ArrayList<Option>> plow = new Hashtable<String, ArrayList<Option>>();
    private String[] expireTimeArray = {};//网上获取

    /*自设值*/
    private double dv;//股票分红率
    private double S[];

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
    int[] month;

    /*新用到的结构类型*/
    class structD{
        Option[] optionCombination;
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
    }


    public RecommendServiceImpl(){
        dataSource = new GetData();
    }

    /*从网络获取所需的数据*/
    public void upDataFromNet() throws IOException {
        r = dataSource.get_r();
        t = Integer.parseInt(dataSource.get_expireAndremainder(T)[0]);
        S0 = dataSource.get_S0();//实时标的价格
        sigma = dataSource.getSigma();//实时波动率
        lastestOptionPrice = dataSource.get_LatestPrice();
        dv = 0;//股票分红率
        expiredMonths = dataSource.get_T();
        String[][] Optionss = dataSource.get_contract(expiredMonths[0]);
//        String[] cOptionsName = Optionss[0];
//        String[] pOptionsName = Optionss[1];
//        System.out.println(d[0].p0);
//        structD D = new structD();
//        D.p0 = 10;
//        System.out.println(D.p0);
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
        d_1 = Math.log(S0 / k) + (r - dv + 0.5 * Math.pow(sigma, 2) * t) / sigma / Math.pow(t, 0.5);
        d_2 = d_1 - (sigma * (Math.pow(t, 0.5)));
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

    private double goalValue(int num, double E, double M, double beta){
//        return w1 * ((num * E) / M - Math.min())
    }

    @Override
    public ResponseMsg recommendPortfolio(double M0, double k, double a, String T, char combination, double p1, double p2, double sigma1, double sigma2) {
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
        this.w1 = w1 / 100.0;
        this.w2 = w2 / 100.0;

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
        List <structD> D = new ArrayList<structD>();

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

        /*第二步*/
        for (structD z : D) {
            z.num = (int) (M / z.p0);
            double[] C_j = Interest(1, S, z.optionCombination[1].getK(), t, r, sigma, dv, z.optionCombination[1].getPrice1());
            double[] C_i = Interest(1, S, z.optionCombination[0].getK(), t, r, sigma, dv, z.optionCombination[0].getPrice2());
            double[] C_new = new double[C_i.length];
            for(int i = 0; i < C_i.length; i++){
                C_new[i] = 2 *  C_j[i] - C_i[i];
            }
            z.E = Expected(C_new, S, p1, p2, sigma1, sigma2);
//            z.beta = 2*beta（j_delta,j_price1)-beta(i_delta,i_price2)

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
        return null;
    }

    @Override
    public ResponseMsg customPortfolio(Option[] list) {
        return null;
    }
}
