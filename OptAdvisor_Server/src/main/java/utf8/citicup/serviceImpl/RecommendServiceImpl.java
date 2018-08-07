package utf8.citicup.serviceImpl;

import utf8.citicup.domain.entity.*;
import utf8.citicup.service.RecommendService;
import utf8.citicup.service.util.GetData;

import java.io.IOException;

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
    private double dv;//股票分红率

    /*计算得到的值*/
    private double d_1;
    private double d_2;


    public RecommendServiceImpl() throws IOException {
        dataSource = new GetData();
        r = dataSource.get_r();
        S0 = dataSource.get_S0();
        sigma = dataSource.getSigma();
        dv = 0;
    }

    /*从网络获取所需的数据*/
    private void upDataFromNet() throws IOException{
        r = dataSource.get_r();
        S0 = dataSource.get_S0();//实时标的价格
        sigma = dataSource.getSigma();//实时波动率
        dv = 0;//股票分红率
    }

    private void Options(){
        double t = 0;//t是什么？？？
        this.d_1 = Math.log(S0/k)+(r-dv+0.5*Math.pow(sigma,2)*t)/sigma/Math.pow(t,0.5);
        this.d_2 = d_1-sigma*(Math.pow(t,0.5));

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


        try {
            this.upDataFromNet();
        } catch (IOException e) {
            return new ResponseMsg(2001, "huo qu cuo wu");
        }
        /*货币基金与衍生品组合分配：*/
        double M = M0*(r*T/12+a)/(1+r*T/12);

        /*Options*/


        return new ResponseMsg(0,"recommend success", new RecommendOption1());
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
