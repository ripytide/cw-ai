package uk.ac.bris.cs.scotlandyard.ui.ai;

import com.google.common.collect.ImmutableSet;
import uk.ac.bris.cs.scotlandyard.model.Piece;

public class SimpleScore implements Score{
    @Override
    public Float score(CustomGameState gameState) {
        ImmutableSet<Piece> winners = gameState.getWinner();
        if(winners.isEmpty()){
            return 0.5f;
        }else if(winners.stream().noneMatch(Piece::isMrX)){
            return 0f;
        }else{
            return 1f;
        }
    }
}
