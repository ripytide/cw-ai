package uk.ac.bris.cs.scotlandyard.ui.ai;
import java.util.Optional;
public interface AccuracyBasedMetaScore {
    //will return None if it runs out of time, or a score otherwise.
    Optional<Float> score(CustomGameState gameState, Long endTime, Score score, Integer accuracy);
}
