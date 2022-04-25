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
import java.time.Instant;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static uk.ac.bris.cs.scotlandyard.model.ScotlandYard.REVEAL_MOVES;
import static uk.ac.bris.cs.scotlandyard.model.ScotlandYard.readGraph;

public class CompareAIs {
    ArrayList<Integer> availableLocations;

    CompareAIs() {
        resetAvailableLocations();
    }

    private void resetAvailableLocations(){
        availableLocations = new ArrayList();
        for (int i = 1; i <= 200; i++) {
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

    public ArrayList<Integer> compareTwoAis(Ai mrXAi, Ai detectivesAi, Integer numOfStartingPositions, Integer numOfRepeatGames, Integer numOfDetectives, Long aiTimeLimit) throws IOException {
        Random rand = new Random();
        Pair<Long, TimeUnit> time = new Pair<>(aiTimeLimit, TimeUnit.SECONDS);

        CustomGameStateBuilder customGameStateBuilder = new CustomGameStateBuilder();

        ImmutableValueGraph<Integer, ImmutableSet<ScotlandYard.Transport>> standardGraph = readGraph(Resources.toString(
                Resources.getResource("graph.txt"), StandardCharsets.UTF_8));

        GameSetup gameSetup = new GameSetup(standardGraph, IntStream.rangeClosed(1, 24)
                .mapToObj(REVEAL_MOVES::contains)
                .collect(ImmutableList.toImmutableList()));


        ArrayList<Integer> moveCounts = new ArrayList<>();

        //10 different starting positions
        for (int i = 0; i < numOfStartingPositions; i++) {
            Player mrX = new Player(Piece.MrX.MRX, getMrXInitialTickets(), getRandomLocation());
            ImmutableList<Player> detectives = getRandomLocationDetectives(numOfDetectives);
            CustomGameState gameState = customGameStateBuilder.build(gameSetup, mrX, detectives);

            //to get average
            try {
                for (int j = 0; j < numOfRepeatGames; j++) {
                    Integer moveCount = 0;
                    while (gameState.getWinner().isEmpty()) {

                        Move pickedMove;
                        if (gameState.isMrXTurn()) {
                            //System.out.println("MrX moved" + Instant.now());
                            moveCount++;
                            pickedMove = mrXAi.pickMove(gameState, time);
                        } else {
                            //System.out.println("A Detective moved" + Instant.now());
                            pickedMove = detectivesAi.pickMove(gameState, time);
                        }
                        gameState = gameState.advance(pickedMove);
                    }

                    moveCounts.add(moveCount);
                    resetAvailableLocations();
                }
            }catch(Exception e){
                System.out.print("game failed");
            }
        }

        return moveCounts;
    }

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

    private ImmutableMap<ScotlandYard.Ticket, Integer> getMrXInitialTickets() {
        return ImmutableMap.of(ScotlandYard.Ticket.TAXI, 4,
                ScotlandYard.Ticket.BUS, 3,
                ScotlandYard.Ticket.UNDERGROUND, 3,
                ScotlandYard.Ticket.DOUBLE, 2,
                ScotlandYard.Ticket.SECRET, 5);
    }

    private ImmutableMap<ScotlandYard.Ticket, Integer> getDetectivesInitialTickets() {
             return ImmutableMap.of(ScotlandYard.Ticket.TAXI, 8,
                     ScotlandYard.Ticket.BUS, 8,
                     ScotlandYard.Ticket.UNDERGROUND, 8,
                     ScotlandYard.Ticket.DOUBLE, 0,
                     ScotlandYard.Ticket.SECRET, 0);
    }
}