import java.util.*;
/**
 *  I suggest use hashmap to store paths we created
 *
 *  Assume path starts from "/", and file name is valid
 *  Could we get or set root ?
 */
public class FileSystemII {
    private Map<String, Integer> paths;

    //follow up
    private Map<String, Runnable> callBacks;

    public FileSystemII() {
        paths = new HashMap<>();
        callBacks = new HashMap<>();
        //our root
        paths.put("/", 0);
    }
    private boolean isValid(String path) {
        return path != null && path.length() != 0 && path.charAt(0) == '/';
    }
    public boolean create(String path, int value) throws Exception {
        if (!isValid(path) || paths.containsKey(path)) {
            throw new Exception();
        }
        int lastSlashIdx = path.lastIndexOf('/');
        String prev = path.substring(0, lastSlashIdx);
        //special check for files to be created under root
        if (prev.length() == 0) {
            prev = "/";
        }
        //check for directory
        if (!paths.containsKey(prev)) {
            return false;
        }
        paths.put(path, value);

        //follow up
        triggerCallBacks(path);

        return true;
    }

    public boolean set(String path, int value) throws Exception {
        if (paths.containsKey(path)) {
            paths.put(path, value);

            //follow up
            triggerCallBacks(path);
            return true;
        }
        throw new Exception();
    }

    public Integer get(String path) {
        return paths.get(path);
    }

    //follow up
    //suppose the callback method will be called from deep
    private void triggerCallBacks(String path) {
        while (path.length() > 0) {
            if (callBacks.containsKey(path)) {
                callBacks.get(path).run();
            }
            path = path.substring(0, path.lastIndexOf('/'));
        }
        //if root has callback
        if (callBacks.containsKey("/")) {
            callBacks.get("/").run();
        }
    }

    public boolean watch(String path, String message) {
        if (!paths.containsKey(path)) {
            return false;
        }
        callBacks.put(path, () -> {
            System.out.println(message);
        });
        return true;
    }


    public static void main(String[] args) throws Exception {
        FileSystemII fileSystem = new FileSystemII();
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
        System.out.println("Create callback for /a : B callback is triggered. " + fileSystem.watch("/a", "B callback is triggered."));

        System.out.println("Create callback for / : A callback is triggered. " + fileSystem.watch("/", "A callback is triggered."));

        System.out.println("Create callback for /a/b : C callback is triggered. " + fileSystem.watch("/a/b", "C callback is triggered."));

        System.out.println("Set path /a/b:3 " + fileSystem.set("/a/b", 3));
    }
}
