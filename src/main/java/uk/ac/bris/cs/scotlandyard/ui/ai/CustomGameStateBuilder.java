package uk.ac.bris.cs.scotlandyard.ui.ai;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import uk.ac.bris.cs.scotlandyard.model.*;

import javax.annotation.Nonnull;
import java.util.ArrayList;

public class CustomGameStateBuilder {
    @Nonnull
    public CustomGameState build(GameSetup setup, Player mrX, ImmutableList<Player> detectives) {
        //checking validity of parameters given
        if (mrX == null) throw new NullPointerException("MrX is Null");
        if (!mrX.isMrX()) throw new IllegalArgumentException("No mrX");
        if (detectives.stream().filter(Player::isMrX).toList().size() > 0) {
            throw new IllegalArgumentException("Multiple MrXs");
        }
        checkDetectivesValidity(detectives);

        if (setup.moves.isEmpty()) throw new IllegalArgumentException("Moves is empty");
        if (setup.graph.nodes().size() == 0) throw new IllegalArgumentException("Graph is empty");

        return new CustomGameState(setup, ImmutableSet.of(Piece.MrX.MRX), ImmutableList.of(), mrX, detectives);
    }

    private void checkDetectivesValidity(ImmutableList<Player> detectives) {
        ArrayList<Piece> usedPieces = new ArrayList<>();
        ArrayList<Integer> usedLocations = new ArrayList<>();
        for (Player p : detectives) {
            if (usedPieces.contains(p.piece())) {
                throw new IllegalArgumentException("Duplicate detectives");
            } else {
                usedPieces.add(p.piece());
            }

            if (usedLocations.contains(p.location())) {
                throw new IllegalArgumentException("Duplicate locations");
            } else {
                usedLocations.add(p.location());
            }

            if (p.tickets().get(ScotlandYard.Ticket.SECRET) > 0) {
                throw new IllegalArgumentException("detectives should not have secret tickets");
            }
            if (p.tickets().get(ScotlandYard.Ticket.DOUBLE) > 0) {
                throw new IllegalArgumentException("detectives should not have double tickets");
            }
        }
    }
}
