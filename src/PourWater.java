/**
 *    make assumptions :
 *    1. handle the water drop by drop
 *    2.  If the droplet would eventually fall by moving left, then move left.
 *    3.  Otherwise, if the droplet would eventually fall by moving right, then move right.
 *    4.  Otherwise, rise at it's current position.
 *
 *     eventually fall" means that the droplet will eventually be at a lower level if it moves in that direction.
 *
 *
 *    7. If droplet will leaves out left, we first check if it could be dropped at right side.
 *       If it's not (either because it leaves out right, or right too high) , it will leaves out
 *
 *
 *    example:
 *    [3, 1, 1, 1, 1, 3]
 *
 *    drop 1st at index 3
 *    [3, 1, 1, 2, 1, 3]
 *
 *    2nd:
 *
 *    [3, 1, 2, 2, 1, 3]
 *
 *    follow up: 假设两边两个墙有个高度 分别为leftH 和rightH, 则只需要在水流最后pos的地方判断是否是0 or n，然后判断当前高度是否超过

 */
public class PourWater {

    public void pourWater(int[] heights, int water, int pos) {
        int[] waters = new int[heights.length];
        for (int i = 0; i < water; i++) {
            pour(heights, pos, waters);
        }
        print(heights, waters);
    }

    /**
     * method that only pour one droplet at the position
     * @param heights
     * @param pos
     */

    //假设 左墙在 index 0
    // 右墙在 index n - 1
    private void pour(int[] heights, int pos, int[] waters) {
        int n = heights.length;
        boolean leftOut = false;
        int currIdx = pos;
        int leftPosIdx = pos;
        while (currIdx >= 0 && heights[currIdx] + waters[currIdx] <= heights[leftPosIdx] + waters[leftPosIdx]) {
            if (heights[currIdx] + waters[currIdx] <= heights[leftPosIdx] + waters[leftPosIdx]) {
                leftPosIdx = currIdx;
            }
            currIdx--;
        }

        if (currIdx == -1) {
            leftOut = true;
        } else if (leftPosIdx != pos) {
            //move left if it falls
            waters[leftPosIdx]++;
            return;
        }

        //else move right
        currIdx = pos;
        int rightPosIdx = pos;
        while (currIdx < heights.length
                && heights[currIdx] + waters[currIdx] <= heights[rightPosIdx] + waters[rightPosIdx]) {
            if (heights[currIdx] + waters[currIdx] < heights[rightPosIdx] + waters[rightPosIdx]) {
                rightPosIdx = currIdx;
            }
            currIdx++;
        }
        //if it leaves out right, or it could not find right pos
        // to drop and it previous might leaves out left
        if (currIdx == n || (rightPosIdx == pos && leftOut)) {
            return;
        }
        waters[rightPosIdx]++;
    }

    private void print(int[]heights, int[] waters) {
        int n = heights.length;
        int maxHeight = Integer.MIN_VALUE;
        for (int i = 0; i < heights.length; i++) {
            maxHeight = Math.max(maxHeight, heights[i] + waters[i]);
        }

        for (int height = maxHeight; height > 0; height--) {
            for (int i = 0; i < n; i++) {
                if (height <= heights[i]) {
                    System.out.print("+");
                } else if (height <= heights[i] + waters[i]) {
                    System.out.print("w");
                } else {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    //follow up:
    //
    // 假设两边两个墙有个高度 分别为leftH 和rightH,
    // 则只需要在水流最后pos的地方判断是否是0 or n，然后判断当前高度是否超过
    public void pourWater(int[] heights, int water, int pos, int leftH, int rightH) {
        int[] waters = new int[heights.length];
        for (int i = 0; i < water; i++) {
            pour(heights, pos, waters, leftH, rightH);
        }
        print(heights, waters);
    }

    private void pour(int[] heights, int pos, int[] waters, int leftH, int rightH) {
        boolean leftOut = false;
        int currIdx = pos;
        while (currIdx > 0 && heights[currIdx] + waters[currIdx] >= heights[currIdx - 1] + waters[currIdx - 1]) {
            currIdx--;
        }
        //check if it leaves out
        if (currIdx == 0 && heights[0] + waters[0] + 1 > leftH) {
            leftOut = true;
        } else if (currIdx != pos) {
            //else check if it finds a position to fall
            waters[currIdx]++;
            return;
        }
        //else move right
        currIdx = pos;
        while (currIdx < heights.length - 1 && heights[currIdx] + waters[currIdx] >= heights[currIdx + 1] + waters[currIdx + 1]) {
            currIdx++;
        }
        //check if it leaves out
        if (currIdx == heights.length - 1 && heights[currIdx] + waters[currIdx] + 1 > rightH) {
            return;
        }

        //if leaves left and not drop right
        if (currIdx == pos && leftOut) {
            return;
        }
        //drop right if it eventually fall and don't leaves out left
        waters[currIdx]++;
    }

    public static void main(String[] args) {
        PourWater sol = new PourWater();
        int[] land = new int[] {5, 4, 2, 1, 3, 2, 1, 0, 1, 4};
        int[] waters = new int[land.length];
        sol.print(land, waters);
        sol.pourWater(land, 100, 1);
//        sol.pourWater(land, 10, 0);
//        sol.pourWater(land, 10, 0, 4, 3);
//
//        //test case 2
//        int[] land2 = new int[] {2, 1, 0, 3, 2, 6};
//        int[] waters2 = new int[land2.length];
//        sol.print(land2, waters2);
//        sol.pourWater(land2, 100, 5, 0, 0);
    }
}
