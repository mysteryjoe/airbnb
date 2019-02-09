import java.util.*;

/**
 *      Each one has a unique rank from 0 to n - 1 and knows a few other people
 *
 *      you are at rank 0, you want to know rank n wizard
 *      Meet new people requires points : square of difference btw rank of introducer and introducee
 *      find the path for minimal points to know wizard n
 *
 *      The problem is a directed graph problem. wizards are vertices, a knows b means a has an edge to b.
 *      Edge has length which is cost of points.
 *      Our goal is to find the shortest path from 0 to n - 1
 *      I suggest use dijkstra algorithm. Using priorityQueue to implement it
 *
 *      input List<List<Integer>> is a adjacent list graph,
 *      so we don't need build graph on our own
 *
 *       *
 *  *
 *  *       time complexity: E edges, worst case: every edge we add into priorityQueue, and E <= O(V^2)
 *  *                        O(ElogE + V) = O(ElogV + V)
 *  *
 *  *       space: O(V + E)
 *  *
 *
 */
public class Nwizards {
    public List<Integer> getShortestPath(List<List<Integer>> wizards, int n) {
        if (wizards.size() == 0) {
            return null;
        }



        //initialize costs. 0 and its friends are 0, others are INF
        int[] costs = new int[n];
        Arrays.fill(costs, Integer.MAX_VALUE);
        //using priority queue, everytime we choose wizard that has minimal costs from now
        Queue<Integer> pq = new PriorityQueue<>(Comparator.comparing(a -> costs[a]));

        int[] parent = new int[n];
        costs[0] = 0;
        pq.offer(0);
        //record the path info
        parent[0] = 0;
        for (int i = 0; i < wizards.get(0).size(); i++) {
            costs[wizards.get(0).get(i)] = 0;
            pq.offer(wizards.get(0).get(i));
            parent[wizards.get(0).get(i)] = 0;
        }

        Set<Integer> settled = new HashSet<>();
        while (!pq.isEmpty()) {
            int curr = pq.poll();
            if (settled.contains(curr)) {
                continue;
            }
            settled.add(curr);

            if (curr == n - 1) {
                return getPath(parent, n - 1);
            }

            for (int next : wizards.get(curr)) {
                int newCost = costs[curr] + (next - curr) * (next - curr);
                if (newCost < costs[next]) {
                    costs[next] = newCost;
                    pq.offer(next);
                    parent[next] = curr;
                }
            }
        }
        return new ArrayList<>();
    }

    private List<Integer> getPath(int[] prev, int end) {
        List<Integer> path = new ArrayList<>();
        while (prev[end] != end) {
            path.add(end);
            end = prev[end];
        }
        path.add(0);
        Collections.reverse(path);
        return path;
    }

    public static void main(String[] args) {
        Nwizards nwizards = new Nwizards();
        int[][] wizards = new int[][] {
                {5},
                {3},
                {4, 7},
                {4},
                {8},
                {2},
                {8, 9},
                {9},
                {9},
                {1, 2, 3}
        };

        List<List<Integer>> wizardList = new ArrayList<>();
        for (int i = 0; i < wizards.length; i++) {
            wizardList.add(new ArrayList<>());
            for (int j = 0; j < wizards[i].length; j++) {
                wizardList.get(i).add(wizards[i][j]);
            }
        }

        List<Integer> res = nwizards.getShortestPath(wizardList, 10);
        System.out.print("Path is : ");
        for (int x : res) {
            System.out.print(x + " -> ");
        }
        System.out.println("DONE");
    }
}
