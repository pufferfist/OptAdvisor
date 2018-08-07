package utf8.citicup.domain.historyEntity;

import org.apache.commons.lang.StringUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 对应OptionETF_TimeSeriesData20180416
 */
@Entity
public class OptionTsd {
    @Id
    private Long id;

    private String latestDate;//(市场)最近交易日期
    private String codeName;//证券代码
    @Column(nullable = true)
    private double closePrice;//收盘价
    @Column(nullable = true)
    private double avgPrice;//均价
    @Column(nullable = true)
    private double preClosePrice;//前收盘价
    private double preEndPrice;//前结算价
    private double endPrice;//结算价
    @Column(nullable = true)
    private double theoryPrice;//理论价格
    @Column(nullable = true)
    private double delta;
    @Column(nullable = true)
    private double gamma;
    @Column(nullable = true)
    private double vega;
    @Column(nullable = true)
    private double theta;
    @Column(nullable = true)
    private double rho;
    private double historyVolatility;//历史波动率
    @Column(nullable = true)
    private double impliedVolatility;//隐含波动率

    public OptionTsd() { }

    /**
     * 读取文本文件，构造对象
     */
    public OptionTsd(String value){
        String[] values=value.split(",");
        setId(Long.valueOf(values[0]));
        setLatestDate(values[1]);
        setCodeName(values[2]);
        setClosePrice(Double.parseDouble(values[3]));
        setPreEndPrice(Double.parseDouble(values[6]));
        setEndPrice(Double.parseDouble(values[7]));
        setHistoryVolatility(Double.parseDouble(values[15]));

        if(!StringUtils.isEmpty(values[4])) {
            setAvgPrice(Double.parseDouble(values[4]));
        }

        if(!StringUtils.isEmpty(values[5])) {
            setPreClosePrice(Double.parseDouble(values[5]));
        }
        if(!StringUtils.isEmpty(values[8])) {
            setTheoryPrice(Double.parseDouble(values[8]));
        }
        if(!StringUtils.isEmpty(values[9])) {
            setDelta(Double.parseDouble(values[9]));
        }
        if(!StringUtils.isEmpty(values[10])) {
            setGamma(Double.parseDouble(values[10]));
        }
        if(!StringUtils.isEmpty(values[11])) {
            setVega(Double.parseDouble(values[11]));
        }
        if(!StringUtils.isEmpty(values[12])) {
            setTheta(Double.parseDouble(values[12]));
        }
        if(!StringUtils.isEmpty(values[13])) {
            setRho(Double.parseDouble(values[13]));
        }
        if(!StringUtils.isEmpty(values[14])) {
            setImpliedVolatility(Double.parseDouble(values[14]));
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodeName() {
        return codeName;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

    public double getClosePrice() {
        return closePrice;
    }

    public void setClosePrice(double closePrice) {
        this.closePrice = closePrice;
    }

    public double getAvgPrice() {
        return avgPrice;
    }

    public void setAvgPrice(double avgPrice) {
        this.avgPrice = avgPrice;
    }

    public double getPreClosePrice() {
        return preClosePrice;
    }

    public void setPreClosePrice(double preClosePrice) {
        this.preClosePrice = preClosePrice;
    }

    public double getPreEndPrice() {
        return preEndPrice;
    }

    public void setPreEndPrice(double preEndPrice) {
        this.preEndPrice = preEndPrice;
    }

    public double getEndPrice() {
        return endPrice;
    }

    public void setEndPrice(double endPrice) {
        this.endPrice = endPrice;
    }

    public double getTheoryPrice() {
        return theoryPrice;
    }

    public void setTheoryPrice(double theoryPrice) {
        this.theoryPrice = theoryPrice;
    }

    public double getDelta() {
        return delta;
    }

    public void setDelta(double delta) {
        this.delta = delta;
    }

    public double getVega() {
        return vega;
    }

    public void setVega(double vega) {
        this.vega = vega;
    }

    public double getRho() {
        return rho;
    }

    public void setRho(double rho) {
        this.rho = rho;
    }

    public double getHistoryVolatility() {
        return historyVolatility;
    }

    public void setHistoryVolatility(double historyVolatility) {
        this.historyVolatility = historyVolatility;
    }

    public double getImpliedVolatility() {
        return impliedVolatility;
    }

    public void setImpliedVolatility(double impliedVolatility) {
        this.impliedVolatility = impliedVolatility;
    }

    public String getLatestDate() {
        return latestDate;
    }

    public void setLatestDate(String latestDate) {
        this.latestDate = latestDate;
    }

    public double getGamma() {
        return gamma;
    }

    public void setGamma(double gamma) {
        this.gamma = gamma;
    }

    public double getTheta() {
        return theta;
    }

    public void setTheta(double theta) {
        this.theta = theta;
    }
}
