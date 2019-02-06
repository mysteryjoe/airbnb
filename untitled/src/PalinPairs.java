import java.util.*;

/**
 *   Assume : 1. words are unique
 *          2. return all pairs, pair has order
 *          3. if we have index 0, word "aba", should we return [0, 0] ?
 *          // or should the pair be distinct word?
 *
 *
 *   For this problem, I suggest using hashmap
 *
 *   First, we reverse each word and put them into hashmap, mapping to itself.
 *
 *   Then we try each word w, split it into left and right
 *
 *   case 1:  left is in reverseMap. so w = left + right , + rev(left) would give us a pair only when right is palin
 *   case 2: right is in reverseMap, so w = rev(right) + left + right, would give us a pair only when left is palin
 *
 *   corner cases:
 *   For w
 *   if left = "",  if we find case 2: right = w in reverseMap, that rev(w) + w
 *   if right = "", we will also find case1: w + rev(w)
 *
 *   same for when we visit rev(w), we could find both w + rev(w) and rev(w) + w again
 *   that causes the duplicated count.
 *
 *   So we avoid it by setting right to be non empty.
 *
 *   And as we could see, if we have "", we will find "" + w and w + "" to be two pairs consider w is palin.
 *   Current we only have w + "" available to find,
 *   so we take consideration of "" as prefix, and find all self-palin words to be its pair

 *
 */

public class PalinPairs {
    public static void main(String[] args) {
        PalinPairs palinPairs = new PalinPairs();
        String[] test = new String[] {"", "aba", "abcd", "dcba", "acda", "c"};
        List<List<String>> res = palinPairs.getPalinHashMap(test);

        for (List<String> pair : res) {
            System.out.println("Pair : " + pair.get(0) + "|" + pair.get(1));
        }
    }

    public List<List<String>> getPalinHashMap(String[] words) {
        List<List<String>> res = new ArrayList<>();
        if (words == null || words.length == 0) {
            return res;
        }
        //reverse all the words and map to origin word
        Map<String, Integer> reverseMap = new HashMap<>();
        for (int i = 0; i < words.length; i++) {
            reverseMap.put(new StringBuilder(words[i]).reverse().toString(), i);
        }

        //find pair for each word
        for (int i = 0; i < words.length; i++) {
            String curr = words[i];
            //special case for "", we only need to find valid pair when it is prefix
            if (curr.equals("")) {
                for (int j = 0; j < words.length; j++) {
                    if (isPalin(words[j]) && i != j) {
                        res.add(Arrays.asList(words[i], words[j]));
                    }
                }
                continue;
            }
            //split word to prefix and suffix
            for (int j = 0; j < curr.length(); j++) {
                String prefix = curr.substring(0, j);
                String suffix = curr.substring(j);

                //case 1 word is first of palin pair
                if (reverseMap.containsKey(prefix) && isPalin(suffix)) {
                    //don't pair themself
                    if (reverseMap.get(prefix) != i) {
                        res.add(Arrays.asList(words[i], words[reverseMap.get(prefix)]));
                    }
                }

                //case 2 word is second
                if (reverseMap.containsKey(suffix) && isPalin(prefix)) {
                    if (reverseMap.get(suffix) != i) {
                        res.add(Arrays.asList(words[reverseMap.get(suffix)], words[i]));
                    }
                }

            }
        }
        return res;
    }

    private boolean isPalin(String word) {
        int i = 0;
        int j = word.length() - 1;
        while (i < j) {
            if (word.charAt(i) != word.charAt(j)) {
                return false;
            }
            i++;
            j--;
        }
        return true;
    }

    public List<List<String>> getPalindromePairs(String[] words) {
        //preprocess to add all words
        Trie trie = new Trie();
        for (String word : words) {
            trie.insert(reverseStr(word));
        }

        //search the palindrome pairs
        List<List<String>> pairs = new ArrayList<>();
        for (String word : words) {
            trie.searchPalinPairs(pairs, word);
        }
        return pairs;
    }

    private String reverseStr(String str) {
        return new StringBuilder(str).reverse().toString();
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
            for (char ch : sa) {
                children.putIfAbsent(ch, new TrieNode());
                curr = children.get(ch);
                children = curr.children;
            }
            curr.word = reverseStr(word);
            curr.hasWord = true;
        }

        public void searchPalinPairs(List<List<String>> pairs, String word) {
            char[] sa = word.toCharArray();
            TrieNode curr = root;
            Map<Character, TrieNode> children = curr.children;

            //case 1
            for (int i = 0; i < sa.length; i++) {
                if (!children.containsKey(sa[i])) {
                    return;
                }
                curr = children.get(sa[i]);
                children = curr.children;
                if (curr.hasWord && isPalin(sa, i + 1)) {

                    //if you don't allow self-pair
                    if (!word.equals(curr.word)) {
                        pairs.add(Arrays.asList(word, curr.word));
                    }
                }
            }

            //case 2
            Set<String> palinSuffix = new HashSet<>();
            depthFirstSearch(curr, palinSuffix, "");
            for (String str : palinSuffix) {
                if (word.equals("")) {
                    pairs.add(Arrays.asList(str, word));
                }
                pairs.add(Arrays.asList(word, str));
            }
        }

        private void depthFirstSearch(TrieNode curr, Set<String> palinSuffix, String suffix) {
            if (curr.hasWord && suffix.length() != 0 && isPalin(suffix.toCharArray(), 0)) {
                palinSuffix.add(curr.word);
            }
            for (char next : curr.children.keySet()) {
                depthFirstSearch(curr.children.get(next), palinSuffix, suffix + next);
            }
        }

        private boolean isPalin(char[] sa, int start) {
            int end = sa.length - 1;
            while (start < end) {
                if (sa[start] != sa[end]) {
                    return false;
                }
                start++;
                end--;
            }
            return true;
        }
    }
    class TrieNode {
        boolean hasWord;
        String word;
        Map<Character, TrieNode> children;

        public TrieNode() {
            children = new HashMap<>();
        }
    }
}

