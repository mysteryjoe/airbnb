import java.util.HashMap;
import java.util.Map;

/**
 *  use least numbers to get target, could choose unlimited times
 */
public class CombiSum {
    public static void main(String[] args) {
        CombiSum combiSum = new CombiSum();
        int[] test = new int[] {1, 2, 3, 4 ,5};
        System.out.println(combiSum.leastCombinationSum(test, 9));
    }

    public int leastCombinationSum(int[] nums, int target) {
        //define f[i] as least # of numbers to sum to i
        int[] f = new int[target + 1];

        //follow up
        Map<Integer, Integer> prev = new HashMap<>();

        int n = nums.length;
        f[0] = 0;
        //init to impossible large number
        for (int i = 1; i <= target; i++) {
            f[i] = n + 1;
        }

        for (int i = 0; i < nums.length; i++) {
            for (int j = nums[i]; j <= target; j++) {
                f[j] = Math.min(f[j], f[j - nums[i]] + 1);

                //follow up
                if (f[j] == f[j - nums[i]] + 1) {
                    prev.put(j, nums[i]);
                }
            }
        }

        if (f[target] == n + 1) {
            return -1;
        }

        //follow up
        //print result
        int temp = target;
        while (temp > 0) {

            System.out.println(prev.get(temp));
            temp -= prev.get(temp);
        }
        return f[target];
    }
}
