package pageranker;

import graph.Graph;

public class PageRanker {

    private static final double DEFAULT_DAMPENING_FACTOR = 0.85;

    private final double dampeningFactor;
    private final double MAX_AVERAGE_CHANGE_IN_PAGE_RANK_AFTER_CONVERGING = 0.0001;

    public PageRanker() {
        this(DEFAULT_DAMPENING_FACTOR);
    }

    public PageRanker(final double dampeningFactor) {
        this.dampeningFactor = dampeningFactor;
    }

    public <T> Graph<Integer, T> assignPageRanks(final Graph<Integer, T> pageRankableGraph) {
        if (pageRankableGraph == null) {
            throw new IllegalArgumentException("Don't pass a null pageRankableGraph to assignPageRanks");
        }

        //prevents division by zero (although it wouldn't throw an Exception anymore)
        if (pageRankableGraph.isEmpty()) {
            return pageRankableGraph;
        }

        initializePageRanks(pageRankableGraph);
        updatePageRanksUntilConverge(pageRankableGraph);

        return pageRankableGraph;
    }

    private <T> Graph<Integer, T> initializePageRanks(final Graph<Integer, T> pageRankables) {
        final double initialPageRank = 1.0 / pageRankables.size();
        pageRankables.getNodes().stream()
                .map(graphNode -> (PageRankableGraphNode) graphNode)
                .forEach(pageRankable -> pageRankable.setPageRank(initialPageRank));

        return pageRankables;
    }

    private <T> Graph<Integer, T> updatePageRanksUntilConverge(final Graph<Integer, T> pageRankables) {
        double averageChangeInPageRank = Double.MAX_VALUE;
        while (!hasPageRanksConverged(averageChangeInPageRank)) {
            averageChangeInPageRank = updatePageRanks(pageRankables);
        }

        return pageRankables;
    }

    private boolean hasPageRanksConverged(final double averageChangeInPageRank) {
        return averageChangeInPageRank <= MAX_AVERAGE_CHANGE_IN_PAGE_RANK_AFTER_CONVERGING;
    }

    //returns avg change in pagerank
    private <T> double updatePageRanks(final Graph<Integer, T> pageRankables) {
        return pageRankables.getNodes().stream()
                .map(graphNode -> (PageRankableGraphNode) graphNode)
                .mapToDouble(pageRankable -> {
                    final double oldPageRank = pageRankable.getPageRank();

                    final double newPageRank = calculatePageRank(pageRankable, pageRankables.size());
                    pageRankable.setPageRank(newPageRank);

                    final double changeInPageRank = Math.abs(newPageRank - oldPageRank);

//                    System.out.println(String.format("Changed PageRank of %s from %s to %s", pageRankable, oldPageRank, newPageRank));

                    return changeInPageRank;
                })
                .average()
                .getAsDouble();
    }

    private <T> double calculatePageRank(final PageRankableGraphNode<T> pageRankableGraphNode, final int numNodesInGraph) {

        final double incomingEdgeNodesPageRankDividedByNumOutgoingEdgesSum =
                pageRankableGraphNode.getIncomingEdgeNodes()
                                    .stream()
                                    .map(incomingEdgeNode -> (PageRankableGraphNode<T>) incomingEdgeNode)
                                    .mapToDouble(incomingEdgePageRankableNode ->
                                            incomingEdgePageRankableNode.getPageRank() / incomingEdgePageRankableNode.getNumOutgoingEdges())
                                    .sum();

        return (1.0 - dampeningFactor) / numNodesInGraph
                + dampeningFactor * incomingEdgeNodesPageRankDividedByNumOutgoingEdgesSum;
    }
}
