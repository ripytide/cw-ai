package uk.ac.bris.cs.scotlandyard.ui.ai;

import io.atlassian.fugue.Pair;
import uk.ac.bris.cs.scotlandyard.model.Board;
import uk.ac.bris.cs.scotlandyard.model.Move;

import java.util.concurrent.TimeUnit;

public interface MovePicker {
    Move selectionAlgorithm(CustomGameState gameState, Board board, Pair<Long, TimeUnit> timeoutPair);
}