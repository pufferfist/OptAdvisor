package utf8.citicup.domain.historyEntity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 对应OptionETF_TimeSeriesData20180416
 */
@Entity
@Table(indexes = {
        @Index(name = "index_latest_date", columnList = "latestDate"),
        @Index(name = "index_code_name", columnList = "codeName")
})
public class OptionTsd implements Serializable {
    @Id
    private Long id;

    private String latestDate;//(市场)最近交易日期
    private String codeName;//证券代码
    @Column(nullable = true)
    private Double closePrice;//收盘价
    @Column(nullable = true)
    private Double avgPrice;//均价
    @Column(nullable = true)
    private Double preClosePrice;//前收盘价
    private double preEndPrice;//前结算价
    private double endPrice;//结算价
    @Column(nullable = true)
    private Double theoryPrice;//理论价格
    @Column(nullable = true)
    private Double delta;
    @Column(nullable = true)
    private Double gamma;
    @Column(nullable = true)
    private Double vega;
    @Column(nullable = true)
    private Double theta;
    @Column(nullable = true)
    private Double rho;
    private double historyVolatility;//历史波动率
    @Column(nullable = true)
    private Double impliedVolatility;//隐含波动率

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getLatestDate() { return latestDate; }

    public void setLatestDate(String latestDate) { this.latestDate = latestDate; }

    public String getCodeName() { return codeName; }

    public void setCodeName(String codeName) { this.codeName = codeName; }

    public Double getClosePrice() { return closePrice; }

    public void setClosePrice(Double closePrice) { this.closePrice = closePrice; }

    public Double getAvgPrice() { return avgPrice; }

    public void setAvgPrice(Double avgPrice) { this.avgPrice = avgPrice; }

    public Double getPreClosePrice() { return preClosePrice; }

    public void setPreClosePrice(Double preClosePrice) { this.preClosePrice = preClosePrice; }

    public double getPreEndPrice() { return preEndPrice; }

    public void setPreEndPrice(double preEndPrice) { this.preEndPrice = preEndPrice; }

    public double getEndPrice() { return endPrice; }

    public void setEndPrice(double endPrice) {
        this.endPrice = endPrice;
    }

    public Double getTheoryPrice() { return theoryPrice; }

    public void setTheoryPrice(Double theoryPrice) { this.theoryPrice = theoryPrice; }

    public Double getDelta() { return delta; }

    public void setDelta(Double delta) { this.delta = delta; }

    public Double getGamma() { return gamma; }

    public void setGamma(Double gamma) { this.gamma = gamma; }

    public Double getVega() { return vega; }

    public void setVega(Double vega) { this.vega = vega; }

    public Double getTheta() { return theta; }

    public void setTheta(Double theta) { this.theta = theta; }

    public Double getRho() { return rho; }

    public void setRho(Double rho) { this.rho = rho; }

    public double getHistoryVolatility() { return historyVolatility; }

    public void setHistoryVolatility(double historyVolatility) { this.historyVolatility = historyVolatility; }

    public Double getImpliedVolatility() { return impliedVolatility; }

    public void setImpliedVolatility(Double impliedVolatility) { this.impliedVolatility = impliedVolatility; }
}
