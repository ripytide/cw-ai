package uk.ac.bris.cs.scotlandyard.ui.ai;

import io.atlassian.fugue.Pair;
import uk.ac.bris.cs.scotlandyard.model.Ai;
import uk.ac.bris.cs.scotlandyard.model.Board;
import uk.ac.bris.cs.scotlandyard.model.Move;

import javax.annotation.Nonnull;
import java.util.concurrent.TimeUnit;

public class AISimpleScoreMinimax implements Ai {
    @Nonnull
    @Override
    public String name() {
        return "AISimpleScoreMinimax";
    }

    @Nonnull
    @Override
    public Move pickMove(@Nonnull Board board, Pair<Long, TimeUnit> timeoutPair) {
        Score score = new SimpleScore();
        MinMax minMax = new MinMax();
        CustomGameState gameState = CustomGameState.build(board);

        return minMax.selectionAlgorithm(gameState, timeoutPair, score);
    }
}
