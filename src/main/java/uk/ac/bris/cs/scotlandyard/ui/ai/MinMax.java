package uk.ac.bris.cs.scotlandyard.ui.ai;

import io.atlassian.fugue.Pair;
import uk.ac.bris.cs.scotlandyard.model.Board;
import uk.ac.bris.cs.scotlandyard.model.Move;

import javax.annotation.Nonnull;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class MinMax implements MovePicker {
    @Override
    public Move selectionAlgorithm(@Nonnull Board board, Pair<Long, TimeUnit> timeoutPair, Score scoringMethod) {
        Long currentTime = Instant.now().toEpochMilli();
        Long endTime = currentTime + timeoutPair.right().toMillis(timeoutPair.left()) - 500L;

        //Move List for debug purposes
        ArrayList<Pair<Pair<Move, Float>, Integer>> moveScoresDepths = new ArrayList<>();

        Integer currentDepth = 0;
        Move stableBestMove = board.getAvailableMoves().asList().get(0);
        boolean stillGotTime = true;
        //while (stillGotTime) {
            Optional<Move> unstableBestMove = Optional.empty();
            Float bestScore = 0f;
            for (Move move : board.getAvailableMoves()) {
                Optional<Float> currentScore = miniMax(((Board.GameState) board).advance(move), endTime, scoringMethod, true, 1);
                if (currentScore.isEmpty()){
                    stillGotTime = false;
                } else if (currentScore.get() > bestScore) {
                    unstableBestMove = Optional.of(move);
                    bestScore = currentScore.get();
                }

                if (currentScore.isPresent()) {
                    moveScoresDepths.add(new Pair<>(new Pair<>(move, currentScore.get()), currentDepth));
                }
            }

            stableBestMove = unstableBestMove.get();
            currentDepth++;
        //}
        //DEBUGGING
        System.out.println("DEPTH: "+currentDepth);
        System.out.println("BESTMOVE: "+stableBestMove);
        System.out.println("MOVEDEPTHSCORES:"+moveScoresDepths);

        return stableBestMove;
    }

    private static Optional<Float> miniMax(@Nonnull Board.GameState gameState, Long endTime, Score scoringMethod, boolean maxing, Integer depth) {
        Long currentTime = Instant.now().toEpochMilli();
        if (currentTime < endTime) {
            if (depth <= 0 || !gameState.getWinner().isEmpty()) {
                return Optional.of(scoringMethod.score(gameState));
            }
            Float bestValue = maxing ? 0f : 1f;
            for (Move move : gameState.getAvailableMoves()) {
                Optional<Float> value = miniMax(gameState.advance(move), endTime, scoringMethod, !maxing, depth--);
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
