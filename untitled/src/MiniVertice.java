import java.util.*;

/**
 *   a lot of pairs (A -> B)  (B -> C)
 *
 *   find minimal number of vertices that could traverse the graph
 *
 *   examples : A -> B - > C -> E
 *                           -> D -> B
 *
 *
 *          A->B  B->A  B-> E  C->E  C->D  D->C
 */

public class MiniVertice {

    private Set<String> findMinVertices(List<String[]> edges) {
        //create graph
        Map<String, Set<String>> graph = new HashMap<>();
        for (String[] edge : edges) {
            graph.putIfAbsent(edge[0], new HashSet<>());
            graph.putIfAbsent(edge[1], new HashSet<>());
            graph.get(edge[0]).add(edge[1]);
        }

        //we use set of res to record minimal nodes
        //and visited for those has been visited
        Set<String> visited = new HashSet<>();
        Set<String> res = new HashSet<>();

        //take each node as a potential source,
        //and remove those older sources who could be achieved by
        //current source.
        for (String v : graph.keySet()) {
            if (!visited.contains(v)) {
                visited.add(v);
                //add potential answer to set
                res.add(v);
                dfs(res, graph, v, v, visited, new HashSet<>());
            }
        }
        return res;
    }

    private void dfs(Set<String> res, Map<String, Set<String>> graph, String currNode, String startNode,
                     Set<String> visited, Set<String> currVisited) {
        currVisited.add(currNode);
        visited.add(currNode);
        for (String next : graph.get(currNode)) {
            //remove older sources who could be achieved by start Node
            if (res.contains(next) && !next.equals(startNode)) {
                res.remove(next);
            }
            //visit each node once
            if (!currVisited.contains(next)) {
                dfs(res, graph, next, startNode, visited, currVisited);
            }
        }
    }

    public static void main(String[] args) {
        List<String[]> test = new ArrayList<>();
        test.add(new String[] {"A", "B"});
        test.add(new String[] {"B", "C"});
        test.add(new String[] {"C", "D"});
        test.add(new String[] {"C", "E"});

        MiniVertice miniVertice = new MiniVertice();
        Set<String> res = miniVertice.findMinVertices(test);
        for (String s : res) {
            System.out.println(s);
        }
    }
}
