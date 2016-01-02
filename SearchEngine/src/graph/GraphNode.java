package graph;

import java.util.HashSet;
import java.util.Set;

public class GraphNode<T> {

    private final Set<GraphNode<T>> outgoingEdgeNodes;
    private final Set<GraphNode<T>> incomingEdgeNodes;

    private final T contents;

    public GraphNode(final T contents) {
        if (contents == null) {
            throw new IllegalArgumentException();
        }

        outgoingEdgeNodes = new HashSet<>();
        incomingEdgeNodes = new HashSet<>();

        this.contents = contents;
    }

    public T getContents() {
        return contents;
    }


    public Set<GraphNode<T>> getOutgoingEdgeNodes() {
        //copy?
        return outgoingEdgeNodes;
    }

    public Set<GraphNode<T>> getIncomingEdgeNodes() {
        //copy?
        return incomingEdgeNodes;
    }

    public int getNumOutgoingEdges() {
        return outgoingEdgeNodes.size();
    }

    public void addOutgoingEdgeTo(final GraphNode<T> graphNode) {
        outgoingEdgeNodes.add(graphNode);
    }

    public void addIncomingEdgeFrom(final GraphNode<T> graphNode) {
        incomingEdgeNodes.add(graphNode);
    }

    @Override
    public String toString() {
        return "<GraphNode " + contents.toString() + ">";
    }
}
