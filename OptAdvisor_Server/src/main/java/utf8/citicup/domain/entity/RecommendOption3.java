package utf8.citicup.domain.entity;

public class RecommendOption3 extends RecommendOption1 {
    double delta;
    double gamma;
    double theta;
    double vega;
    double rho;

    public RecommendOption3(Option[] optionList, int[] buyAndSell, int num, double cost, double bond, double z_delta, double z_gamma, double z_vega, double z_theta, double z_rho, double EM, double beta, String[][] graph) {
        super(optionList, buyAndSell, num, cost, bond, z_delta, z_gamma, z_vega, z_theta, z_rho, EM, beta, graph);
    }
}
