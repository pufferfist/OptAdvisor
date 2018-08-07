package utf8.citicup.service;


import utf8.citicup.domain.entity.*;

public interface RecommendService {

    public ResponseMsg recommendPortfolio(double M0, double k, double a, int T, char combination, double p1, double p2, double sigma1, double sigma2);
    public ResponseMsg heging(int N0, double a, double s_exp, int T);
    public ResponseMsg customPortfolio(Option[] list);
}
