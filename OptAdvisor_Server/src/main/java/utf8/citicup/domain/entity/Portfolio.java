package utf8.citicup.domain.entity;

import javax.persistence.*;

import java.util.HashMap;
import java.util.Map;

import static utf8.citicup.domain.common.Type.RECOMMEND_PORTFOLIO;

@Entity
public class Portfolio {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String username;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "portfolio")
    @OrderColumn
    private Option[] options;

    private Enum type; //type指1：资产配置组合 2：套期保值组合 3：DIY组合
    private boolean trackingStatus;

    //期权组合和DIY要有的东西
    private double M0;//警报需要
    private double k;//警报需要
    private double sigma1;
    private double sigma2;
    private double p1;
    private double p2;
    private double cost;//成本p1-p2
    private double bond;//保证金
    private double z_delta;
    private double z_gamma;
    private double z_vega;
    private double z_theta;
    private double z_rho;
    private double returnOnAssets;
    private double EM;//组合的期望收益率
    private double beta;//组合风险值

    //套期保值需要的量
    private int N;//套期保值中的N
    private double iK;//套期保值中的iK
    private double pAsset;//套期保值中的pAsset
    private double sExp;
    private boolean flag;

    private String backTestData;
    private String backTestData1;

    public Portfolio(String username,RecommendOption2 recommendOption2,int n, double pAsset, double sExp,boolean flag, Enum type) {
            this.username = username;
            this.type = type;

            N = n;
            this.iK = recommendOption2.getiK();
            this.pAsset = pAsset;
            this.sExp = sExp;
            this.flag = flag;

            options = new Option[1];
            options[0] = recommendOption2.getOption();
            setBackTestData(recommendOption2.getGraph()[1]);
            setBackTestData1(recommendOption2.getGraph()[2]);
    }

    public Portfolio(String name, String username, RecommendOption1 recommendOption1, Enum type, boolean trackingStatus) {
        this.name = name;
        this.username = username;
        this.type = type;
        options = new Option[recommendOption1.getOptionList().length];
        System.arraycopy(recommendOption1.getOptionList(), 0, options,0,options.length);
        M0 = recommendOption1.getM0();
        k = recommendOption1.getK();
        if (type == RECOMMEND_PORTFOLIO) {
            sigma1 = recommendOption1.getSigma1();
            sigma2 = recommendOption1.getSigma2();
            p1 = recommendOption1.getP1();
            p2 = recommendOption1.getP2();
        }
        cost = recommendOption1.getCost();
        bond = recommendOption1.getBond();
        z_delta = recommendOption1.getZ_delta();
        z_gamma = recommendOption1.getZ_gamma();
        z_rho = recommendOption1.getZ_rho();
        z_theta = recommendOption1.getZ_theta();
        z_vega = recommendOption1.getZ_vega();
        returnOnAssets = recommendOption1.getReturnOnAssets();
        EM = recommendOption1.getEM();
        beta = recommendOption1.getBeta();
        this.trackingStatus = trackingStatus;
        setBackTestData(recommendOption1.getGraph()[1]);
    }

    public Portfolio(){}

    public int getN() {
        return N;
    }

    public double getiK() {
        return iK;
    }

    public double getpAsset() {
        return pAsset;
    }

    public double getsExp() {
        return sExp;
    }

    public boolean isFlag() {
        return flag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Option[] getOptions() {
        return options;
    }

    public void setOptions(Option[] options) {
        if (null != this.options)
            for (Option each : this.options)
                each.setPortfolio(null);
        if (null != options)
            for (Option each : options)
                each.setPortfolio(this);
        this.options = options;
    }

    public Enum getType() {
        return type;
    }

    public void setType(Enum type) {
        this.type = type;
    }

    public boolean isTrackingStatus() {
        return trackingStatus;
    }

    public void setTrackingStatus(boolean trackingStatus) {
        this.trackingStatus = trackingStatus;
    }

    public Long getId() { return id; }

    public double getM0() {
        return M0;
    }

    public double getK() {
        return k;
    }

    public double getCost() {
        return cost;
    }

    public double getBond() {
        return bond;
    }

    public double getZ_delta() {
        return z_delta;
    }

    public double getZ_gamma() {
        return z_gamma;
    }

    public double getZ_vega() {
        return z_vega;
    }

    public double getZ_theta() {
        return z_theta;
    }

    public double getZ_rho() {
        return z_rho;
    }

    public double getEM() {
        return EM;
    }

    public double getBeta() {
        return beta;
    }

    public double getSigma1() {
        return sigma1;
    }

    public double getSigma2() {
        return sigma2;
    }

    public double getP1() {
        return p1;
    }

    public double getP2() {
        return p2;
    }

    public double getReturnOnAssets() {
        return returnOnAssets;
    }

    public String[] getBackTestData(){
        return this.backTestData.split(",");
    }

    public String[] getBackTestData1(){
        return this.backTestData1.split(",");
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setM0(double m0) {
        M0 = m0;
    }

    public void setK(double k) {
        this.k = k;
    }

    public void setSigma1(double sigma1) {
        this.sigma1 = sigma1;
    }

    public void setSigma2(double sigma2) {
        this.sigma2 = sigma2;
    }

    public void setP1(double p1) {
        this.p1 = p1;
    }

    public void setP2(double p2) {
        this.p2 = p2;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public void setBond(double bond) {
        this.bond = bond;
    }

    public void setZ_delta(double z_delta) {
        this.z_delta = z_delta;
    }

    public void setZ_gamma(double z_gamma) {
        this.z_gamma = z_gamma;
    }

    public void setZ_vega(double z_vega) {
        this.z_vega = z_vega;
    }

    public void setZ_theta(double z_theta) {
        this.z_theta = z_theta;
    }

    public void setZ_rho(double z_rho) {
        this.z_rho = z_rho;
    }

    public void setEM(double EM) {
        this.EM = EM;
    }

    public void setBeta(double beta) {
        this.beta = beta;
    }

    public void setN(int n) {
        N = n;
    }

    public void setiK(double iK) {
        this.iK = iK;
    }

    public void setpAsset(double pAsset) {
        this.pAsset = pAsset;
    }

    public void setsExp(double sExp) {
        this.sExp = sExp;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public void setReturnOnAssets(double returnOnAssets) {
        this.returnOnAssets = returnOnAssets;
    }

    public void setBackTestData(String[] BackTestData){
        this.backTestData = String.join(",",BackTestData);
    }

    public void setBackTestData1(String[] BackTestData){
        this.backTestData1 = String.join(",",BackTestData);
    }
}
