package uk.ac.bris.cs.scotlandyard.ui.ai;

import io.atlassian.fugue.Pair;
import uk.ac.bris.cs.scotlandyard.model.Ai;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Delegates to the actual UI main
 */
public class Main {
	public static void main(String[] args) throws IOException {
		//uk.ac.bris.cs.scotlandyard.Main.main(args);
		CompareAIs comparison = new CompareAIs();

		ArrayList<Ai> mrXAis = new ArrayList<>();
		mrXAis.add(new RandomAi());
		mrXAis.add(new MrXAISimpleScoreMinimax());
		mrXAis.add(new MrXAIDistanceToNearestDetectiveMinimax());
		mrXAis.add(new MrXAITotalDistanceToMrXMinimax());

		ArrayList<Ai> detectiveAis = new ArrayList<>();
		detectiveAis.add(new RandomAi());
		detectiveAis.add(new DetectiveAISimpleScoreMinimax());
		detectiveAis.add(new DetectiveAIDistanceToNearestDetectiveMinimax());
		detectiveAis.add(new DetectiveAITotalDistanceToMrXMinimax());

		for(Ai mrXAi : mrXAis){
			for(Ai detectiveAi : detectiveAis){
				System.out.println("mrX Ai: " + mrXAi.name());
				System.out.println("detective Ai: " + detectiveAi.name());

				ArrayList<Pair<Integer, WhoWon>> result = comparison.compareTwoAis(mrXAi, detectiveAi, 1, 1, 1, 1L);
				System.out.println("Results: " + result);

				System.out.println("Average: " + result.stream().map(p -> p.left()).reduce(0, Integer::sum));

				System.out.println("MrX Won: " + result.stream().map(p -> p.right()).filter(w -> w==WhoWon.MrX).count() + " times");
				System.out.println("Detectives Won: " + result.stream().map(p -> p.right()).filter(w -> w==WhoWon.Detectives).count() + " times");
				System.out.println();
			}
		}
	}
}