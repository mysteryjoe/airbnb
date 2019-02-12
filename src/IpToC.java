import java.util.ArrayList;
import java.util.List;

public class IpToC {
    public static void main(String[]args) {
        IpToC sol = new IpToC();
        List<String> res = sol.ipRange2Cidr("0.0.0.0", 256);
        printResult(res);
    }

    public List<String> ipRange2Cidr(String startIp, int range) {
        List<String> ans = new ArrayList<>();
        long startIpLong = ipToLong(startIp);
        while (range > 0) {
            //the maximum range we could cover based on startIp
            //for example , 0000 0000 0010 0100 -> 0000 0000 0000 0100
            // maxRange = 2 ^ (index of lowest bit 1)
            long maxRange = startIpLong & (-startIpLong);
            if (startIpLong == 0) {
                //special case, 0 means we could cover any range in
                //32 bit
                maxRange = 1l << 32;
            }
            //adjust to represent many ips as possible
            while (maxRange > range) {
                maxRange >>= 1;
            }

            ans.add(longToIp(startIpLong) + "/" + (32 - getHighestBit(maxRange)));
            startIpLong += maxRange;
            range -= maxRange;
        }
        return ans;
    }

    private static int getHighestBit(long num) {
        int high = -1;
        while (num > 0) {
            high++;
            num = num >> 1;
        }
        return high;
    }
    private static void printResult(List<String> res) {
        for (int i = 0; i < res.size(); i++) {
            System.out.println(res.get(i));
        }
    }
    private long ipToLong(String ip) {
        String[] parts = ip.split("\\.");
        long res = 0;
        for (int i = 0; i < parts.length; i++) {
            res = res << 8 | Integer.parseInt(parts[i]);
        }
        return res;
    }

    private String longToIp(long ipLong) {
        String[] nums = new String[4];
        for (int i = 3; i >= 0; i--) {
            nums[i] = "" + (ipLong & 255);
            ipLong = ipLong >> 8;
        }

        return String.join(".", nums);
    }
}
