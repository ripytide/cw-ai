package uk.ac.bris.cs.scotlandyard.ui.ai;

import uk.ac.bris.cs.scotlandyard.model.Board;

public interface Score {
    public Integer score(Board.GameState gameState);
}
