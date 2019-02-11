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
    private final double epsilon = 1e-6;
    public List<List<String>> findMenuCombo(Map<String, Double> menu, double target) {
        List<String> dishList = new ArrayList<>(menu.keySet());
        dishList.sort(Comparator.comparing(a -> menu.get(a)));
        List<List<String>> comboList = new ArrayList<>();
        dfs(comboList, dishList, menu, new ArrayList<>(), 0, target);
        return comboList;
    }

    private void dfs(List<List<String>> comboList,
                     List<String> dishList,
                     Map<String, Double> menu,
                     List<String> currCombo,
                     int index,
                     double money) {
        if (Math.abs(money) <= epsilon) {
            comboList.add(new ArrayList<>(currCombo));
            return;
        }

        for (int i = index; i < dishList.size(); i++) {
            String currDish = dishList.get(i);
            if (menu.get(currDish) > money + epsilon) {
                break;
            }
            currCombo.add(currDish);
            dfs(comboList, dishList, menu, currCombo, i, money - menu.get(currDish));
            currCombo.remove(currCombo.size() - 1);
        }
    }

    //follow up if we want to find minCombo
    public List<String> findMinCombo(Map<String, Double> menu, double target) {
        List<String> dishList = new ArrayList<>(menu.keySet());
        dishList.sort(Comparator.comparing(a -> menu.get(a)));
        List<String> minCombo = new ArrayList<>();
        dfsMinCombo(minCombo, dishList, menu, new ArrayList<>(), 0, target);
        return minCombo;
    }

    private void dfsMinCombo(List<String> minCombo,
                             List<String> dishList,
                             Map<String, Double> menu,
                             List<String> currCombo,
                             int index,
                             double money) {
        if (Math.abs(money) <= epsilon) {
            minCombo.clear();
            minCombo.addAll(currCombo);
            return;
        }

        if (minCombo.size() != 0 && currCombo.size() + 1 >= minCombo.size()) {
            return;
        }

        for (int i = index; i < dishList.size(); i++) {
            String currDish = dishList.get(i);
            if (menu.get(currDish) > money + epsilon) {
                break;
            }
            currCombo.add(currDish);
            dfsMinCombo(minCombo, dishList, menu, currCombo, i, money - menu.get(currDish));
            currCombo.remove(currCombo.size() - 1);
        }
    }

    public static void main(String[] args) {
        MenuSum menuSum = new MenuSum();
        Map<String, Double> menu = new HashMap<>();
        menu.put("A", 12.255);
        menu.put("B", 11.355);
        menu.put("C", 10.455);
        menu.put("D", 8.555);
        menu.put("E", 1.655);
        List<List<String>> res = menuSum.findMenuCombo(menu, 100);
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