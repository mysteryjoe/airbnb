import java.util.*;

/**
 *    find cheapest price from src to dst with up to K stops
 *
 *    output -1 if no such route;
 *
 *    I suggest using dijkstra algorithm since our problem is to find shortest path in
 *    a directed graph,
 *    However in this problem, the restriction of k stops is important.
 *    It means we have two different weights on each edge, the costs and the # of stops
 *
 *    So we need to modify the dijkstra algorithm
 *
 *    I will use priorityQueue and for each city, it has class State that stores current costs, and current stops
 *    We start from start city, and every time we consider the nodes with
 *    minimal costs and stops <= k to be next one to evaluate.
 *
 *    we add node if its costs is smaller than mincosts or its # of stops is smaller than minstops
 *
 *    What's the different between this one and bfs?? or dfs??
 *
 *    For bfs, if we have a circle, for example  S -> A : 1,  A - > B : 1, B -> S :1, B -> C : 20. K = 1000
 *      we might go over the loop many times.
 *      But for our algorithm, we store the miniaml cost and stop pair, B: cost: 2, stop 1. And we won't go over
 *      the loop. since the cost and stops of node will be greater than previous.
 *
 *
 *    O(ElogE = ElogV) time complexity.  We could have maximum |E| times creating states and adding to pq
 *    and  V < E <= V^2 so we have ElogE = ElogV^2 = 2ElogV = O(ElogV).
 */

// TEST CASE  [["S", "A", 6], ["A", "B", 20], ["S", "C", 5], ["C", "D", 10], ["D", "B", 3], ["B", "E", 100], ["E", "F", 20]], K = 3
    // S - > A -> B -> E -> F
    // S -> C- > D -> B -> E -> F

public class CheapFlight {
    public static void main(String[] args) {
        Dijkstra dijkstra = new Dijkstra();
        BellmanFord bellmanFord = new BellmanFord();
        List<List<String>> flights = new ArrayList<>();
        flights.add(Arrays.asList("S", "A", "8"));
        flights.add(Arrays.asList("A", "B", "20"));
        flights.add(Arrays.asList("S", "C", "5"));
        flights.add(Arrays.asList("C", "D", "10"));
        flights.add(Arrays.asList("D", "B", "3"));
        flights.add(Arrays.asList("B", "E", "100"));
        flights.add(Arrays.asList("E", "F", "20"));
        int price1 = dijkstra.findCheapestPrice(flights, "S", "F", 3);
        System.out.println("Dij : Total price is " + price1);
        int price2 = bellmanFord.findCheapestPrice(flights, "S", "F", 3);
        System.out.println("Bell : Total price is " + price2);



        //test case for circle
        List<List<String>> test = new ArrayList<>();
        test.add(Arrays.asList("A", "B", "1"));
        test.add(Arrays.asList("B", "C", "1"));
        test.add(Arrays.asList("C", "A", "1"));
        test.add(Arrays.asList("D", "F", "20"));

        int price3 = dijkstra.findCheapestPrice(test, "A", "F", 100000);
        System.out.println("Total price is " + price3);
        int price4 = bellmanFord.findCheapestPrice(test, "A", "F", 100000);
        System.out.println("Bell : Total price is " + price4);
    }
}

class BellmanFord {
    Map<Integer, Map<String, String>> prev;

    public int findCheapestPrice(List<List<String>> flights, String src, String dst, int k) {
        //follow up
        prev = new HashMap<>();

        //dist from src to each vertex
        Map<String, Integer> costs = new HashMap<>();
        for (List<String> flight : flights) {
            costs.putIfAbsent(flight.get(0), Integer.MAX_VALUE);
            costs.putIfAbsent(flight.get(1), Integer.MAX_VALUE);
        }
        if (!costs.containsKey(src) || !costs.containsKey(dst)) {
            return -1;
        }

        costs.put(src, 0);

        //do k + 1 iterations
        for (int i = 0; i < k + 1; i++) {
            //follow up
            prev.put(i, new HashMap<>());

            Map<String, Integer> curr = new HashMap<>();
            for (String key : costs.keySet()) {
                curr.put(key, costs.get(key));
            }

            for (List<String> flight : flights) {
                String from = flight.get(0);
                String to = flight.get(1);
                int cost = Integer.parseInt(flight.get(2));
                if (costs.get(from) == Integer.MAX_VALUE) {
                    continue;
                }
                if (costs.get(from) + cost < curr.get(to)) {
                    curr.put(to, costs.get(from) + cost);

                    //follow up
                    prev.get(i).put(to, from);
                }
            }
            costs = curr;
        }

        if (costs.get(dst) != Integer.MAX_VALUE) {
            //follow up
            printPath(src, dst, k);

            return costs.get(dst);
        }
        return -1;
    }

    public void printPath(String src, String dst, int k) {
        List<String> path = new ArrayList<>();
        while (k >= 0) {
            if (prev.get(k).containsKey(dst)) {
                path.add(dst);
                dst = prev.get(k).get(dst);
            }
            k--;
        }
        path.add(src);
        Collections.reverse(path);
        System.out.print("Path is : ");
        for (String s : path) {
            System.out.print(s + " -> ");
        }
        System.out.println("done");

    }
}





class Dijkstra {
    class State {
        String city;
        int cost;
        int stop;

        //follow up
        State prev;

        public State(String city, int cost, int stop) {
            this.city = city;
            this.cost = cost;
            this.stop = stop;
        }
    }

    private void buildGraph(Map<String, Map<String, Integer>> graph, List<List<String>> flights) {
        for (List<String> flight : flights) {
            String from = flight.get(0);
            String to = flight.get(1);
            int cost = Integer.parseInt(flight.get(2));

            graph.putIfAbsent(from, new HashMap<>());
            graph.putIfAbsent(to, new HashMap<>());
            graph.get(from).put(to, cost);
        }
    }

    public int findCheapestPrice(List<List<String>> flights, String src, String dst, int k) {

        //create graph based on flights information
        Map<String, Map<String, Integer>> graph = new HashMap<>();
        buildGraph(graph, flights);
        if (!graph.containsKey(src) || !graph.containsKey(dst)) {
            return -1;
        }
        //initialize costs and stops
        //to keep track of min pair of each city
        Map<String, State> minStates = new HashMap<>();
        for (String city : graph.keySet()) {
            minStates.put(city, new State(city, Integer.MAX_VALUE, Integer.MAX_VALUE));
        }
        minStates.put(src, new State(src, 0, 0));

        Queue<State> pq = new PriorityQueue<>((a, b) -> {
            if (a.cost == b.cost) {
                return a.stop - b.stop;
            }
            return a.cost - b.cost;
        });

        pq.offer(new State(src, 0, 0));
        while (!pq.isEmpty()) {
            State curr = pq.poll();
            //first check if it is destination
            if (curr.city.equals(dst)) {
                //follow up
                printPath(curr);

                return curr.cost;
            }
            //important! if current stops greater than k,
            //which means we don't need to consider its neighbor anymore
            if (curr.stop > k) {
                continue;
            }
            for (Map.Entry<String, Integer> entry : graph.get(curr.city).entrySet()) {
                String toCity = entry.getKey();
                int newCost = curr.cost + entry.getValue();
                int newStop = curr.stop + 1;
                State minState = minStates.get(toCity);
                if (newCost < minState.cost || newStop < minState.stop) {
                    State nextState = new State(toCity, newCost, newStop);

                    //follow up for Print Path
                    nextState.prev = curr;

                    pq.offer(nextState);
                    if (newCost < minState.cost && newStop < minState.stop) {
                        minState.cost = newCost;
                        minState.stop = newStop;
                    }
                }
            }
        }
        return -1;
    }

    //follow up
    public void printPath(State state) {
        List<String> cities = new ArrayList<>();
        while (state.prev != null) {
            cities.add(state.city);
            state = state.prev;
        }
        cities.add(state.city);
        Collections.reverse(cities);
        System.out.println("The flight path : ");
        for (String city : cities) {
            System.out.print(city + " -> ");
        }
        System.out.println("DONE");
    }
}



//    /**
//     * DFS time Complexity : n ^ (k + 1)  every step we have worst n cities neighbor.
//     */
//    public int findByDfs(List<Flight> flights, String src, String dst, int k) {
//        Map<String, Map<String, Integer>> graph = createGraph(flights);
//        int[] minCost = new int[]{Integer.MAX_VALUE};
//        //follow up path
//        List<String> minPath = new ArrayList<>();
//        minPath.add(src);
//
//        dfs(graph, new HashSet<>(), new ArrayList<>(), minPath, src, dst, 0, 0, minCost, k);
//
//        //follow up print path
//        for (String s : minPath) {
//            System.out.print(s + " -> ");
//        }
//        System.out.println("DONE");
//        return minCost[0];
//    }
//
//    private void dfs(Map<String, Map<String, Integer>> graph, Set<String> visited,
//                     List<String> path, List<String> minPath,
//                     String curr, String dst,
//                     int currStop, int currCost,
//                     int[] minCost, int k) {
//        if (dst.equals(curr)) {
//            minCost[0] = Math.min(minCost[0], currCost);
//            if (minCost[0] == currCost) {
//                minPath.clear();
//                minPath.addAll(path);
//            }
//        }
//        if (currStop > k || currCost >= minCost[0]) {
//            return;
//        }
//        for (String next : graph.get(curr).keySet()) {
//            if (!visited.contains(next)) {
//                visited.add(next);
//                path.add(next);
//                dfs(graph, visited, path, minPath, next, dst, currStop + 1, currCost + graph.get(curr).get(next), minCost, k);
//                path.remove(path.size() - 1);
//                visited.remove(next);
//            }
//        }
//    }
//}
//
