package uk.ac.bris.cs.scotlandyard.ui.ai;

import io.atlassian.fugue.Pair;
import uk.ac.bris.cs.scotlandyard.model.Board;
import uk.ac.bris.cs.scotlandyard.model.Move;

import javax.annotation.Nonnull;
import java.util.concurrent.TimeUnit;

public interface MovePicker {
    public Move selectionAlgorithm(@Nonnull CustomGameState gameState, Pair<Long, TimeUnit> timeoutPair, Score scoringMethod);
}