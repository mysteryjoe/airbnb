import java.io.*;
import java.util.*;

/*
 key = "abc"

 cnt    timestamp
  5       1000
  3       2000
  2       3000

 get_hits(key, 0, 100000000) => 5 + 3 + 2 = 10
 get_hits(key, 0, 1500) => 5

 corner case:
 get_hits(key, 1000, 2000) => 3

 */

/*

data structure :
 Map<String, List<Entry>>

*/

class Entry {
    long timestamp;
    int cntSofar;

    public Entry(long timestamp, int cntSofar) {
        this.timestamp = timestamp;
        this.cntSofar = cntSofar;
    }
}

class Cache {
    // ex. hit("abc", 10, 1000) => at tiemstamp 1000, we hit "abc" for 10 times
    private Map<String, List<Entry>> countMap;

    public Cache() {
        countMap = new HashMap<>();
    }
    public void hit(String key, int cnt, long timestamp) {
        countMap.putIfAbsent(key, new ArrayList<>());
        List<Entry> list = countMap.get(key);
        int newCount = cnt;
        if (!list.isEmpty()) {
            newCount += list.get(list.size() - 1).cntSofar;
        }
        countMap.get(key).add(new Entry(timestamp, newCount));
    }

    // ex. return how many times the key was hit during (startTime, endTime].
    public int get_hits(String key, long startTime, long endTime) {
        if (!countMap.containsKey(key)) {
            return 0;
        }
        List<Entry> hitHistory = countMap.get(key);
        //first, find the entry with timestamp > startTime
        int firstIdx = binarySearch(hitHistory, startTime);

        if (firstIdx == -1) {
            return 0;
        } else {
            firstIdx -= 1;
        }

        int lastIdx = binarySearch(hitHistory, endTime);
        if (lastIdx == -1) {
            lastIdx = hitHistory.size() - 1;
        } else {
            lastIdx -= 1;
        }


        if (firstIdx == -1) {
            return hitHistory.get(lastIdx).cntSofar;
        }
        return hitHistory.get(lastIdx).cntSofar - hitHistory.get(firstIdx).cntSofar;
    }

    //binary search to return first index of Entry
    //whose timestamp is > given timestamp
    public int binarySearch(List<Entry> hitHistory, long timestamp) {
        int res = -1;
        int start = 0;
        int end = hitHistory.size() - 1;
        while (start <= end) {
            int mid = start + (end - start) / 2;
            long currTime = hitHistory.get(mid).timestamp;
            if (currTime > timestamp) {
                res = mid;
                end = mid - 1;
            } else {
                start = mid + 1;
            }
        }
        return res;
    }

}





class Solution {
    public static void main(String[] args) {
        Cache cache = new Cache();
        cache.hit("abc", 5, 1000);
        cache.hit("abc", 3, 2000);
        cache.hit("abc", 2, 3000);
        System.out.println("Cnt between " + 0 + " and " + 1500 + " is " + cache.get_hits("abc", 0, 1500));
        //first check our hit function

        System.out.println("Cnt between " + 1000 + " and " + 2000 + " is " + cache.get_hits("abc", 1000, 2000));

        System.out.println("Cnt between " + 0 + " and " + 100000 + " is " + cache.get_hits("abc", 0, 100000));

        System.out.println("Cnt between " + 1000 + " and " + 3000 + " is " + cache.get_hits("abc", 1000, 3000));

        System.out.println("Cnt between " + 0 + " and " + 3000 + " is " + cache.get_hits("abc", 0, 3000));

        System.out.println("Cnt between " + 4000 + " and " + 5000 + " is " + cache.get_hits("abc", 4000, 5000));
    }
}
