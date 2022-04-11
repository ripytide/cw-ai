package uk.ac.bris.cs.scotlandyard.ui.ai;

import uk.ac.bris.cs.scotlandyard.model.Board;
import uk.ac.bris.cs.scotlandyard.model.Piece;
import uk.ac.bris.cs.scotlandyard.model.Player;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class DistanceToNearestDetective implements Score {
    @Override
    public Float score(CustomGameState gameState) {
        Integer mrXLocation = gameState.mrX.location();
        List<Integer> detectiveLocations = gameState.detectives.stream().map(d -> d.location()).toList();
        List<Float> distancesToMrX = detectiveLocations.stream()
                .map(d -> GraphHelper.distanceBetweenLocations(gameState.getSetup().graph, d, mrXLocation))
                .toList();

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
