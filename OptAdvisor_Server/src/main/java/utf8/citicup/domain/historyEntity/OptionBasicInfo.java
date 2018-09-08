package utf8.citicup.domain.historyEntity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

/**
 * 对应option50etf_basic_info表
 */
@Entity
@Table(indexes = {
        @Index(name = "index_code_name", columnList = "codeName")
})
public class OptionBasicInfo {
    @Id
    private String codeName;//期权代码
    private boolean type;//行权方式 true：认购 false:认沽
    private String abbr;//股票英文简称
    private String listDate;//上市日期
    private String endDate;//最后行权日期
    private double price;//行权价格
    private int multiplier;//合约乘数
    private int duration;//总存续期
    private String tradeDate;//起始交易日期

    public String getCodeName() {
        return codeName;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

    public boolean isType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    public String getAbbr() {
        return abbr;
    }

    public void setAbbr(String abbr) {
        this.abbr = abbr;
    }

    public String getListDate() {
        return listDate;
    }

    public void setListDate(String listDate) {
        this.listDate = listDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(int multiplier) {
        this.multiplier = multiplier;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(String tradeDate) {
        this.tradeDate = tradeDate;
    }
}
