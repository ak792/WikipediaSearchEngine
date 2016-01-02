package pageranker;

import com.google.common.collect.ImmutableList;
import corpus.Document;
import graph.Graph;
import graph.GraphNode;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PageRankerTest {

    private final double MARGIN_OF_ERROR_DOUBLE = 0.0002;

    @Test
    public void test_assignPageRanks_GraphStructure1() {
        final PageRankableGraphNode<Object> nodeA = getPageRankableGraphNode(1);
        final PageRankableGraphNode<Object> nodeB = getPageRankableGraphNode(2);
        final PageRankableGraphNode<Object> nodeC = getPageRankableGraphNode(3);
        final PageRankableGraphNode<Object> nodeD = getPageRankableGraphNode(4);

        assignEdgesGraphStructure1(ImmutableList.of(nodeA, nodeB, nodeC, nodeD));


        final PageRanker pageRanker = new PageRanker();

        Graph<Integer, Object> graph = new Graph<>();
        graph.addNode(1, nodeA);
        graph.addNode(2, nodeB);
        graph.addNode(3, nodeC);
        graph.addNode(4, nodeD);

        graph = pageRanker.assignPageRanks(graph);

        Assert.assertEquals(0.3726, nodeA.getPageRank(), MARGIN_OF_ERROR_DOUBLE);
        Assert.assertEquals(0.1958, nodeB.getPageRank(), MARGIN_OF_ERROR_DOUBLE);
        Assert.assertEquals(0.3942, nodeC.getPageRank(), MARGIN_OF_ERROR_DOUBLE);
        Assert.assertEquals(0.0375, nodeD.getPageRank(), MARGIN_OF_ERROR_DOUBLE);
    }

    @Test
    public void test_assignPageRanks_GraphStructure2() {
        final PageRankableGraphNode<Object> nodeA = getPageRankableGraphNode(1);
        final PageRankableGraphNode<Object> nodeB = getPageRankableGraphNode(2);

        assignEdgesGraphStructure2(ImmutableList.of(nodeA, nodeB));

        final PageRanker pageRanker = new PageRanker();

        Graph<Integer, Object> graph = new Graph<>();
        graph.addNode(1, nodeA);
        graph.addNode(2, nodeB);

        graph = pageRanker.assignPageRanks(graph);

        Assert.assertEquals(0.5, nodeA.getPageRank(), MARGIN_OF_ERROR_DOUBLE);
        Assert.assertEquals(0.5, nodeB.getPageRank(), MARGIN_OF_ERROR_DOUBLE);
    }

    @Test
    public void test_assignPageRanks_GraphStructure3() {
        final PageRankableGraphNode<Object> nodeA = getPageRankableGraphNode(1);
        final PageRankableGraphNode<Object> nodeB = getPageRankableGraphNode(2);
        final PageRankableGraphNode<Object> nodeC = getPageRankableGraphNode(3);

        assignEdgesGraphStructure3(ImmutableList.of(nodeA, nodeB, nodeC));

        final PageRanker pageRanker = new PageRanker();

        Graph<Integer, Object> graph = new Graph<>();
        graph.addNode(1, nodeA);
        graph.addNode(2, nodeB);
        graph.addNode(3, nodeC);

        graph = pageRanker.assignPageRanks(graph);

        Assert.assertEquals(0.1448, nodeA.getPageRank(), MARGIN_OF_ERROR_DOUBLE);
        Assert.assertEquals(0.1115, nodeB.getPageRank(), MARGIN_OF_ERROR_DOUBLE);
        Assert.assertEquals(0.1115, nodeC.getPageRank(), MARGIN_OF_ERROR_DOUBLE);
    }

    private <T> void assignEdgesGraphStructure1(final List<? extends GraphNode<T>> pageRankables) {
        if (pageRankables.size() < 4) {
            throw new IllegalArgumentException("Not big enough");
        }

        final GraphNode<T> nodeA = pageRankables.get(0);
        final GraphNode<T> nodeB = pageRankables.get(1);
        final GraphNode<T> nodeC = pageRankables.get(2);
        final GraphNode<T> nodeD = pageRankables.get(3);

        nodeA.addOutgoingEdgeTo(nodeB);
        nodeB.addIncomingEdgeFrom(nodeA);

        nodeA.addOutgoingEdgeTo(nodeC);
        nodeC.addIncomingEdgeFrom(nodeA);

        nodeB.addOutgoingEdgeTo(nodeC);
        nodeC.addIncomingEdgeFrom(nodeB);

        nodeC.addOutgoingEdgeTo(nodeA);
        nodeA.addIncomingEdgeFrom(nodeC);

        nodeD.addOutgoingEdgeTo(nodeC);
        nodeC.addIncomingEdgeFrom(nodeD);
    }

    private <T> void assignEdgesGraphStructure2(final List<? extends GraphNode<T>> pageRankables) {
        if (pageRankables.size() < 2) {
            throw new IllegalArgumentException("Not big enough");
        }

        final GraphNode<T> nodeA = pageRankables.get(0);
        final GraphNode<T> nodeB = pageRankables.get(1);

        nodeA.addOutgoingEdgeTo(nodeB);
        nodeB.addIncomingEdgeFrom(nodeA);

        nodeB.addOutgoingEdgeTo(nodeA);
        nodeA.addIncomingEdgeFrom(nodeB);
    }

    private <T> void assignEdgesGraphStructure3(final List<? extends GraphNode<T>> pageRankables) {
        if (pageRankables.size() < 3) {
            throw new IllegalArgumentException("Not big enough");
        }

        final GraphNode<T> nodeA = pageRankables.get(0);
        final GraphNode<T> nodeB = pageRankables.get(1);
        final GraphNode<T> nodeC = pageRankables.get(2);

        nodeA.addOutgoingEdgeTo(nodeB);
        nodeB.addIncomingEdgeFrom(nodeA);

        nodeB.addOutgoingEdgeTo(nodeA);
        nodeA.addIncomingEdgeFrom(nodeB);

        nodeA.addOutgoingEdgeTo(nodeC);
        nodeC.addIncomingEdgeFrom(nodeA);
    }

    private PageRankableGraphNode<Object> getPageRankableGraphNode(final int docId) {
        return new PageRankableGraphNode<>(new Document(docId, Arrays.asList("lady", "fair", "my", "pig")));
    }
}
