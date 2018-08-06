package utf8.citicup.service;


import utf8.citicup.domain.entity.Option;
import utf8.citicup.domain.entity.RecommendOption1;
import utf8.citicup.domain.entity.RecommendOption2;
import utf8.citicup.domain.entity.RecommendOption3;

public interface RecommendService {
    public RecommendOption1 recommendPortfolio(double M0, double k, int T, char combination, double p1, double p2, double sigma1, double sigma2);
    public RecommendOption2 heging(int N0, double a, double s_exp, int T);
    public RecommendOption3 customPortfolio(Option[] list);
}
