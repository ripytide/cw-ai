package uk.ac.bris.cs.scotlandyard.ui.ai;

import uk.ac.bris.cs.scotlandyard.model.Board;

public interface Score {
    public float score(Board.GameState gameState);
}
