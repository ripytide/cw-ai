package uk.ac.bris.cs.scotlandyard.ui.ai;

import java.util.List;

public class TotalDistanceToMrX implements Score {

    @Override
    public Float score(CustomGameState gameState) {
        List<Float> distances = gameState.getDistancesBetweenMrXAndDetectives();
        
        return gameState.getDistancesBetweenMrXAndDetectives().stream().reduce(0f, Float::sum);
    }
}