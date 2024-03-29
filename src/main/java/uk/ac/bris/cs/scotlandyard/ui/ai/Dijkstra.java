package uk.ac.bris.cs.scotlandyard.ui.ai;

import com.google.common.graph.ImmutableValueGraph;

import java.util.*;

public class Dijkstra {
    public static Float dijkstra(ImmutableValueGraph<Integer, Float> graph, Integer source, Integer destination) {
        if(source.equals(destination)) return 0f;

        HashMap<Integer, Float> visitedNodes = new HashMap<>();
        HashMap<Integer, Float> unvisitedNodes = new HashMap<>();

        visitedNodes.put(source, 0f);

        boolean found = false;
        Integer currentNode = source;
        Float currentNodeDist = 0f;
        while (!found) {
            //updating adjacent unvisited nodes with min distance
            for (Integer adjacentNode : graph.adjacentNodes(currentNode)) {
                if (!visitedNodes.containsKey(adjacentNode)) {
                    Float newDistance = currentNodeDist + graph.edgeValue(currentNode, adjacentNode).get();
                    if (unvisitedNodes.containsKey(adjacentNode)) {
                        Float adjNodeDist = unvisitedNodes.get(adjacentNode);
                        if (newDistance < adjNodeDist) {
                            unvisitedNodes.put(adjacentNode, newDistance);
                        }
                    } else {
                        unvisitedNodes.put(adjacentNode, newDistance);
                    }
                }
            }

            //select closest new currentNode
            Integer closestNode = null;
            Float minDistance = Float.POSITIVE_INFINITY;
            for (Integer node : unvisitedNodes.keySet()) {
                Float nodeDistance = unvisitedNodes.get(node);
                if (nodeDistance < minDistance) {
                    closestNode = node;
                    minDistance = nodeDistance;
                }
            }

            //visit node
            visitedNodes.put(closestNode, minDistance);

            //checking if we've finished
            if (currentNode.equals(destination)) {
                found = true;
            } else if (unvisitedNodes.isEmpty()) {
                throw new IllegalArgumentException("destination unreachable in Dijkstra's.");
            }unvisitedNodes.remove(closestNode);

            currentNode = closestNode;
            currentNodeDist = minDistance;
        }

        return currentNodeDist;
    }
}