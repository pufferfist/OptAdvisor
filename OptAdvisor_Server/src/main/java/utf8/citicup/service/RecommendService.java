package utf8.citicup.service;


import org.springframework.stereotype.Service;
import utf8.citicup.domain.entity.*;

@Service
public interface RecommendService {

    public ResponseMsg recommendPortfolio(double M0, double k, String T, char combination, double p1, double p2, double sigma1, double sigma2, int w1, int w2);
    public ResponseMsg hedging(int N0, double a, double s_exp, String T);
    public ResponseMsg customPortfolio(Option[] list);
}
