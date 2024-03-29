import java.util.*;

public class NestedList {
    Iterator<NestedInteger> currIter;
    Integer currInteger = null;
    Stack<Iterator<NestedInteger>> stack;
    public NestedList(List<NestedInteger> nestedList) {
        stack = new Stack<>();
        currIter = nestedList.iterator();
    }

    public Integer next() {
        if (hasNext()) {
            int res = currInteger;
            currInteger = null;
            return res;
        }
        return null;
    }

    public boolean hasNext() {
        if (currInteger != null) {
            return true;
        }
        while (currInteger == null) {
            while (!stack.isEmpty() && !currIter.hasNext()) {
                currIter = stack.pop();
            }
            if (stack.isEmpty() && !currIter.hasNext()) {
                return false;
            }
            NestedInteger next = currIter.next();
            if (next.isInteger()) {
                currInteger = next.getInteger();
            } else {
                stack.push(currIter);
                currIter = next.getList().iterator();
            }
        }

        return true;
    }

    public void remove() {
        if (currIter != null) {
            currIter.remove();
        }
    }
}

class NestedInteger {
    List<NestedInteger> list;
    Integer value;

    public NestedInteger(int val) {
        this.value = val;
    }

    public NestedInteger(List<NestedInteger> list) {
        this.list = list;
    }

    public boolean isInteger() {
        return value != null;
    }

    public Integer getInteger() {
        return value;
    }

    public List<NestedInteger> getList() {
        return list;
    }
}
