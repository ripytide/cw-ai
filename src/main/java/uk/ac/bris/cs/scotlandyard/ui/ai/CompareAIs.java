package uk.ac.bris.cs.scotlandyard.ui.ai;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.graph.ImmutableValueGraph;
import com.google.common.io.Resources;
import io.atlassian.fugue.Pair;
import uk.ac.bris.cs.scotlandyard.model.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static uk.ac.bris.cs.scotlandyard.model.ScotlandYard.REVEAL_MOVES;
import static uk.ac.bris.cs.scotlandyard.model.ScotlandYard.readGraph;

enum WhoWon {
    Detectives,
    MrX,
}

public class CompareAIs {
    ArrayList<Integer> availableLocations;

    CompareAIs() {
        resetAvailableLocations();
    }

    private void resetAvailableLocations() {
        availableLocations = new ArrayList<>();
        for (int i = 1; i <= 199; i++) {
            availableLocations.add(i);
        }
    }

    private Integer getRandomLocation() {
        Random rand = new Random();
        Integer index = rand.nextInt(0, availableLocations.size());
        Integer value = availableLocations.get(index);
        availableLocations.remove(value);

        return value;
    }

    //returns the move count and who won for every game played between two AIs
    public ArrayList<Pair<Integer, WhoWon>> compareTwoAis(Ai mrXAi,
                                                          Ai detectivesAi,
                                                          Integer numOfStartingPositions,
                                                          Integer numOfRepeatGames,
                                                          Integer numOfDetectives,
                                                          Long aiTimeLimit) throws IOException {
        Pair<Long, TimeUnit> time = new Pair<>(aiTimeLimit, TimeUnit.SECONDS);

        CustomGameStateBuilder customGameStateBuilder = new CustomGameStateBuilder();

        //reads graph (with nodes from 1-199) from Resources - Taken from GameSetup (Closed Task)
        ImmutableValueGraph<Integer, ImmutableSet<ScotlandYard.Transport>> standardGraph = readGraph(Resources
                .toString(Resources
                        .getResource("graph.txt"),
                                     StandardCharsets.UTF_8));

        //makes a GameStu using standard hidden moves
        GameSetup gameSetup = new GameSetup(standardGraph, IntStream.rangeClosed(1, 24)
                .mapToObj(REVEAL_MOVES::contains)
                .collect(ImmutableList.toImmutableList()));

        ArrayList<Pair<Integer, WhoWon>> gamesResults = new ArrayList<>();

        //controls for luck of starting positions
        for (int i = 0; i < numOfStartingPositions; i++) {
            Player mrX = new Player(Piece.MrX.MRX, getMrXInitialTickets(numOfDetectives), getRandomLocation());
            ImmutableList<Player> detectives = getRandomLocationDetectives(numOfDetectives);
            CustomGameState gameState = customGameStateBuilder.build(gameSetup, mrX, detectives);

            //containerizing possibly error-prone code
            try {
                //repeat games increases repeatability of results
                for (int j = 0; j < numOfRepeatGames; j++) {
                    Integer moveCount = 0;
                    while (gameState.getWinner().isEmpty()) {

                        Move pickedMove;
                        if (gameState.isMrXTurn()) {
                            moveCount++;
                            pickedMove = mrXAi.pickMove(gameState, time);
                        } else {
                            pickedMove = detectivesAi.pickMove(gameState, time);
                        }
                        gameState = gameState.advance(pickedMove);
                    }

                    //keeping track of both who won and the move count
                    WhoWon whoWon;
                    if (gameState.getWinner().contains(Piece.MrX.MRX)) {
                        whoWon = WhoWon.MrX;
                    } else {
                        whoWon = WhoWon.Detectives;
                    }
                    gamesResults.add(new Pair<>(moveCount, whoWon));

                    resetAvailableLocations();
                }
            } catch (Exception e) {
                System.out.print("game failed");
                throw e;
            }
        }
        return gamesResults;
    }

    //returns detectives in random and available locations
    private ImmutableList<Player> getRandomLocationDetectives(Integer numberOfDetectives) {
        ArrayList<Player> mutableDetectives = new ArrayList<>();

        ArrayList<Piece.Detective> colours = new ArrayList<>();
        colours.add(Piece.Detective.BLUE);
        colours.add(Piece.Detective.WHITE);
        colours.add(Piece.Detective.RED);
        colours.add(Piece.Detective.GREEN);
        colours.add(Piece.Detective.YELLOW);

        for (int i = 0; i < numberOfDetectives; i++) {
            Player detective = new Player(colours.get(i), getDetectivesInitialTickets(), getRandomLocation());
            mutableDetectives.add(detective);
        }

        return ImmutableList.copyOf(mutableDetectives);
    }

    //standard tickets for MrX
    private ImmutableMap<ScotlandYard.Ticket, Integer> getMrXInitialTickets(int numOfDetectives) {
        return ImmutableMap.of(ScotlandYard.Ticket.TAXI, 4,
                ScotlandYard.Ticket.BUS, 3,
                ScotlandYard.Ticket.UNDERGROUND, 3,
                ScotlandYard.Ticket.DOUBLE, 2,
                ScotlandYard.Ticket.SECRET, numOfDetectives);
    }

    //standard tickets for detectives
    private ImmutableMap<ScotlandYard.Ticket, Integer> getDetectivesInitialTickets() {
        return ImmutableMap.of(ScotlandYard.Ticket.TAXI, 10,
                ScotlandYard.Ticket.BUS, 8,
                ScotlandYard.Ticket.UNDERGROUND, 4,
                ScotlandYard.Ticket.DOUBLE, 0,
                ScotlandYard.Ticket.SECRET, 0);
    }
}