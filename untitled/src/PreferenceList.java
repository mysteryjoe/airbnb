import java.util.*;

/**
 *   每个人都有个preference order
 *
 *   find one order that meets everyone's preference
 *
 *   I suggest using topological sort to solve this problem.
 *
 *   for example
 *   [2, 3, 5], [4, 2, 1], [4, 1, 5, 6, 8], [4, 7]
 *
 *   first, get topological order:
 *   7
 *   |
 *   4 -> 2 - > 3 -> 5 -> 6 -> 8
 *   \   /          /
 *     1 -----------
 *
 *   4, 7, 2, 1, 3, 5, 6, 8
 *
 *
 *   time complexity : initialize O(nl) (n people with list length l)
 *                     since every edge will be visited once, and E <= nl
 *                     O(nl + E) = O(nl)
 *
 *
 *   follow up : what if person list is weighted?
 *   Maintain a map that keep highest weight for each number, then when traverse by topological order, we use
 *   priority queue to have them sorted by their weight
 *
 *   (also could use list<Node> and after init the neighbors, we sort every List by Node weight.
 */
public class PreferenceList {
    public static void main(String[] args) {
        int[][] testArr = new int[][] {
                {2, 3, 5},
                {4, 1, 2},
                {4, 1, 5, 6, 8},
                {4, 7}
        };

//        //follow up test
//        int[][] testArr = new int[][] {
//                {0, 1, 2, 3},
//                {4},
//                {5},
//                {6, 7}
//        };

        List<List<Integer>> test = new ArrayList<>();
        //print preference list
        for (int i = 0; i < testArr.length; i++) {
            test.add(new ArrayList<>());
            System.out.println("person " + i+"'s prefer order: ");
            for (int j = 0; j < testArr[i].length; j++) {
                System.out.print(testArr[i][j] + "  ");
                test.get(i).add(testArr[i][j]);
            }
            System.out.println();
        }

        PreferenceList sol = new PreferenceList();
        List<Integer> res = sol.getPreference(test);

        //follow up
//        List<Integer> weights = Arrays.asList(0, 1, 3, 2);
//        List<Integer> res = sol.getPreference(test, weights);
        System.out.println();
        System.out.println("The preferences list is : ");
        for (int i : res) {
            System.out.print(i + "  ");
        }
        System.out.println();
    }

    public List<Integer> getPreference(List<List<Integer>> preferences) {
        Map<Integer, Integer> indegrees = new HashMap<>();
        Map<Integer, Set<Integer>> graph = new HashMap<>();

        //get topological sort by initialize indegrees and neighbors
        for (int i = 0; i < preferences.size(); i++) {
            List<Integer> preference = preferences.get(i);
            //if that person has no preferences
            if (preference.isEmpty()) {
                continue;
            }

            //we start from first node
            int from = preference.get(0);
            indegrees.putIfAbsent(from, 0);
            graph.putIfAbsent(from, new HashSet<>());

            for (int j = 1; j < preference.size(); j++) {
                int to = preference.get(j);
                if (!graph.containsKey(to)) {
                    indegrees.put(to, 0);
                    graph.put(to, new HashSet<>());
                }

                //avoid duplicate indegree count
                if (!graph.get(from).contains(to)) {
                    indegrees.put(to, indegrees.get(to) + 1);
                    graph.get(from).add(to);
                }

                from = to;
            }
        }

        //then use bfs to traverse the graph
        List<Integer> res = new ArrayList<>();
        Queue<Integer> queue = new LinkedList<>();
        for (int key : indegrees.keySet()) {
            if (indegrees.get(key) == 0) {
                queue.offer(key);
            }
        }
        while (!queue.isEmpty()) {
            int curr = queue.poll();
            res.add(curr);
            for (int neighbor : graph.get(curr)) {
                indegrees.put(neighbor, indegrees.get(neighbor) - 1);
                if (indegrees.get(neighbor) == 0) {
                    queue.offer(neighbor);
                }
            }

        }
        return res;
    }



    //follow up

    public List<Integer> getPreference(List<List<Integer>> preferences, List<Integer> weights) {
        Map<Integer, Integer> indegrees = new HashMap<>();
        Map<Integer, Set<Integer>> neighbors = new HashMap<>();
        Map<Integer, Integer> highestWeight = new HashMap<>();

        //get topological sort by initialize indegrees and neighbors
        for (int i = 0; i < preferences.size(); i++) {
            List<Integer> preference = preferences.get(i);
            int weight = weights.get(i);
            //if that person has no preferences
            if (preference.isEmpty()) {
                continue;
            }

            //every time we add start node first
            int from = preference.get(0);
            indegrees.putIfAbsent(from, 0);
            neighbors.putIfAbsent(from, new HashSet<>());
            highestWeight.putIfAbsent(from, weight);
            highestWeight.put(from, Math.max(weight, highestWeight.get(from)));

            for (int j = 1; j < preference.size(); j++) {
                int to = preference.get(j);

                if (!neighbors.containsKey(to)) {
                    indegrees.put(to, 0);
                    neighbors.put(to, new HashSet<>());
                    highestWeight.put(to, weight);
                }

                //update weight
                highestWeight.put(to, Math.max(weight, highestWeight.get(to)));

                //avoid duplicate indegree count
                if (!neighbors.get(from).contains(to)) {
                    indegrees.put(to, indegrees.get(to) + 1);
                    neighbors.get(from).add(to);
                }
                from = to;
            }
        }

        //then use bfs to traverse the graph
        List<Integer> res = new ArrayList<>();
        Queue<Integer> queue = new PriorityQueue<>(Comparator.comparing(a -> -highestWeight.get(a)));
        for (int key : indegrees.keySet()) {
            if (indegrees.get(key) == 0) {
                queue.offer(key);
            }
        }
        while (!queue.isEmpty()) {
            int curr = queue.poll();
            res.add(curr);
            for (int neighbor : neighbors.get(curr)) {
                indegrees.put(neighbor, indegrees.get(neighbor) - 1);
                if (indegrees.get(neighbor) == 0) {
                    queue.offer(neighbor);
                }
            }

        }
        return res;
    }

}
