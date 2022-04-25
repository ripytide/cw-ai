package uk.ac.bris.cs.scotlandyard.ui.ai;

import io.atlassian.fugue.Pair;
import uk.ac.bris.cs.scotlandyard.model.Ai;
import uk.ac.bris.cs.scotlandyard.model.Board;
import uk.ac.bris.cs.scotlandyard.model.Move;

import javax.annotation.Nonnull;
import java.util.concurrent.TimeUnit;

public class MrXAITotalDistanceToMrXMinimax implements Ai {
    @Nonnull
    @Override
    public String name() {
        return "MrXAITotalDistanceToMrXMinimax";
    }

    @Nonnull
    @Override
    public Move pickMove(@Nonnull Board board, Pair<Long, TimeUnit> timeoutPair) {
        Score score = new TotalDistanceToMrX();
        AccuracyBasedMetaScore minMax = new MinMax(score);
        MovePicker timeBasedMovePicker = new TimeBasedMovePicker(minMax);

        CustomGameState gameState = CustomGameState.build(board, false);

        return timeBasedMovePicker.selectionAlgorithm(gameState, board, timeoutPair);
    }
}
