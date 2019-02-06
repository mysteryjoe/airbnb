import java.util.*;


/**
 * 设计一个银行帐户系统，实现：
 * 存钱（帐户id，存钱数目，日期）
 * 取钱（帐户id，存钱数目，日期）
 * 查账（帐户id，起始日期，结束日期）： 只需要返回两个数值，一个是起始日期的balance，一个是结束日期的balance。
 * 描述就是这么多，剩下的自己发挥。钱的类型用integer，日期什么的自定义，我直接拿了integer
 */
class BankSystem {
    Map<Integer, Integer> accBalance;    // id -> balance
    Map<Integer, TreeMap<Long, Integer>> accStatement; // id -> timestamp -> balance

    public BankSystem() {
        this.accBalance = new HashMap<>();
        this.accStatement = new TreeMap<>();
    }

    public void deposite(int id, int amount, long timestamp) {
        //create new account if it doesn't exist
        if (!accBalance.containsKey(id)) {
            accBalance.put(id, 0);
            accStatement.put(id, new TreeMap<>());
        }
        accBalance.put(id, accBalance.get(id) + amount);
        accStatement.get(id).put(timestamp, accBalance.get(id));
    }

    public boolean withdraw(int id, int amount, long timestamp) {
        if (!accBalance.containsKey(id) || accBalance.get(id) < amount) {
            return false;
        }

        accBalance.put(id, accBalance.get(id) - amount);
        accStatement.get(id).put(timestamp, accBalance.get(id));
        return true;
    }

    // [startTime, endTime]
    //assume if startTime is before first deposit, we will consider the balance as 0
    public int[] check(int id, long startTime, long endTime) {
        if (startTime > endTime || !accBalance.containsKey(id)) {
            return new int[0];
        }

        int[] res = new int[2];
        TreeMap<Long, Integer> statements = accStatement.get(id);
        //find first timestamp such that  startTime < time < endTime
        Long firstTime = statements.floorKey(startTime);
        if (firstTime == null) {
            res[0] = 0;
        } else {
            res[0] = statements.get(firstTime);
        }

        Long lastTime = statements.floorKey(endTime);
        if (lastTime == null) {
            return new int[0];
        }
        res[1] = statements.get(lastTime);
        return res;
    }

    public static void main(String args[]) {
        BankSystem bs = new BankSystem();
        System.out.println(bs.withdraw(0, 100, 0));  // false
        bs.deposite(0, 100, 1);
        bs.deposite(1, 250, 2);
        bs.withdraw(0, 30, 3);
        System.out.println(bs.check(0, 0, 2)[0]);  // 0
        System.out.println(bs.check(0, 0, 2)[1]);  // 100
        bs.deposite(1, 5, 7);
        System.out.println(bs.check(1, 3, 9)[0]);  // 250
        System.out.println(bs.check(1, 3, 9)[1]);  // 255
    }
}
