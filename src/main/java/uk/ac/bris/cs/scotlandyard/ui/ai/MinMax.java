package uk.ac.bris.cs.scotlandyard.ui.ai;

import uk.ac.bris.cs.scotlandyard.model.Move;
import javax.annotation.Nonnull;
import java.time.Instant;
import java.util.Optional;

public class MinMax implements AccuracyBasedMetaScore {
    public Optional<Float> score(@Nonnull CustomGameState gameState, Long endTime, Score scoringMethod, Integer depth) {
        boolean maxing = gameState.isMrXTurn();
        Long currentTime = Instant.now().toEpochMilli();
        if (currentTime < endTime) {
            //recursion base case
            if (depth <= 0 || !gameState.getWinner().isEmpty()) {
                return Optional.of(scoringMethod.score(gameState));
            }
            Float bestValue = maxing ? 0f : 1f;
            for (Move move : gameState.getAvailableMoves()) {
                Optional<Float> value = score(gameState.advance(move), endTime, scoringMethod, depth--);
                if (value.isEmpty()) {
                    return Optional.empty();
                } else {
                    bestValue = maxing ? Math.max(bestValue, value.get()) : Math.min(bestValue, value.get());
                }
            }
            return Optional.of(bestValue);
        }
        return Optional.empty();
    }
}
