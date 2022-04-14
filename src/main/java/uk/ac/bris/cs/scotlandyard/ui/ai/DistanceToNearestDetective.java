package uk.ac.bris.cs.scotlandyard.ui.ai;

import java.util.List;

public class DistanceToNearestDetective implements Score {
    @Override
    public Float score(CustomGameState gameState) {
        List<Float> distancesToMrX = gameState.getDistancesBetweenMrXAndDetectives();

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