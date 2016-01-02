package pageranker;

import graph.GraphNode;

public class PageRankableGraphNode<T> extends GraphNode<T> {

    private double pageRank;

    public PageRankableGraphNode(final T contents) {
        super(contents);
        pageRank = -1; //uninitialized
    }

    public double getPageRank() {
        return pageRank;
    }

    public void setPageRank(final double pageRank) {
        this.pageRank = pageRank;
    }
}
