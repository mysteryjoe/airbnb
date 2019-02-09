import java.util.*;

/**
 *      Given check points and edges, we find maximum score we could earn from highest point
 *      to lowest point
 *
 *      checkPoints : "A,5" "B,7" denotes the rewards for each checkpoints
 *
 *      Edge: "A,B,3" means from A to B costs 3
 *
 *      score we could earn = 2 * currentPoints + distance cost
 *
 *      I suggest using topological sort and breadth first search to solve this problem.
 *
 *      Start Point : points that is sources
 *      End point : sinks
 *
 *      maintain each point a variable called maxScore, to keep track max score possible if through the point
 *
 *      topological sort assured every check point could get their maxScore before going down, thus visiting edge
 *      only once.
 *
 *      bfs will visit edges more than once. which is inefficient
 *
 *      Time complexity: n checkpoints, E edges
 *                      O(n + E)
 *
 *      Space: O(n + E)
 */

public class Ski {

    public static void main(String[] args) {
        Ski sol = new Ski();
        String[] checkPoints = new String[] {"A,5", "B,7", "C,6", "D,2", "E,4", "F,7", "H,7", "I,3", "J,2"};
        String[] edges = {
                "A,B,2",
                "A,C,3",
                "B,D,5",
                "B,E,6",
                "C,E,4",
                "C,F,4",
                "D,H,7",
                "E,H,6",
                "H,I,1",
                "H,J,2",
                "F,J,3"
//                "N,C,10"
        };
        sol.maxPath(checkPoints, edges);
    }

    public int maxPath(String[] checkPoints, String[] edges) {
        Map<String, Node> graph = new HashMap<>();
        Map<String, Integer> indegrees = new HashMap<>();

        //follow up
        Map<String, String> prev = new HashMap<>();


        //create each point
        for (String p : checkPoints) {
            String[] t = p.split(",");
            graph.put(t[0], new Node(t[0], Integer.parseInt(t[1])));
            indegrees.put(t[0], 0);
        }

        //create edge and calc indegree
        for (String s : edges) {
            String[] t = s.split(",");
            Node from = graph.get(t[0]);

            from.neighbors.put(t[1], Integer.parseInt(t[2]));
            indegrees.put(t[1], indegrees.get(t[1]) + 1);
        }

        //topological order
        String endPoint = "";
        int maxScore = Integer.MIN_VALUE;
        Queue<Node> q = new LinkedList<>();
        //start point, set score to their points value
        for (String key : indegrees.keySet()) {
            if (indegrees.get(key) == 0) {
                q.offer(graph.get(key));
                graph.get(key).maxScore = 2 * graph.get(key).point;
                prev.put(key, key);
            }
        }

        while (!q.isEmpty()) {
            Node curr = q.poll();
            //meet sink, which means end point
            if (curr.neighbors.isEmpty() && curr.maxScore > maxScore) {
                endPoint = curr.id;
                maxScore = curr.maxScore;
            }

            for (String s : curr.neighbors.keySet()) {
                Node neighbor = graph.get(s);
                int score = 2 * neighbor.point + curr.neighbors.get(neighbor.id);
                if (curr.maxScore + score > neighbor.maxScore) {
                    neighbor.maxScore = curr.maxScore + score;
                    prev.put(neighbor.id, curr.id);
                }

                indegrees.put(s, indegrees.get(s) - 1);
                if (indegrees.get(s) == 0) {
                    q.offer(neighbor);
                }
            }
        }
        //build path
        String path = "";
        while (!prev.get(endPoint).equals(endPoint)) {
            path = "->" + endPoint + path;
            endPoint = prev.get(endPoint);
        }
        path = endPoint + path;
        System.out.println("max score is " + maxScore);
        System.out.println("path is : ");
        System.out.println(path);

        return maxScore;
    }

    private class Node {
        String id;
        Map<String, Integer> neighbors;
        int point;
        int maxScore;
        public Node(String id, int point) {
            this.id = id;
            this.point = point;
            neighbors = new HashMap<>();
        }
    }
}
