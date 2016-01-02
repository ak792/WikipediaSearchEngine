package graph;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Graph<S, T> {


    private final Map<S, GraphNode<T>> nodes;

    public Graph() {
        nodes = new HashMap<>();
    }


    public void addNode(final S hashableKey, final GraphNode<T> graphNode) {
        if (containsNode(hashableKey)) {
            throw new IllegalArgumentException("Node for key " + hashableKey + " is already in the Graph");
        }

        nodes.put(hashableKey, graphNode);
    }

    public boolean containsNode(final S hashableKey) {
        return nodes.containsKey(hashableKey);
    }

    public GraphNode<T> getNode(final S hashableKey) {
        return nodes.get(hashableKey);
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public int size() {
        return nodes.size();
    }

    public Collection<GraphNode<T>> getNodes() {
        return nodes.values();
    }


}
