package uk.ac.bris.cs.scotlandyard.ui.ai;

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
		Ai mrXAi = new MrXAIDistanceToNearestDetectiveMinimax();
		Ai detectivesAi = new RandomAi();

		ArrayList<Integer> result = comparison.compareTwoAis(mrXAi, detectivesAi, 1, 1, 5L);
		System.out.println("Results: " + result);
		System.out.println("Average: " + result.stream().reduce(0, Integer::sum));
	}
}
