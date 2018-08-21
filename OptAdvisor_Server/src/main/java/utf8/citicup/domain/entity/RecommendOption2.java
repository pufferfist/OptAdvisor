package utf8.citicup.domain.entity;

public class RecommendOption2 {
    private Option option;
    private double iK;
    private double N;
    private double pAsset;
    private String[][] graph;

    public RecommendOption2(Option option, double iK, String[][] graph) {
        this.option = option;
        this.iK = iK;
        this.graph = graph;
    }

    public Option getOption() {
        return option;
    }

    public double getIK() {
        return iK;
    }

    public String[][] getGraph() {
        return graph;
    }

    public double getiK() {
        return iK;
    }

    public double getN() {
        return N;
    }

    public double getpAsset() {
        return pAsset;
    }
}
