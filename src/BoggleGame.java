import java.util.*;

public class BoggleGame {
    int[] dx = new int[] {1, -1, 0, 0};
    int[] dy = new int[] {0, 0, 1, -1};
    public List<String> findCombo(char[][] board, String[] words) {
        Trie trie = new Trie();
        for (String word : words) {
            trie.insert(word);
        }

        List<String> res = new ArrayList<>();
        int n = board.length;
        int m = board[0].length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                //set first word's position
                dfs(res, new ArrayList<>(), new boolean[n][m], board, i, j, trie.root, trie.root);
            }
        }
        return res;
    }

    private void dfs(List<String> res, List<String> currWords, boolean[][] visited, char[][] board, int x, int y, TrieNode root, TrieNode curr) {
        int n = board.length;
        int m = board[0].length;
        if (x < 0 || x >= n || y < 0 || y >= m || visited[x][y] || !curr.children.containsKey(board[x][y])) {
            return;
        }

        visited[x][y] = true;
        curr = curr.children.get(board[x][y]);
        if (curr.hasWord) {
            currWords.add(curr.word);
            if (currWords.size() > res.size()) {
                res.clear();
                res.addAll(currWords);
            }

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    dfs(res, currWords, visited, board, i, j, root, root);
                }
            }
            currWords.remove(currWords.size() - 1);

            return;
        }

        for (int i = 0; i < 4; i++) {
            int nx = x + dx[i];
            int ny = y + dy[i];

            dfs(res, currWords, visited, board, nx, ny, root, curr);
        }

        visited[x][y] = false;
    }


    public static void main(String[] args) {
        BoggleGame boggleGame = new BoggleGame();
        char[][] test = new char[][] {
                {'e', 's', 'y', 'z'},
                {'n', 'e', 'p', 'k'},
                {'s', 't', 'c', 'z'},
                {'x', 'i', 'o', 'i'}
        };
        String[] words = new String[] {
                "set", "pen", "ten", "pet", "pets", "petioczi", "zypk", "czi"
        };

        System.out.println(boggleGame.findCombo(test, words));
    }

}

class Trie {
    TrieNode root;

    public Trie() {
        root = new TrieNode();
    }

    public void insert(String word) {
        char[] sa = word.toCharArray();
        TrieNode curr = root;
        Map<Character, TrieNode> children = root.children;

        for (int i = 0; i < sa.length; i++) {
            children.putIfAbsent(sa[i], new TrieNode());
            curr = children.get(sa[i]);
            children = curr.children;
        }
        curr.hasWord = true;
        curr.word = word;
    }
}

class TrieNode {
    Map<Character, TrieNode> children;
    boolean hasWord;
    String word;

    public TrieNode() {
        children = new HashMap<>();
    }
}
