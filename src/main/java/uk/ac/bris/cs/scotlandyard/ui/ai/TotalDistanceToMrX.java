package uk.ac.bris.cs.scotlandyard.ui.ai;

import java.util.List;

public class TotalDistanceToMrX implements Score {

    @Override
    public Float score(CustomGameState gameState) {
        Integer mrXLocation = gameState.mrX.location();
        List<Integer> detectiveLocations = gameState.detectives.stream().map(d -> d.location()).toList();
        List<Float> distancesToMrX = detectiveLocations.stream()
                .map(d -> GraphHelper.distanceBetweenLocations(gameState.getSetup().graph, d, mrXLocation))
                .toList();

        return distancesToMrX.stream().
        return min;
    }
}
