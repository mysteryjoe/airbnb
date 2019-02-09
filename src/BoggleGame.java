import java.util.*;

public class BoggleGame {
    public List<String> getMaxWords(char[][] board, String[] words) {
        Trie trie = new Trie();
        for (String word : words) {
            trie.insert(word);
        }

        List<String> res = new ArrayList<>();
        int n = board.length;
        int m = board[0].length;
        //choose start word's position
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                boolean[][] visited = new boolean[n][m];
                findWords(res, board, visited, new ArrayList<>(), i, j, trie.root, trie.root);
            }
        }
        return res;
    }

    int[] dx = new int[] {1, -1, 0, 0};
    int[] dy = new int[] {0, 0, 1, -1};
    private void findWords(List<String> res, char[][] board, boolean[][] visited, List<String> currWords, int x, int y, TrieNode root, TrieNode curr) {
        int n = board.length;
        int m = board[0].length;
        if (x < 0 || x >= n || y < 0 || y >= n || visited[x][y] || !curr.children.containsKey(board[x][y])) {
            return;
        }

        curr = curr.children.get(board[x][y]);
        visited[x][y] = true;

        // If current is a word, we can start form current index and to next round dfs;
        if (curr.hasWord) {
            currWords.add(curr.word);
            if (currWords.size() > res.size()) {
                res.clear();
                res.addAll(currWords);
            }
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    findWords(res, board, visited, currWords, i, j, root, root);
                }
            }
            currWords.remove(currWords.size() - 1);
            return;
        }

        // Current is not a word, we need to broader its 4-driections
        for (int i = 0; i < 4; i++) {
            findWords(res, board, visited, currWords, x + dx[i], y + dy[i], root, curr);
        }
        visited[x][y] = false;
    }


    public static void main(String[] args) {
        BoggleGame boggleGame = new BoggleGame();
        List<String> res = boggleGame.getMaxWords(new char[][] {
                {'a', 'a', 'a'},
                {'a', 'a', 'a'},
                {'a', 'a', 'a'}
        }, new String[] {"aaa", "aa", "a", "aaa"});
        for (String s : res) {
            System.out.println(s);
        }
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
        Map<Character, TrieNode> children = curr.children;
        for (int i = 0; i <sa.length; i++) {
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