package uk.ac.bris.cs.scotlandyard.ui.ai;

import java.util.List;

public class TotalDistanceToMrX implements Score {

    @Override
    public Float score(CustomGameState gameState) {
        List<Float> distances = gameState.getDistancesBetweenMrXAndDetectives();
        if(distances.stream().reduce(0f, Float::sum) == 1.0f){
            System.out.println(distances);
        }
        return gameState.getDistancesBetweenMrXAndDetectives().stream().reduce(0f, Float::sum);
    }
}