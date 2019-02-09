/**
 *  given a large file of integers, find median.
 *
 *  Assume We could not load all the integers into our memory.
 *
 *  Using binary search to guess median number
 *
 *  Since bound is [INT_MIN, INT_MAX] = -2^32 to 2^32 - 1
 *  Every guess we scan file once.
 *
 */


public class FindMedInFile {

    public double findMedian(LargeFile file) {
        int size = getFileSize(file);
        int kthSmall = findKthSmallest(file, size / 2);
        if (size % 2 == 0) {
            return (double) (kthSmall + findKthSmallest(file, size / 2 + 1)) / 2.0;
        } else {
            return (double) kthSmall;
        }
    }

    //return kth smallest number,
    private int findKthSmallest(LargeFile file, int k) {
        long lower = Integer.MIN_VALUE;
        long upper = Integer.MAX_VALUE;
        int res = Integer.MIN_VALUE;
        while (lower <= upper) {
            long mid = lower + (upper - lower) / 2;
            //count how many nums smaller than or equal to mid in file
            int count = 0;
            int kthNum = Integer.MIN_VALUE;
            while (file.hasNext()) {
                int next = file.next();
                if (next <= mid) {
                    kthNum = Math.max(kthNum, next);
                    count++;
                }
            }
            file.reset();
            if (count >= k) {
                //store potential ans
                res = kthNum;
                upper = mid - 1;
            } else {
                lower = mid + 1;
            }
        }
        return res;
    }

    private int getFileSize(LargeFile file) {
        int count = 0;
        while (file.hasNext()) {
            count++;
            file.next();
        }
        file.reset();
        return count;
    }

    public static void main(String[] args) {
        FindMedInFile findMedInFile = new FindMedInFile();

        int[] test1 = new int[] {3, 1, 2, 2, 2, 3};
        LargeFile largeFile = new LargeFile(test1);

        System.out.println("The median of the file is " + findMedInFile.findMedian(largeFile));
    }
}
class LargeFile {
    private int[] nums;
    private int index;
    public LargeFile(int[] nums) {
        this.nums = nums;
        index = 0;
    }

    public Integer next() {
        if (hasNext()) {
            return nums[index++];
        }
        return null;
    }
    public boolean hasNext() {
        return index < nums.length;
    }

    public void reset() {
        index = 0;
    }
}
