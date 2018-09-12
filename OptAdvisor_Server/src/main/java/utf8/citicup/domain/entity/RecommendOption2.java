package utf8.citicup.domain.entity;

public class RecommendOption2 {
    private Option option;
    private double iK;
    private String[][] graph;
    private double iNum;
    private double pAsset;

    public RecommendOption2(Option option, double iK, double iNum, String[][] graph, double pAsset) {
        this.option = option;
        this.iK = iK;
        this.graph = graph;
        this.iNum = iNum;
        this.pAsset = pAsset;
    }

    public Option getOption() {
        return option;
    }


    public String[][] getGraph() {
        return graph;
    }

    public double getiK() {
        return iK;
    }


    public double getiNum() { return iNum; }
}
