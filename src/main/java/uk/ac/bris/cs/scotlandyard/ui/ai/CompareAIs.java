package uk.ac.bris.cs.scotlandyard.ui.ai;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.graph.ImmutableValueGraph;
import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.ValueGraph;
import com.google.common.graph.ValueGraphBuilder;
import com.google.common.io.Resources;
import javafx.util.Pair;
import uk.ac.bris.cs.scotlandyard.model.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Random;
import java.util.stream.IntStream;

import static uk.ac.bris.cs.scotlandyard.model.ScotlandYard.REVEAL_MOVES;
import static uk.ac.bris.cs.scotlandyard.model.ScotlandYard.readGraph;

public class CompareAIs {
    ArrayList availableLocations;

    CompareAIs() {
        availableLocations = new ArrayList();
        for (int i = 0; i < 201; i++) {
            availableLocations.add(i);
        }
    }

    private Integer getRandomLocation() {
        Random rand = new Random();
        Integer index = rand.nextInt(0, availableLocations.size());
        Integer value = availableLocations.get(index);
        return availableLocations.remove(index);
    }

    public Pair<Boolean, Integer> compareTwoAis(Ai firstAI, Ai secondAi) throws IOException {
        Random rand = new Random();

        CustomGameStateBuilder customGameStateBuilder = new CustomGameStateBuilder();

        ImmutableValueGraph<Integer, ImmutableSet<ScotlandYard.Transport>> standardGraph = readGraph(Resources.toString(
                Resources.getResource("graph.txt"), StandardCharsets.UTF_8));

        GameSetup gameSetup = new GameSetup(standardGraph, IntStream.rangeClosed(1, 24)
                .mapToObj(REVEAL_MOVES::contains)
                .collect(ImmutableList.toImmutableList()));


        //10 different starting positions
        for (int i = 0; i < 10; i++) {
            Integer mrXLocation = rand.nextInt(1, 201);
            Player mrX = new Player(Piece.MrX.MRX, getInitialTickets(), mrXLocation);
            ImmutableList<Player> detectives;
            customGameStateBuilder.build(gameSetup, getRandomLocationMrX(), );
        }

        private Player getRandomLocationMrX () {
            int mrXStartingLocation = rand.nextInt(1, 201);

            Player mrX = new Player(Piece.MrX.MRX, getInitialTickets(), mrXStartingLocation);
        }

        private ImmutableList<Player> getRandomLocationDetectives (Integer numberOfDetectives){
            ArrayList<Piece.Detective> colours = new ArrayList<>();
            colours.add(Piece.Detective.BLUE);
            colours.add(Piece.Detective.WHITE);
            colours.add(Piece.Detective.RED);
            colours.add(Piece.Detective.GREEN);
            colours.add(Piece.Detective.YELLOW);

            for (int i = 0; i < numberOfDetectives; i++) {
                int startingLocation = getRandomLocation();
                Player detective = new Player(colours.get(i), getInitialTickets(), getRandomLocation());
            }
        }

        private ImmutableMap<ScotlandYard.Ticket, Integer> getInitialTickets () {
            return ImmutableMap.of(ScotlandYard.Ticket.TAXI, 4,
                    ScotlandYard.Ticket.BUS, 3,
                    ScotlandYard.Ticket.UNDERGROUND, 3,
                    ScotlandYard.Ticket.DOUBLE, 2,
                    ScotlandYard.Ticket.SECRET, 5);
        }
    }