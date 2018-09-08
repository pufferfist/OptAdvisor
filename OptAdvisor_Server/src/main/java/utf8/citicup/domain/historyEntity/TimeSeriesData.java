package utf8.citicup.domain.historyEntity;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 对应ETF50_Time_Series_Data表
 */
@Entity
@Table(indexes = {
        @Index(name = "index_lastTradeDate", columnList = "lastTradeDate")
})
public class TimeSeriesData implements Serializable {
    @Id
    private Long id;
    private String lastTradeDate;
    private double closePrice;
    private double openPrice;
    private double highPrice;
    private double lowPrice;
    private double amt;
    private long volume;

    public TimeSeriesData() {
    }

    public TimeSeriesData(String value) {
        String[] values = value.split(",");
    }

    public String getLastTradeDate() {
        return lastTradeDate;
    }

    public void setLastTradeDate(String lastTradeDate) {
        this.lastTradeDate = lastTradeDate;
    }

    public double getClosePrice() {
        return closePrice;
    }

    public void setClosePrice(double closePrice) {
        this.closePrice = closePrice;
    }

    public double getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(double openPrice) {
        this.openPrice = openPrice;
    }

    public double getHighPrice() {
        return highPrice;
    }

    public void setHighPrice(double highPrice) {
        this.highPrice = highPrice;
    }

    public double getLowPrice() {
        return lowPrice;
    }

    public void setLowPrice(double lowPrice) {
        this.lowPrice = lowPrice;
    }

    public double getAmt() {
        return amt;
    }

    public void setAmt(double amt) {
        this.amt = amt;
    }

    public long getVolume() {
        return volume;
    }

    public void setVolume(long volume) {
        this.volume = volume;
    }
}
