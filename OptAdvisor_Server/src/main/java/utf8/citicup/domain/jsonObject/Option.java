package utf8.citicup.domain.jsonObject;

/**
 * 期权实体
 */
public class Option {
    String id;
    String name;//例如:50ETF购8月2600
    int type;//1为买入0为卖出
    int property;//1为看涨0为看跌
    String expireTime;//到期时间
    double executionPrice;//执行价格
    double transactionPrice;//成交价
    int quantity;//在组合中的份数,单独存在无意义
    double delta;
    double gamma;
    double vega;
    double theta;
    double rho;
    double beta;
}
