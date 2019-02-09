public class FixedSizeArray {
    private int fixedSize;
    private ListNode head;
    private ListNode tail;
    private int first;
    private int next;
    private int count;

    public FixedSizeArray(int size) {
        this.fixedSize = size;
        head = new ListNode();
        tail = head;
        first = 0;
        next = 0;
        count = 0;
    }

    public void offer(int num) {
        tail.arr[next] = num;
        count++;
        next++;
        if (next == fixedSize) {
            tail.next = new ListNode();
            tail = tail.next;
            next = 0;
        }
    }

    public Integer poll() {
        if (count == 0) {
            return null;
        }
        int num = head.arr[first];
        first++;
        if (first == fixedSize) {
            head = head.next;
            first = 0;
        }
        return num;
    }


    private class ListNode {
        int[] arr;
        ListNode next;

        public ListNode() {
            arr = new int[fixedSize];
        }
    }

    public static void main(String[] args) {
        FixedSizeArray sol = new FixedSizeArray(2);
        for (int i = 0; i < 10; i++) {
            sol.offer(i);
        }
        for (int i = 0; i < 10; i++) {
            System.out.println(sol.poll());
        }
    }
}
