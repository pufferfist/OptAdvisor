package utf8.citicup.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

/**
 * 期权实体
 */
@Entity
@Table(name = "myOption")
public class Option {
    @Id
    @GeneratedValue
    private Long persisentId;//存储用主键

    private String tradeCode; //交易代码
    private String optionCode; //期权代码
    private String name;//例如:50ETF购8月2600
    private int type;//1为买入0为卖出
    private int cp;//1为看涨 -1为看跌
    private String expireTime;//到期时间
    private double transactionPrice;//成交价(即交易时的买价或卖价)
    private double yclose;//期权前一天收盘价
    private double price1;//期权实时买入价格
    private double price2;//期权实时卖出价格
    private double k;//期权行权价格
    private double realTimePrice;//期权实时价
    private double delta;
    private double gamma;
    private double vega;
    private double theta;
    private double rho;
    private double beta;

    private int dynamic;//0是静态，1是动态

    @ManyToOne
    @JoinColumn(name = "portfolio_id")
    @JsonIgnore
    private Portfolio portfolio;

    public Option(){}

    @SuppressWarnings("CopyConstructorMissesField")
    public Option(Option option) {
        this.tradeCode = option.tradeCode;
        this.optionCode = option.optionCode;
        this.name = option.name;
        this.type = option.type;
        this.cp = option.cp;
        this.expireTime = option.expireTime;
        this.transactionPrice = option.transactionPrice;
        this.yclose = option.yclose;
        this.price1 = option.price1;
        this.price2 = option.price2;
        this.k = option.k;
        this.realTimePrice = option.realTimePrice;
        this.delta = option.delta;
        this.gamma = option.gamma;
        this.vega = option.vega;
        this.theta = option.theta;
        this.rho = option.rho;
        this.beta = option.beta;
    }

    public Option(String optionCode, int type, int cp, String expireTime) {
        this.optionCode = optionCode;
        this.type = type;
        this.cp = cp;
        this.expireTime = expireTime;
    }

    public Option(String tradeCode, String optionCode, String name, int type, int cp, String expireTime, double transactionPrice, double yclose, double price1, double price2, double k, double realTimePrice, double delta, double gamma, double vega, double theta, double rho, double beta) {
        this.tradeCode = tradeCode;
        this.optionCode = optionCode;
        this.name = name;
        this.type = type;
        this.cp = cp;
        this.expireTime = expireTime;
        this.transactionPrice = transactionPrice;
        this.yclose = yclose;
        this.price1 = price1;
        this.price2 = price2;
        this.k = k;
        this.realTimePrice = realTimePrice;
        this.delta = delta;
        this.gamma = gamma;
        this.vega = vega;
        this.theta = theta;
        this.rho = rho;
        this.beta = beta;
    }

    @Override
    public String toString() {
        return "{"+name+","+ tradeCode + "," + expireTime +"}";
    }

    public double getK() {
        return k;
    }

    public void setK(double k) {
        this.k = k;
    }

    public double getPrice1() {
        return price1;
    }

    public void setPrice1(double price1) {
        this.price1 = price1;
    }

    public double getPrice2() {
        return price2;
    }

    public void setPrice2(double price2) {
        this.price2 = price2;
    }

    public double getYclose() {
        return yclose;
    }

    public void setYclose(double yclose) {
        this.yclose = yclose;
    }

    public String getTradeCode() { return tradeCode; }

    public void setTradeCode(String tradeCode) { this.tradeCode = tradeCode; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public int getType() { return type; }

    public void setType(int type) { this.type = type; }

    public int getCp() {
        return cp;
    }

    public void setCp(int cp) {
        this.cp = cp;
    }

    public String getExpireTime() { return expireTime; }

    public void setExpireTime(String expireTime) { this.expireTime = expireTime; }

    public double getTransactionPrice() { return transactionPrice; }

    public void setTransactionPrice(double transactionPrice) { this.transactionPrice = transactionPrice; }

    public double getDelta() { return delta; }

    public void setDelta(double delta) { this.delta = delta; }

    public double getGamma() { return gamma; }

    public void setGamma(double gamma) { this.gamma = gamma; }

    public double getVega() { return vega; }

    public void setVega(double vega) { this.vega = vega; }

    public double getTheta() { return theta; }

    public void setTheta(double theta) { this.theta = theta; }

    public double getRho() { return rho; }

    public void setRho(double rho) { this.rho = rho; }

    public double getBeta() { return beta; }

    public void setBeta(double beta) { this.beta = beta; }

    public Long getPersisentId() {
        return persisentId;
    }

    public Portfolio getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
    }

    public String getOptionCode() {
        return optionCode;
    }

    public void setOptionCode(String optionCode) {
        this.optionCode = optionCode;
    }

    public double getRealTimePrice() {
        return realTimePrice;
    }

    public void setRealTimePrice(double realTimePrice) {
        this.realTimePrice = realTimePrice;
    }

    public int getDynamic() {
        return dynamic;
    }

    public void setDynamic(int dynamic) {
        this.dynamic = dynamic;
    }
}
