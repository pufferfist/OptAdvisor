package utf8.citicup.domain.entity;

public class RecommendOption1 {
    private Option[] optionList;
    private int[] buyAndSell;
    private int num;//组合的分数
    private double cost;//成本p1-p2
    private double bond;//保证金
    private double z_delta;
    private double z_gamma;
    private double z_vega;
    private double z_theta;
    private double z_rho;
    private double EM;//组合的期望收益率
    private double beta;//组合风险值
    private double returnOnAssets;//资产期望收益率
    private String[][] Graph;
    private String[][] assertPrice2Profit;
    private String[][] profit2Probability;
    private String[][] historyProfit2Probability;

    private double M0;
    private double k;
    private double sigma1;
    private double sigma2;
    private double p1;
    private double p2;

    public RecommendOption1(Option[] optionList, int[] buyAndSell, int num, double cost, double bond, double z_delta,
                            double z_gamma, double z_vega, double z_theta, double z_rho, double EM,
                            double returnOnAssets, double beta, String[][] assertPrice2Profit,String[][] profit2Probability,
                            String[][] historyProfit2Probability, double m0, double k) {
        this.optionList = optionList;
        this.buyAndSell = buyAndSell;
        this.num = num;
        this.cost = cost;
        this.bond = bond;
        this.z_delta = z_delta;
        this.z_gamma = z_gamma;
        this.z_vega = z_vega;
        this.z_theta = z_theta;
        this.z_rho = z_rho;
        this.EM = EM;
        this.returnOnAssets = returnOnAssets;
        this.beta = beta;
        this.M0 = m0;
        this.k = k;
        this.assertPrice2Profit = assertPrice2Profit;
        this.profit2Probability = profit2Probability;
        this.historyProfit2Probability = historyProfit2Probability;
    }

    public RecommendOption1(Option[] optionList, int[] buyAndSell, int num, double cost, double bond, double z_delta,
                            double z_gamma, double z_vega, double z_theta, double z_rho, double EM,
                            double returnOnAssets, double beta, double m0, double k,
                            double sigma1, double sigma2, double p1, double p2) {
        this.optionList = optionList;
        this.buyAndSell = buyAndSell;
        this.num = num;
        this.cost = cost;
        this.bond = bond;
        this.z_delta = z_delta;
        this.z_gamma = z_gamma;
        this.z_vega = z_vega;
        this.z_theta = z_theta;
        this.z_rho = z_rho;
        this.EM = EM;
        this.returnOnAssets = returnOnAssets;
        this.beta = beta;
        M0 = m0;
        this.k = k;
        this.sigma1 = sigma1;
        this.sigma2 = sigma2;
        this.p1 = p1;
        this.p2 = p2;
    }

    public RecommendOption1(Option[] optionList, int[] buyAndSell, int num, double cost, double bond, double z_delta,
                            double z_gamma, double z_vega, double z_theta, double z_rho, double EM,
                            double returnOnAssets, double beta, String[][] assertPrice2Profit, String[][] profit2Probability,
                            String[][] historyProfit2Probability, double m0, double k, double sigma1, double sigma2,
                            double p1, double p2) {
        this.optionList = optionList;
        this.buyAndSell = buyAndSell;
        this.num = num;
        this.cost = cost;
        this.bond = bond;
        this.z_delta = z_delta;
        this.z_gamma = z_gamma;
        this.z_vega = z_vega;
        this.z_theta = z_theta;
        this.z_rho = z_rho;
        this.EM = EM;
        this.beta = beta;
        this.returnOnAssets = returnOnAssets;
        this.assertPrice2Profit = assertPrice2Profit;
        this.profit2Probability = profit2Probability;
        this.historyProfit2Probability = historyProfit2Probability;
        M0 = m0;
        this.k = k;
        this.sigma1 = sigma1;
        this.sigma2 = sigma2;
        this.p1 = p1;
        this.p2 = p2;
    }


    public Option[] getOptionList() {
        return optionList;
    }

    public int[] getBuyAndSell() {
        return buyAndSell;
    }

    public int getNum() {
        return num;
    }

    public String[][] getGraph() {
        return Graph;
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

    public double getM0() {
        return M0;
    }

    public double getK() {
        return k;
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

    public String[][] getAssertPrice2Profit() {
        return assertPrice2Profit;
    }

    public String[][] getProfit2Probability() {
        return profit2Probability;
    }

    public String[][] getHistoryProfit2Probability() {
        return historyProfit2Probability;
    }
}