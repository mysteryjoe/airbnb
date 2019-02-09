import java.util.*;

/**
 *
 *     I suggest to use trie for this problem.
 *     Assumption:
 *     1. for root "/" we could not create it, but we could set it and get it
 *     default value of root is 0
 *     2. we should handle invalid path if not start with "/"
 *
 */
public class FileSystem {
    Trie trie;

    public FileSystem() {
        trie = new Trie();
    }

    private boolean isValid(String path) {
        return path != null && path.length() != 0 && path.charAt(0) == '/';
    }

    public boolean create(String path, int value) {
        //step 1, check valid path to create
        if (!isValid(path) || path.equals("/")) {
            return false;
        }
        //step 2, substring the new path to create
        int lastIndex = path.lastIndexOf('/');
        String newPath = path.substring(lastIndex + 1);
        TrieNode node = trie.traverse(path.substring(0, lastIndex));
        //1. no such dir during the traverse   2. path exists,
        if (node == null  || node.subPath.containsKey(newPath)) {
            return false;
        }

        node.subPath.put(newPath, new TrieNode(value));
        //follow up
        trie.runCallBacks(path);
        return true;
    }

    public boolean set(String path, int value) {
        if (!isValid(path)) {
            return false;
        }

        TrieNode node = trie.traverse(path);
        if (node == null) {
            return false;
        }
        node.value = value;
        trie.runCallBacks(path);
        return true;
    }

    public Integer get(String path) {
        if (!isValid(path)) {
            return null;
        }

        TrieNode node = trie.traverse(path);
        if (node == null) {
            return null;
        }
        return node.value;
    }

    //follow up
    public boolean watch(String path, Runnable callBack) {
        if (!isValid(path)) {
            return false;
        }

        TrieNode node = trie.traverse(path);
        if (node == null) {
            return false;
        }

        node.callBack = callBack;
        return true;
    }

    private class Trie {
        TrieNode root;

        public Trie() {
            root = new TrieNode(0);
        }
        public TrieNode traverse(String path) {
            String[] paths = path.split("/");
            TrieNode curr = root;
            for (int i = 1; i < paths.length; i++) {
                if (!curr.subPath.containsKey(paths[i])) {
                    return null;
                }
                curr = curr.subPath.get(paths[i]);
            }
            return curr;
        }

        //follow up helper function
        private void runCallBacks(String path) {
            //if the order is from last to first, use a stack
            Stack<Runnable> callBacks = new Stack<>();
            if (root.callBack != null) {
                callBacks.push(root.callBack);
            }

            String[] paths = path.split("/");
            TrieNode curr = root;

            for (int i = 1; i < paths.length; i++) {
                curr = curr.subPath.get(paths[i]);
                if (curr.callBack != null) {
                    callBacks.push(curr.callBack);
                }
            }

            while (!callBacks.isEmpty()) {
                callBacks.pop().run();
            }
        }
    }
    private class TrieNode {
        Map<String, TrieNode> subPath;
        int value;
        //follow up
        Runnable callBack = null;

        public TrieNode(int value) {
            this.value = value;
            subPath = new HashMap<>();
        }
    }

    public static void main(String[] args) {
        FileSystem fileSystem = new FileSystem();
        System.out.println("Creating path /a:1 " + fileSystem.create("/a", 1));
        //error case 1 : create path that existed
        System.out.println("Creating path /a:1 " + fileSystem.create("/a", 2));
        //test case for GET method
        System.out.println("Get path /a -> " + fileSystem.get("/a"));
        System.out.println("Creating path /a/b:2 " + fileSystem.create("/a/b", 2));
        System.out.println("Get path /a/b ->  " + fileSystem.get("/a/b"));
        //test case for SET method
        System.out.println("Set path /a/b:3 " + fileSystem.set("/a/b", 3));
        System.out.println("Get path /a/b ->" + fileSystem.get("/a/b"));

        //error case 2 : create root
        System.out.println("Creating path / " + fileSystem.create("/", 5));
        //test case for set root
        System.out.println("Set path /:5 " + fileSystem.set("/", 5));
        System.out.println("Get path / -> " + fileSystem.get("/"));

        //callback method
        System.out.println("Create callback for /a : B callback is triggered. " + fileSystem.watch("/a", new Runnable() {
            @Override
            public void run() {
                System.out.println("B callback is triggered.");
            }
        }));

        System.out.println("Create callback for / : A callback is triggered. " + fileSystem.watch("/", new Runnable() {
            @Override
            public void run() {
                System.out.println("A callback is triggered.");
            }
        }));

        System.out.println("Create callback for /a/b : C callback is triggered. " + fileSystem.watch("/a/b", new Runnable() {
            @Override
            public void run() {
                System.out.println("C callback is triggered.");
            }
        }));

        System.out.println("Set path /a/b:3 " + fileSystem.set("/a/b", 3));


    }
}
