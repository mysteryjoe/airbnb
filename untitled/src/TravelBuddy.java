import java.util.*;

/**
 *
 */
public class TravelBuddy {
    class Buddy {
        Set<String> cities;
        double similarity;
        String name;

        public Buddy(String name, Set<String> cities, double similarity) {
            this.name = name;
            this.cities = cities;
            this.similarity = similarity;
        }
    }
    public List<Buddy> findBuddy(List<String> myWishList, List<Buddy> buddies) {
        Set<String> wishList = new HashSet<>(myWishList);
        List<Buddy> res = new ArrayList<>();
        for (Buddy buddy : buddies) {
            int count = 0;
            for (String city : buddy.cities) {
                if (wishList.contains(city)) {
                    count++;
                }
            }
            buddy.similarity = count / buddy.cities.size();

            if (buddy.similarity >= 0.5) {
                res.add(buddy);
            }
        }
        res.sort(Comparator.comparing(a -> a.similarity));
        return res;
    }

    public List<String> recomendCities(List<String> myWishList, List<Buddy> buddies, int k) {
        Set<String> wishList = new HashSet<>(myWishList);
        Set<String> res = new LinkedHashSet<>();

        buddies.sort(Comparator.comparing(a -> a.similarity));

        for (Buddy buddy : buddies) {
            for (String city : buddy.cities) {
                if (!wishList.contains(city) && res.size() < k) {
                    res.add(city);
                }
            }
        }
        List<String> list = new ArrayList<>();
        Iterator<String> it = res.iterator();
        while (it.hasNext()) {
            list.add(it.next());
        }
        return list;
    }
}
