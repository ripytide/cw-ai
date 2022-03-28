package uk.ac.bris.cs.scotlandyard.ui.ai;

import io.atlassian.fugue.Pair;
import uk.ac.bris.cs.scotlandyard.model.Board;
import uk.ac.bris.cs.scotlandyard.model.Move;

import javax.annotation.Nonnull;
import java.util.LinkedList;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class MinMax implements MovePicker {
    @Override
    public Move selectionAlgorithm(@Nonnull Board board, Pair<Long, TimeUnit> timeoutPair, Score scoringMethod) {
        Move bestMove;
        LinkedList<Move> moveQueue = new LinkedList<>(board.getAvailableMoves());
    }

    private static Optional<Float> miniMax(@Nonnull Board.GameState gameState, Pair<Long, TimeUnit> timeoutPair, Score scoringMethod, boolean maxing, Integer depth) {
        if (stillGotTime) {
            if (depth <= 0 || !gameState.getWinner().isEmpty()) {
                return Optional.of(scoringMethod.score(gameState));
            }
            Float bestValue = maxing ? 0f : 1f;
            for (Move move : gameState.getAvailableMoves()) {
                Optional<Float> value = miniMax(gameState.advance(move), timeoutPair, scoringMethod, !maxing, depth--);
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
