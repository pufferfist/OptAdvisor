package utf8.citicup.domain.entity;


import utf8.citicup.domain.entity.Option;

public class RecommendOption1 {
    Option[] optionList;
    double cost;//成本p1-p2
    double bond;//保证金
    double z_delta;
    double z_gamma;
    double z_vega;
    double z_theta;
    double z_rho;
    double EM;//组合的期望收益率
    double beta;//组合风险值
    double[][] Graph;
}
