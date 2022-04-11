package uk.ac.bris.cs.scotlandyard.ui.ai;

import com.google.common.collect.ImmutableSet;
import com.google.common.graph.*;
import uk.ac.bris.cs.scotlandyard.model.ScotlandYard;

public class GraphHelper {
    public static Float distanceBetweenLocations(ImmutableValueGraph<Integer, ImmutableSet<ScotlandYard.Transport>> graph, Integer location1, Integer location2) {
        MutableValueGraph<Integer, Float> valueAs1Graph = ValueGraphBuilder.undirected().build();
        for (Integer node : graph.nodes()) {
            valueAs1Graph.addNode(node);
        }

        for (EndpointPair<Integer> edge : graph.edges()) {
            valueAs1Graph.putEdgeValue(edge.nodeU(), edge.nodeV(), 1f);
        }

        ImmutableValueGraph<Integer, Float> immutableGraph = ImmutableValueGraph.copyOf(valueAs1Graph);

        return Dijkstra.dijkstra(immutableGraph, location1, location2);
    }
}