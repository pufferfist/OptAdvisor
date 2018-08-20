package utf8.citicup.domain.entity;

public class RecommendOption2 {
    private Option option;
    private double iK;
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
}
