import java.util.*;

/**
 *   1. is there any duplicates? could we choose same dish multiple times?
 *   2.  ask us to find all combinations
 *
 *   suppose we have List<Dish> menu
 *
 *   Time complexity: N dishes, each dish could have 0 to target / price times to choose.
 *   worst case O(k ^ N） but should be smaller than it.
 *   f(n, t) = f(n, t - p0) + f(n - 1, t - p1) + f(n - 1, t - p2 ).
 *
 */

public class MenuSum {
    private static final double elipson = 1e-6;
    public List<List<String>> getCombinationSum(Map<String, Double> menu, double target) {
        List<List<String>> combinations = new ArrayList<>();
        //sort to avoid duplicate dfs path
        List<String> dishes = new ArrayList<>(menu.keySet());

        dishes.sort((a, b) -> {
            if (Math.abs(menu.get(a) - menu.get(b)) < elipson) {
                return 0;
            } else if (menu.get(a) < menu.get(b)) {
                return -1;
            } else {
                return 1;
            }
        });
        backTrack(combinations, new ArrayList<>(), dishes, menu, 0, target);
        return combinations;
    }

    private void backTrack(List<List<String>> combinations,
                           List<String> curr,
                           List<String> dishes,
                           Map<String, Double> menu,
                           int item,
                           double target) {
        if (target <= elipson) {
            combinations.add(new ArrayList<>(curr));
            return;
        }

        for (int i = item; i < dishes.size(); i++) {
            String dish = dishes.get(i);
            // since our list is sorted by prices
            // we don't need to consider dishes after it
            if (menu.get(dish) - target > elipson) {
                break;
            }
            curr.add(dish);
            backTrack(combinations, curr, dishes, menu, i, target - menu.get(dish));
            curr.remove(curr.size() - 1);
        }
    }


    public static void main(String[] args) {
        MenuSum menuSum = new MenuSum();
        Map<String, Double> menu = new HashMap<>();
        menu.put("A", 12.2);
        menu.put("B", 11.3);
        menu.put("C", 10.4);
        menu.put("D", 8.5);
        menu.put("E", 1.6);
        List<List<String>> res = menuSum.getCombinationSum(menu, 100);
        System.out.println(res.size());
        for (List<String> ans : res) {
            System.out.print("Combination : [ ");
            for (String s : ans) {
                System.out.print(s + " ");
            }
            System.out.println("]");
            System.out.println();
        }
    }
}
