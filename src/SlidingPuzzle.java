import java.util.*;

/**
 *        give a board contains 0 - 8
 *
 *        return true if it could move as [1 2 3] [4 5 6] [7 8 0]
 *
 *        I suggest use bfs to solve the problem. WHY NOT DFS (some state might be deep to dfs,
 *        so it will cause stackOverflow.
 *
 *        First, we need to store the state from board to a String,
 *        let's implement two helper function:
 *            boardToStr()
 *            strToBoard()
 *        as serialize and deserialize
 *
 *        Second, we need a set to keep track of visited state
 *
 *        Then, we start from initial board state, and transit
 *        to next possible states by moving elements around elem '0'.
 *
 *
 *          //follow up
 *          can you print path????
 *          When doing state transition, we use two maps to keep track of
 *          previous state and the direction from prev to current state!!
 *
 */
public class SlidingPuzzle {
    private int row;
    private int col;
    //direction array
    private int[] dx = new int[] {1, -1, 0, 0};
    private int[] dy = new int[] {0, 0, 1, -1};

    //follow up
    private Map<String, String> prevState;
    //the directions correspond to direction array, where
    //{1, 0} makes UP from prev to current (move elem lower than '0' to '0' is UP)
    String[] directions = new String[] {"UP", "DOWN", "LEFT", "RIGHT"};
    private Map<String, String> prevDir;


    public SlidingPuzzle(int row, int col) {
        this.row = row;
        this.col = col;

        //follow up
        prevDir = new HashMap<>();
        prevState = new HashMap<>();
    }


    //random generate a board
    public int[][] generateBoard() {
        int[][] board = new int[row][col];

        //follow up
        prevDir = new HashMap<>();
        prevState = new HashMap<>();

        Random rand = new Random();
        for (int i = 1; i < row * col; i++) {
            int pos = rand.nextInt(row * col);
            while (board[pos / col][pos % col] != 0) {
                pos = rand.nextInt(row * col);
            }
            board[pos / col][pos % col] = i;
        }
        printBoard(board);
        return board;
    }

    //print such board
    public void printBoard(int[][] board) {
        System.out.println("Printing current board:");
        for (int i = 0; i < row; i++) {
            System.out.print("[\t");
            for (int j = 0; j < col; j++) {
                System.out.print(board[i][j] + "\t");
            }
            System.out.println("]");
        }
        System.out.println("----------------");
    }

    public boolean canSolve(int[][] board) {
        if (board.length == 0) {
            return false;
        }
        if (board[0].length == 0) {
            return false;
        }

        String target = "";
        for (int i = 1; i < row * col; i++) {
            target += i + ",";
        }
        target += 0;

        String start = boardToStr(board);

        //initialize queue and set
        Set<String> visited = new HashSet<>();
        Queue<String> queue = new LinkedList<>();
        queue.add(start);

        //follow up
        prevState.put(start, start);

        //bfs
        while (!queue.isEmpty()) {
            String curr = queue.poll();
            visited.add(curr);
            //check return condition
            if (target.equals(curr)) {
                return true;
            }

            //step 1 find zero's position
            int[][] state = strToBoard(curr);
            //if x and y are 3, we could use zx = curr.indexOf('0') / 3
            int zx = 0;
            int zy = 0;
            search:
                for (int i = 0; i < row; i++) {
                    for (int j = 0; j < col; j++) {
                        if (state[i][j] == 0) {
                            zx = i;
                            zy = j;
                            break search;
                        }
                    }
                }


            //step 2 get next states by moving elements near '0'
            for (int k = 0; k < 4; k++) {
                int nx = zx + dx[k];
                int ny = zy + dy[k];

                if (nx < 0 || nx >= row || ny < 0 || ny >= col) {
                    continue;
                }

                state[zx][zy] = state[nx][ny];
                state[nx][ny] = 0;
                String next = boardToStr(state);
                if (!visited.contains(next)) {
                    queue.add(next);

                    //follow up
                    prevState.put(next, curr);
                    prevDir.put(next, directions[k]);
                }
                state[nx][ny] = state[zx][zy];
                state[zx][zy] = 0;
            }
        }

        return false;
    }

    /**
     *   Serialize and Deserialize board state
     */
    private String boardToStr(int[][] board) {
        String str = "";
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                str += board[i][j] + ",";
            }
        }
        return str.substring(0, str.length() - 1);
    }
    private int[][] strToBoard(String state) {
        String[] nums = state.split(",");
        int[][] board = new int[row][col];
        for (int i = 0; i < nums.length; i++) {
            board[i / col][i % col] = Integer.parseInt(nums[i]);
        }
        return board;
    }

    /////////////////////////////////////////////////////////

    //follow up
    public List<String> getPath(String target) {
        List<String> res = new ArrayList<>();
        String curr = target;
        while (prevState.get(curr) != curr) {
            res.add(prevDir.get(curr));
            curr = prevState.get(curr);
        }
        Collections.reverse(res);
        return res;
    }
    public static void main(String[] args) {
        SlidingPuzzle puzzle = new SlidingPuzzle(3, 3);
        int[][] board = puzzle.generateBoard();
        System.out.println("Can solve it ? :" + puzzle.canSolve(board));
        List<String> path2 = puzzle.getPath("1,2,3,4,5,6,7,8,0");
        System.out.println("Path is : ");
        for (String s : path2) {
            System.out.print(s + " -> ");
        }
        System.out.println("DONE");
        int[][] test = new int[][] {
                {1, 2, 3},
                {0, 4, 6},
                {7, 5, 8}
        };

        System.out.println("Can solve it ? :" + puzzle.canSolve(test));
        List<String> path = puzzle.getPath("1,2,3,4,5,6,7,8,0");
        System.out.println("Path is : ");
        for (String s : path) {
            System.out.print(s + " -> ");
        }
        System.out.println("DONE");

    }
}
