import java.util.*;

public class Collatz {
    Map<Long, Long> map = new HashMap<>();

    public long findStep(long num) {
        if (num == 1) {
            return 0;
        }
        if (map.containsKey(num)) {
            return map.get(num);
        }
        if (num % 2 == 0) {
            return 1 + findStep( num / 2);
        } else {
            return 1 + findStep(num * 3 + 1);
        }
    }

    public long findLongestStep(long num) {
        if (num < 1) {
            return 0;
        }
        long res = 0;
        long one = 0;
        for (long i = 1; i <= num; i++) {
            long t = findStep(i);
            map.put(i, t);
            res = Math.max(res, t);
            if (res == t) {
                one = i;
            }
        }
        System.out.println(one);
        return res;
    }

    public static void main(String[] args) {
        Collatz collatz = new Collatz();
        long num = collatz.findLongestStep(1000000);
        System.out.println(num);
        System.out.println(collatz.findStep(910107));
    }
}
