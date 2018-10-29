package utf8.citicup.domain.entity;

public class RecommendOption2 {
    private Option option;
    private double iK;
    private String[][] graph;
    private double iNum;
    private double pAsset;


    private int n0;
    private double a;
    private double sExp;


    public RecommendOption2(Option option, double iK, double iNum, String[][] graph, double pAsset, int n0, double a, double sExp) {
        this.option = option;
        this.iK = iK;
        this.graph = graph;
        this.iNum = iNum;
        this.pAsset = pAsset;
        this.n0 = n0;
        this.a = a;
        this.sExp = sExp;
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

    public double getpAsset() {
        return pAsset;
    }

    public int getN0() {
        return n0;
    }

    public double getA() {
        return a;
    }

    public double getsExp() {
        return sExp;
    }

}
