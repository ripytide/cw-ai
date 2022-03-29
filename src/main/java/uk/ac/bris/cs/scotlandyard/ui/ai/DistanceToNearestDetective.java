package uk.ac.bris.cs.scotlandyard.ui.ai;

import com.google.common.graph.*;
import uk.ac.bris.cs.scotlandyard.model.Board;
import uk.ac.bris.cs.scotlandyard.model.Piece;
import uk.ac.bris.cs.scotlandyard.model.Player;

import java.util.List;
import java.util.stream.Collectors;

public class DistanceToNearestDetective implements Score {
    @Override
    public Float score(Board board) {
        MutableValueGraph<Integer, Float> valueAs1Graph = ValueGraphBuilder.undirected().build();
        for (Integer node : board.getSetup().graph.nodes()) {
            valueAs1Graph.addNode(node);
        }

        for (EndpointPair<Integer> edge : board.getSetup().graph.edges()) {
            valueAs1Graph.putEdgeValue(edge.nodeU(), edge.nodeV(), 1f);
        }

        ImmutableValueGraph<Integer, Float> immutableGraph = ImmutableValueGraph.copyOf(valueAs1Graph);

        List<Piece> detectives = board.getPlayers().stream().filter(Piece::isDetective).toList();
        Integer moveNumber = board.getMrXTravelLog().size();
        Integer mrXLocation = board.getMrXTravelLog().get(moveNumber - 1).location().get();
        List<Integer> detectiveLocations = detectives.stream().map(p -> board.getDetectiveLocation((Piece.Detective) p).get()).toList();
        List<Float> distancesToMrX = detectiveLocations.stream().map(d -> Dijkstra.dijkstra(immutableGraph, d, mrXLocation)).toList();
        Float min = distancesToMrX.get(0);

        for (Float distance : distancesToMrX) {
            if (distance < min) {
                min = distance;
            }
        }

        return min;
    }
}
