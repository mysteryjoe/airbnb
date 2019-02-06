import sun.text.CompactByteArray;

import java.util.*;

/**
 *    给定一个List of list integer 代表了wizard i 的neighbor都是谁
 *    cost的计算方式为 (i-j)^2
 *    请问给定source and dest 如何求出最小cost？
 *
 *    use dijistra algorithm to find the shortest path
 *
 *    implement by using priorityQueue.
 *    using int[] costs to maintain the costs from source to vertex i
 *
 *    1. First add the source into pq
 *    2. while the priorityQueue is not empty, we do following
 *
 *       if current node is our destination, then we achieve the shortest path, return cost
 *
 *       poll the minimal cost node from now, check if it has been visited, evaluate its neighbors' cost,
 *       and put it to visited set
 *
 *       by comparing neighbors' current cost and current node's cost + current to neighbor's edge cost
 *       and if it's smaller, we add to priorityQueue. else just ignore.
 *
 *
 *       time complexity: E edges, worst case: every edge we add into priorityQueue, and E <= O(V^2)
 *                        O(ElogE + V) = O(ElogV + V)
 *
 *       space: O(V + E)
 *
 */
public class TenWizards {

    public static void main(String[] args) {
        TenWizards tenWizards = new TenWizards();
        int[][] wizards = new int[][] {
                {1, 5},
                {2},
                {3, 5},
                {4},
                {5},
                {0},
                {8, 9},
                {9},
                {9},
                {}
        };
        List<List<Integer>> wizardList = new ArrayList<>();
        for (int i = 0; i < wizards.length; i++) {
            wizardList.add(new ArrayList<>());
            for (int j = 0; j < wizards[i].length; j++) {
                wizardList.get(i).add(wizards[i][j]);
            }
        }
        System.out.println("the min cost is " + tenWizards.getShortestPath(wizardList, 0, 5));
    }

    public int getShortestPath(List<List<Integer>> wizards, int source, int dest) {
        if (wizards.size() == 0) {
            return -1;
        }
        //initialization
        int[] costs = new int[wizards.size()];
        int[] paths = new int[wizards.size()];
        for (int i = 0; i < paths.length; i++) {
            paths[i] = i;
            costs[i] = Integer.MAX_VALUE;
        }

        Queue<Integer> pq = new PriorityQueue<>(Comparator.comparing(a -> costs[a]));
        pq.offer(source);
        costs[source] = 0;
        for (int x : wizards.get(0)) {
            costs[x] = 0;
            paths[x] = 0;
            pq.add(x);
        }
        Set<Integer> visited = new HashSet<>();
        while (!pq.isEmpty()) {
            int curr = pq.poll();
            if (visited.contains(curr)) {
                continue;
            }
            visited.add(curr);
            if (curr == dest) {
                //follow up
                printPath(curr, paths);
                return costs[dest];
            }
            for (int neighbor : wizards.get(curr)) {
                int newCost = costs[curr] + (neighbor - curr) * (neighbor - curr);
                if (newCost < costs[neighbor]) {
                    //update cost value first then add to priorityQueue
                    costs[neighbor] = newCost;
                    paths[neighbor] = curr;
                    pq.offer(neighbor);
                }
            }
        }
        return -1;
    }

    private void printPath(int dest, int[] paths) {
        String path = "";
        while (paths[dest] != dest) {
            path = "->" + dest + path;
            dest = paths[dest];
        }
        path = dest + path;
        System.out.println(path);
    }

}
