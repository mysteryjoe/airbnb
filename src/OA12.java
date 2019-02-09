import java.util.*;

public class OA12 {
    public static void main(String[] args) {
        //test example case
        List<Float> t1 = Arrays.asList(0.7f, 2.80f, 4.90f);
        roundPricesToMatchTarget(t1, getSum(t1));

        //all integer case
        List<Float> t2 = Arrays.asList(1.0f, 2.0f, 3.0f, 4.0f);
        roundPricesToMatchTarget(t2, getSum(t2));


        //corner cases
        List<Float> t3 = Arrays.asList(0.3f, 1.2f, 2.3f);
        roundPricesToMatchTarget(t3, getSum(t3));
    }

    public static List<Integer> roundPricesToMatchTarget(List<Float> prices, int target) {
        List<Integer> res = new ArrayList<Integer>();
        Queue<Diff> pq = new PriorityQueue<>((a, b) -> {
            if (a.diff < b.diff) {
                return 1;
            } else {
                return -1;
            }
        });

        //first set all prices to their floor values
        int floorSum = 0;
        for (int i = 0; i < prices.size(); i++) {
            int floor = prices.get(i).intValue();
            floorSum += floor;
            pq.offer(new Diff(i, prices.get(i) - floor));
            res.add(floor);
        }

        //then convert to ceil values from greatest diff
        //which means after converting, the diff will be minimal adding to final answer
        while (target > floorSum) {
            if (pq.isEmpty()) {
                return null;
            }
            Diff diff = pq.poll();
            res.set(diff.index, res.get(diff.index) + 1);
            floorSum++;
        }
        print(prices, res);
        return res;
    }

    static class Diff {
        int index;
        double diff;

        public Diff(int index, double diff) {
            this.index = index;
            this.diff = diff;
        }
    }

    public static void print(List<Float> prices, List<Integer> res) {
        System.out.println("--------------------");
        for (int i = 0; i < prices.size(); i++) {
            System.out.println(prices.get(i) +" - > " + res.get(i));
        }
    }

    public static int getSum(List<Float> prices) {
        double sum = 0.0;
        for (double p : prices) {
            sum += p;
        }
        return (int) Math.round(sum);
    }


}
