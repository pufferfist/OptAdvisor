package utf8.citicup.serviceImpl;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import utf8.citicup.domain.entity.*;
import utf8.citicup.service.RecommendService;
import utf8.citicup.service.util.GetData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

@Service
public class RecommendServiceImpl implements RecommendService {
    /*用户输入数据*/
    private double M0;//托管资金总额
    private double k;//投资者允许最大亏损
    private double a;//投资者可接受的最大损失百分比
    private int T;//投资者预测价格有效时间
    private char combination;//A-H 八个选项选其一
    private double p1;
    private double p2;
    private double sigma1;
    private double sigma2;

    /*网络获取数据*/
    private GetData dataSource;
    private double S0;//实时标的价格[7]
    private double r;//固定年化收益率[5]
    private double sigma;//实时波动率[6]
    private String[] expiredMonths;
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

    public void test() {
        System.out.println("hello");
    }

    public RecommendServiceImpl() throws IOException {
        dataSource = new GetData();
        upDataFromNet();
    }

    /*从网络获取所需的数据*/
    private void upDataFromNet() throws IOException {
        r = dataSource.get_r();
        S0 = dataSource.get_S0();//实时标的价格
        sigma = dataSource.getSigma();//实时波动率
        dv = 0;//股票分红率
        expiredMonths = dataSource.get_T();
    }

    /*标准正态分布函数*/
    private double normcdf(double num) {
        return 0;
    }

    private double Options(int cp, double S0, double k, int t, double r, double sigma, double dv) {
        this.d_1 = Math.log(S0 / k) + (r - dv + 0.5 * Math.pow(sigma, 2) * t) / sigma / Math.pow(t, 0.5);
        this.d_2 = d_1 - sigma * (Math.pow(t, 0.5));
        double price = cp * S0 * Math.exp(-1 * dv * t) * normcdf(cp * d_1) -
                cp * k * Math.exp(-1 * r * t) * normcdf(cp * d_2);
        return price;
    }

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
                if (cp == -1) {
                    C[i] = 10000 * (Math.max(k - S[i], 0) - price);
                }
            }
        }
        return C;
    }

    @Override
    public ResponseMsg recommendPortfolio(double M0, double k, double a, int T, char combination, double p1, double p2, double sigma1, double sigma2) {
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
        this.M = M0 * (r * T / 12 + a) / (1 + r * T / 12);
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
    public ResponseMsg heging(int N0, double a, double s_exp, int T) {
        return null;
    }

    @Override
    public ResponseMsg customPortfolio(Option[] list) {
        return null;
    }
}
