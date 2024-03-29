package uk.ac.bris.cs.scotlandyard.ui.ai;

import io.atlassian.fugue.Pair;
import uk.ac.bris.cs.scotlandyard.model.Board;
import uk.ac.bris.cs.scotlandyard.model.Move;

import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class TimeBasedMovePicker implements MovePicker{
    private final AccuracyBasedMetaScore metaScore;
    
    public TimeBasedMovePicker(AccuracyBasedMetaScore metaScore){
        this.metaScore = metaScore;
    }
    
    @Override
    public Move selectionAlgorithm(CustomGameState gameState, Board board, Pair<Long, TimeUnit> timeoutPair) {
        Long currentTime = Instant.now().toEpochMilli();
        Long endTime = currentTime + timeoutPair.right().toMillis(timeoutPair.left()) - 500L;
        
        Integer currentDepth = 0;
        Move stableBestMove = board.getAvailableMoves().asList().get(0);
        boolean stillGotTime = true;
        while (stillGotTime) {
            Optional<Move> unstableBestMove = Optional.empty();
            Float bestScore = gameState.isMrXTurn() ? Float.NEGATIVE_INFINITY : Float.POSITIVE_INFINITY;
            for (Move move : board.getAvailableMoves()) {
                Optional<Float> currentScore = metaScore.score(gameState.advance(move), endTime, currentDepth);
                if (currentScore.isEmpty()) {
                    //if we run out of time on a specific depth discard the current best move as
                    //it is better to take to complete best move of a prior depth
                    unstableBestMove = Optional.empty();
                    stillGotTime = false;
                } else if (gameState.isMrXTurn() ? currentScore.get() > bestScore : currentScore.get() < bestScore) {
                    unstableBestMove = Optional.of(move);
                    bestScore = currentScore.get();
                }
            }

            if (unstableBestMove.isPresent()) {
                stableBestMove = unstableBestMove.get();
                currentDepth++;
            }
        }

        return stableBestMove;
    }
}
