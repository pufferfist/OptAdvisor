package utf8.citicup.domain.entity;

public class RecommendOption1 {
    private Option[] optionList;
    private int[] buyAndSell;
    private int num;
    private double cost;//成本p1-p2
    private double bond;//保证金
    private double z_delta;
    private double z_gamma;
    private double z_vega;
    private double z_theta;
    private double z_rho;
    private double EM;//组合的期望收益率
    private double beta;//组合风险值
    private String[][] Graph;

    public RecommendOption1(Option[] optionList, int[] buyAndSell, int num, double cost, double bond, double z_delta, double z_gamma, double z_vega, double z_theta, double z_rho, double EM, double beta, String[][] graph) {
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
        Graph = graph;
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

    public String[][] getGraph() {
        return Graph;
    }
}
