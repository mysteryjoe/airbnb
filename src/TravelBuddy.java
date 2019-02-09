import java.util.*;

public class TravelBuddy {
    public static void main(String[] args) {
        TravelBuddy sol = new TravelBuddy();
        Map<String, List<String>> test = new HashMap<>();
        test.put("teddy", Arrays.asList("A", "B", "C", "D"));
        test.put("Amy", Arrays.asList("A", "C", "E", "F", "D", "M"));
        test.put("Me", Arrays.asList("A", "B", "D"));
        test.put("Jerry", Arrays.asList("B", "D", "M", "N"));

        List<String> res = sol.findTravelBuddy(test, "Me");
        for (String s : res) {
            System.out.println(s);
        }
    }

    public List<String> findTravelBuddy(Map<String, List<String>> travelers, String target) {
        List<String> buddies = new ArrayList<>();
        Map<String, Double> similarities = new HashMap<>();
        if (!travelers.containsKey(target)) {
            return buddies;
        }

        Set<String> myList = new HashSet<>(travelers.get(target));
        for (String buddy : travelers.keySet()) {
            if (buddy.equals(target)) {
                continue;
            }

            List<String> list = travelers.get(buddy);
            if (list.isEmpty()) {
                continue;
            }
            int count = 0;
            for (String city : list) {
                if (myList.contains(city)) {
                    count++;
                }
            }
            if ((double) count / list.size() >= 0.5) {
                buddies.add(buddy);
                similarities.put(buddy, (double) count / list.size());
            }
        }
        buddies.sort(Comparator.comparing(a -> -similarities.get(a)));
        return buddies;
    }


}
