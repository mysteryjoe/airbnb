import java.util.*;

/**
 *
 * using topological sort and bfs
 *
 * Example : Input:
 * [
 *   "wrt",
 *   "wrf",
 *   "er",
 *   "ett",
 *   "rftt"
 * ]
 *
 * Output: "wertf"
 *
 * compare each adjacent words and find first column with different chars
 * That gives us the ordering information
 *
 * follow up 1: find all possible dict
 *
 *         using backtrack on current state (List<Character> sources, indegrees, graph)
 *         and try every source as next char, update indegrees, dfs, backtrack
 *
 *
 *
 * follow up 2: using dfs instead of bfs
 *
 *
 */
public class AlienDict {

    public String alienOrder(String[] words) {
        if (words == null || words.length == 0) {
            return "";
        }
        Map<Character, Set<Character>> graph = new HashMap<>();
        Map<Character, Integer> indegrees = new HashMap<>();

        //initialize and build graph
        buildGraph(words, graph, indegrees);

        //topological sort on the graph

        //first add sources
        Queue<Character> queue = new LinkedList<>();
        for (char ch : indegrees.keySet()) {
            if (indegrees.get(ch) == 0) {
                queue.offer(ch);
            }
        }

        //bfs and keep track of our result order
        String res = "";
        while (!queue.isEmpty()) {
            char curr = queue.poll();
            res += curr;
            for (char next : graph.get(curr)) {
                indegrees.put(next, indegrees.get(next) - 1);
                if (indegrees.get(next) == 0) {
                    queue.offer(next);
                }
            }
        }

        if (res.length() != graph.size()) {
            return "";
        }
        return res;
    }

    private void buildGraph(String[] words, Map<Character, Set<Character>> graph, Map<Character, Integer> indegrees) {
        //create every vertex
        for (String word : words) {
            for (char c : word.toCharArray()) {
                graph.putIfAbsent(c, new HashSet<>());
                indegrees.putIfAbsent(c, 0);
            }
        }

        //find topological order
        for (int i = 0; i < words.length - 1; i ++) {

            for (int j = 0; j < words[i].length() && j < words[i + 1].length(); j++) {
                char from = words[i].charAt(j);
                char to = words[i + 1].charAt(j);

                if (from != to) {
                    //avoid duplicate
                    if (!graph.get(from).contains(to)) {
                        graph.get(from).add(to);
                        indegrees.put(to, indegrees.get(to) + 1);
                    }
                    break;
                }
            }
        }
    }

    /* -------------------------------------------------------------*/
    //   follow up 1 could we get all combination as List<String> ?
    public List<String> alienOrderAll(String[] words) {
        List<String> res = new ArrayList<>();
        if (words == null || words.length == 0) {
            return res;
        }
        Map<Character, Set<Character>> graph = new HashMap<>();
        Map<Character, Integer> indegrees = new HashMap<>();

        //initialize and build graph
        buildGraph(words, graph, indegrees);

        //backtrack to get all possible dicts
        Set<Character> sources = new HashSet<>();
        for (char c : indegrees.keySet()) {
            if (indegrees.get(c) == 0) {
                sources.add(c);
            }
        }

        backTrack(sources, "", res, graph, indegrees);
        return res;
    }

    private void backTrack(Set<Character> sources,
                           String dict,
                           List<String> res,
                           Map<Character, Set<Character>> graph,
                           Map<Character, Integer> indegrees) {
        if (sources.size() == 0) {
            if (dict.length() == graph.size()) {
                res.add(dict);
                return;
            }
        }

        for (char next = 'a'; next <= 'z'; next++) {
            if (!sources.contains(next)) {
                continue;
            }
            //choose one as next char order
            sources.remove(next);
            for (char neighbor : graph.get(next)) {
                indegrees.put(neighbor, indegrees.get(neighbor) - 1);
                if (indegrees.get(neighbor) == 0) {
                    sources.add(neighbor);
                }
            }
            backTrack(sources, dict + next, res, graph, indegrees);

            //backtrack, restore
            sources.add(next);
            for (char neighbor : graph.get(next)) {
                if (indegrees.get(neighbor) == 0) {
                    sources.remove(neighbor);
                }
                indegrees.put(neighbor, indegrees.get(neighbor) + 1);
            }
        }
    }

    /* -------------------------------------------------------------*/
    /*    follow up 2. Using DFS to solve it
     *     each vertex has three status.
     *     not visited
     *     visiting: still visiting
     *     visited: already visited
     *
     *     we start depth first search each vertex
     *     1. if vertex is visiting, which means cycle detected, return false
     *     2. if vertex is visited, then pass it
     *     3. if it's unknown, we start dfs on it
     *
     *     On dfs, we do exactly same thing above to current vertex's neighbors
     *     we use postorder to add our dict order, and reverse it back, it's the answer;
     *
     */


    enum Status {
        UNKNOWN, VISITING, VISITED
    }

    public String alienOrderDfs(String[] words) {
        if (words == null || words.length == 0) {
            return "";
        }
        Map<Character, Set<Character>> graph = new HashMap<>();
        Map<Character, Status> visitStatus = new HashMap<>();
        buildGraph2(words, graph, visitStatus);

        //do dfs on each vertex, add the char in finising order(postorder)
        StringBuilder ans = new StringBuilder();
        for (char ch : graph.keySet()) {
            if (visitStatus.get(ch) == Status.UNKNOWN) {
                if (!dfs(graph, ch, visitStatus, ans)) {
                    return "";
                }
            } else if (visitStatus.get(ch) == Status.VISITING) {
                return "";
            }
        }
        return ans.reverse().toString();
    }

    private boolean dfs(Map<Character, Set<Character>> graph, char curr, Map<Character, Status> visitStatus, StringBuilder ans) {
        visitStatus.put(curr, Status.VISITING) ;
        for (char next : graph.get(curr)) {
            if (visitStatus.get(next) == Status.UNKNOWN) {
                if (!dfs(graph, next, visitStatus, ans)) {
                    return false;
                }
            } else if (visitStatus.get(next) == Status.VISITING) {
                return false;
            }
        }
        visitStatus.put(curr, Status.VISITED);
        ans.append(curr);
        return true;
    }

    private void buildGraph2(String[] words, Map<Character, Set<Character>> graph, Map<Character, Status> visitStatus) {
        //create every vertex and set them to unknown yet
        for (String word : words) {
            for (char c : word.toCharArray()) {
                graph.putIfAbsent(c, new HashSet<>());
                visitStatus.put(c, Status.UNKNOWN);
            }
        }

        //find topological order
        for (int i = 0; i < words.length - 1; i ++) {
            int index = 0;
            while (index < words[i].length() && index < words[i + 1].length()) {
                char from = words[i].charAt(index);
                char to = words[i + 1].charAt(index);

                if (from != to) {
                    if (!graph.get(from).contains(to)) {
                        graph.get(from).add(to);
                    }
                    break;
                }
                index++;
            }
        }
    }

    public static void main(String[] args) {
        AlienDict alienDict = new AlienDict();
        String[] test1 = new String[] {"wa", "ac", "ew", "ea"};
        testFunc(test1, alienDict.alienOrder(test1));

        //test should return empty
        String[] test2 = new String[] {"z", "x", "z"};
        testFunc(test1, alienDict.alienOrder(test1));

        //test follow up 1
        String[] test3 = new String[] {"wa", "ba"};
        testFunc2(test3, alienDict);

        String[] test4 = new String[] {"wrt","wrf","er","ett","rftt"};
        testFunc(test4, alienDict.alienOrderDfs(test4));

    }

    private static void testFunc(String[] words, String order) {
        System.out.println("Alien dict has words : ");
        for (String word : words) {
            System.out.println(word);
        }
        System.out.println();
        System.out.println("we get order : " + order);
        System.out.println("------------------------");

    }

    public static void testFunc2(String[] words, AlienDict alienDict) {
        System.out.println("Alien dict has words : ");
        for (String word : words) {
            System.out.println(word);
        }
        System.out.println();
        List<String> res = alienDict.alienOrderAll(words);
        System.out.println("we get possible order lists : " + res.size());
        for (String s : res) {
            System.out.println(s);
        }
        System.out.println("------------------------");

    }


}
