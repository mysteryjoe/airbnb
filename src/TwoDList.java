import java.util.*;

/**
 *     corner cases: contains null and empty []
 *     [ null, [], [1, 2, null], [3], [4, 5]]
 */
public class TwoDList {
    private Iterator<List<Integer>> rowIter;
    private Iterator<Integer> colIter;

    public TwoDList(List<List<Integer>> vec2d) throws Exception {
        if (vec2d == null) {
            throw new Exception();
        }
        rowIter = vec2d.iterator();
        colIter = null;
    }

    public Integer next() throws Exception {
        if (hasNext()) {
            return colIter.next();
        }
        throw new Exception();
    }

    public boolean hasNext() {
        while ((colIter == null || !colIter.hasNext()) && rowIter.hasNext()) {
            List<Integer> list = rowIter.next();
            if (list != null) {
                colIter = list.iterator();
            }
        }
        return colIter != null && colIter.hasNext();
    }

    public void remove() {
        if (colIter == null) {
            return;
        }

        colIter.remove();
    }


    public static void main(String[] args) throws Exception {
        List<List<Integer>> vec2D = new ArrayList<>();
        vec2D.add(null);
        vec2D.add(null);
        List<Integer> vec1d = new ArrayList<>();
        vec1d.add(null);
        vec1d.add(null);
        vec1d.add(5);

        vec2D.add(new ArrayList<>());
        vec2D.add(vec1d);
        // [null, null, [], [null, null, 5]]
        TwoDList twoDList = new TwoDList(vec2D);

        //test for remove
        System.out.println("Has next ? : " + twoDList.hasNext());
        System.out.println("First element : " + twoDList.next());
        //should be 3
        System.out.println(vec2D.get(3).size());
        twoDList.remove();
        System.out.println("Second element : " + twoDList.next());
        //should be 2
        System.out.println(vec2D.get(3).size());
        twoDList.remove();
        //should only has 1
        System.out.println("Third element : " + twoDList.next());
        System.out.println(vec2D.get(3).size());
        System.out.println();

        //test for normal case
        List<List<Integer>> test2 = new ArrayList<>();
        test2.add(Arrays.asList(1, 2));
        test2.add(new ArrayList<>());
        test2.add(Arrays.asList(3, null, 5, 7));
        int index = 0;
        // [1, 2], [], [3, 5 ,7]
        TwoDList test = new TwoDList(test2);
        while (test.hasNext()) {
            System.out.println(index + "th elem is " + test.next());
            index++;
        }


    }
}
