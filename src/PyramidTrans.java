/**
 *   stack a pyramid matrix from bot to top
 *
 *   we have bot = "xxxxxx" a string,
 *   we first store all allowed word w to a map that w(0,2) -> w(3)
 *
 *   For example, bot = "ABCC"
 *   we try "AB" -> all possible chars, and dfs until we get top which is length 1
 *   and we return true;
 *
 */

import java.util.*;

class PyramidTrans {
    public boolean pyramidTransition(String bottom, List<String> allowed) {
        //store all allowed triples
        Map<String, Set<Character>> code = new HashMap<>();
        for (String str : allowed) {
            String pa = str.substring(0, 2);
            code.putIfAbsent(pa, new HashSet<>());
            code.get(pa).add(str.charAt(2));
        }

        return dfs(bottom, 0, "", code);
    }

    private boolean dfs(String currLevel, int index, String nextLevel, Map<String, Set<Character>> code) {
        //when we hit top 1 , we finished the pyramid
        if (currLevel.length() == 1) {
            return true;
        }
        //going to next upper level
        if (index == currLevel.length() - 1) {
            return dfs(currLevel, 0, "", code);
        }
        //try every possible mapping for current two blocks
        String pa = currLevel.substring(index, index + 2);
        if (!code.containsKey(pa)) {
            return false;
        }
        for (char ch : code.get(pa)) {
            if (dfs(currLevel, index + 1, nextLevel + ch, code)) {
                return true;
            }
        }
        return false;
    }
}
