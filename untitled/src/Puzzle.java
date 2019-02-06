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
class Puzzle {
    int[][] board;
    int[] dirX = new int[] {1, -1, 0, 0};
    int[] dirY = new int[] {0, 0, 1, -1};
    Scanner scnr;

    //follow up
    Map<String, String> prevDir;
    Map<String, String> prevState;
    String[] direction = new String[] {"Up", "Down", "Left", "Right"};

    public Puzzle() {
        board = new int[3][3];
        randomBoard();
        scnr = new Scanner(System.in);
    }

    public void start() {
        String target = "123456780";
        int posZero = boardToStr(board).indexOf('0');
        int zx = posZero / 3;
        int zy = posZero % 3;
        System.out.println("Please enter u/U for up, l/L for left, r/R for right, and d/D for down. Q/q for quit");
        while (true) {
            printBoard();
            System.out.println("Your move: ");
            char move = scnr.nextLine().charAt(0);
            move = Character.toLowerCase(move);

            int dir = 0;
            if (move == 'u') {
                dir = 0;
            } else if (move == 'd') {
                dir = 1;
            } else if (move == 'l') {
                dir = 2;
            } else if (move == 'r') {
                dir = 3;
            } else if (move == 'q') {
                return;
            } else {
                System.out.println("Wrong input");
                continue;
            }

            //move number
            int nx = zx + dirX[dir];
            int ny = zy + dirY[dir];
            if (nx < 0 || nx >= 3 || ny < 0 || ny >= 3) {
                continue;
            }
            board[zx][zy] = board[nx][ny];
            board[nx][ny] = 0;
            zx = nx;
            zy = ny;

            if (boardToStr(board).equals(target)) {
                System.out.println("You win!");
                return;
            }
        }
    }

    private String boardToStr(int[][] board) {
        String res = "";
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                res += board[i][j];
            }
        }
        return res;
    }

    private int[][] strToBoard(String str) {
        int[][] state = new int[3][3];
        for (int i = 0; i < str.length(); i++){
            state[i / 3][i % 3] = str.charAt(i) - '0';
        }
        return state;
    }

    private void randomBoard() {
        Random rand = new Random();
        for (int i = 1; i <= 8; i++) {
            int pos = rand.nextInt(9);
            while (board[pos / 3][pos % 3] != 0) {
                pos = rand.nextInt(9);
            }
            board[pos / 3][pos % 3] = i;
        }
    }

    public void printBoard() {
        System.out.println("---------");
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                System.out.print(board[i][j] + "\t");
            }
            System.out.println();
        }
        System.out.println("---------");
    }

    //the function return true if the board could be solve
    public boolean canSolve(int[][] board) {
        String start = boardToStr(board);
        String target = "123456780";

        //follow up
        prevDir = new HashMap<>();
        prevState = new HashMap<>();
        prevState.put(start, start);


        Queue<String> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        queue.offer(start);

        while (!queue.isEmpty()) {
            String curr = queue.poll();
            visited.add(curr);
            if (target.equals(curr)) {
                //print path  follow up
                printPath(prevState, prevDir);

                return true;
            }

            int[][] state = strToBoard(curr);
            int pz = curr.indexOf('0');
            int zx = pz / 3;
            int zy = pz % 3;
            for (int i = 0; i < 4; i++) {
                int nx = zx + dirX[i];
                int ny = zy + dirY[i];
                if (nx < 0 || nx >= 3 || ny < 0 || ny >= 3) {
                    continue;
                }
                state[zx][zy] = state[nx][ny];
                state[nx][ny] = 0;

                String next = boardToStr(state);
                if (!visited.contains(next)) {
                    queue.offer(next);

                    //follow up
                    prevState.put(next, curr);
                    prevDir.put(next, direction[i]);
                }

                state[nx][ny] = state[zx][zy];
                state[zx][zy] = 0;
            }
        }

        return false;
    }

    private void printPath(Map<String, String> prevState, Map<String, String> prevDir) {
        List<String> moves = new ArrayList<>();
        String curr = "123456780";
        while (!prevState.get(curr).equals(curr)) {
            moves.add(prevDir.get(curr));
            curr = prevState.get(curr);
        }
        Collections.reverse(moves);
        System.out.println("Path is: ");
        for (String move : moves) {
            System.out.print(move + " -> ");
        }
        System.out.println("Done");

    }
}


class Driver {
    public static void main(String[] args) {

        Puzzle puzzle = new Puzzle();
        puzzle.canSolve(new int[][] {
                {1, 2, 3},
                {0, 4, 6},
                {7, 5, 8}
        });
    }
}

