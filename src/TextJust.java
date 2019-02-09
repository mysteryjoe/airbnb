import java.util.*;

/**
 *  given a list of strings
 */
public class TextJust {
    public void printI(List<String> sentences) {
        //first find longest one
        int maxLen = 0;
        for (String s : sentences) {
            maxLen = Math.max(s.length(), maxLen);
        }

        for (String s : sentences) {
            printHorizontal(maxLen);
            printWord(s, maxLen);
        }
        printHorizontal(maxLen);
    }

    public void printII(List<String> sentences, int width) {
        for (String sentence : sentences) {
            printHorizontal(width);
            String curr = "";
            String[] words = sentence.split(" ");
            int idx = 0;
            while (idx < words.length) {
                if (curr.length() + words[idx].length() > width) {
                    printWord(curr, width);
                    curr = words[idx] + " ";
                } else {
                    curr += words[idx] + " ";
                }
                idx++;
            }
            if (!curr.isEmpty()) {
                printWord(curr, width);
            }
        }
        printHorizontal(width);
    }

    private void printWord(String s, int width) {
        s = s.trim();
        s = "|" + s;
        while (s.length() <= width) {
            s += " ";
        }
        System.out.println(s + "|");
    }
    private void printHorizontal(int len) {
        String res = "+";
        for (int i = 0; i < len; i++) {
            res += "-";
        }
        res += "+";
        System.out.println(res);
    }

    public static void main(String[] args) {
        TextJust textJust = new TextJust();
        List<String> test = new ArrayList<>(Arrays.asList("first word", "my second sentence", "now it's third"));
        textJust.printII(test, 14);
    }
}
