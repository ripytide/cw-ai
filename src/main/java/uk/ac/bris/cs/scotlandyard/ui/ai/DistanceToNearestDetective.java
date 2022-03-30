package uk.ac.bris.cs.scotlandyard.ui.ai;

import com.google.common.graph.*;
import uk.ac.bris.cs.scotlandyard.model.Board;
import uk.ac.bris.cs.scotlandyard.model.Piece;
import uk.ac.bris.cs.scotlandyard.model.Player;

import java.util.List;
import java.util.stream.Collectors;

public class DistanceToNearestDetective implements Score {
    @Override
    public Float score(CustomGameState gameState) {
        MutableValueGraph<Integer, Float> valueAs1Graph = ValueGraphBuilder.undirected().build();
        for (Integer node : gameState.getSetup().graph.nodes()) {
            valueAs1Graph.addNode(node);
        }

        for (EndpointPair<Integer> edge : gameState.getSetup().graph.edges()) {
            valueAs1Graph.putEdgeValue(edge.nodeU(), edge.nodeV(), 1f);
        }

        ImmutableValueGraph<Integer, Float> immutableGraph = ImmutableValueGraph.copyOf(valueAs1Graph);

        Integer mrXLocation = gameState.mrX.location();
        List<Integer> detectiveLocations = gameState.detectives.stream().map(d -> d.location()).toList();
        List<Float> distancesToMrX = detectiveLocations.stream().map(d -> Dijkstra.dijkstra(immutableGraph, d, mrXLocation)).toList();

        //get smallest
        Float min = distancesToMrX.get(0);
        for (Float distance : distancesToMrX) {
            if (distance < min) {
                min = distance;
            }
        }
        return min;
    }
}
