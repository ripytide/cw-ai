package uk.ac.bris.cs.scotlandyard.ui.ai;

public class TotalDistanceToMrX implements Score {

    @Override
    public Float score(CustomGameState gameState) {
        return gameState.getDistancesBetweenMrXAndDetectives().stream().reduce(0f, Float::sum);
    }
}