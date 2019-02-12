import java.util.*;

public class SlidePuzzle {
    private int[][] board;
    private String target = "123456780";
    private int[] dx = new int[] {1, -1, 0, 0};
    private int[] dy = new int[] {0, 0, 1, -1};

    public SlidePuzzle() {
        board = generate();
    }

    public int[][] generate() {
        Random rand = new Random();
        int[][] board = new int[3][3];
        for (int i = 1; i <= 8; i++) {
            int pos = rand.nextInt(9);
            while (board[pos / 3][pos % 3] != 0) {
                pos = rand.nextInt(9);
            }
            board[pos / 3][pos % 3] = i;
        }
        return board;
    }

    public void printBoard(int[][] board) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.print(board[i][j] + "  ");
            }
            System.out.println();
        }
        System.out.println();
    }

    private void move(int[][] board, int zeroPos, int dir) {
        int zx = zeroPos / 3;
        int zy = zeroPos % 3;
        int nx = zx + dx[dir];
        int ny = zy + dy[dir];

        if (checkBound(board, nx, ny)) {
            board[zx][zy] = board[nx][ny];
            board[nx][ny] = 0;
        }
    }

    private boolean checkBound(int[][] board, int x, int y) {
        int n = board.length;
        int m = board[0].length;
        if (x < 0 || x >= n || y < 0 || y >= m) {
            return false;
        }
        return true;
    }

    private String boardToStr(int[][] board) {
        String res = "";
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                res += board[i][j];
            }
        }
        return res;
    }

    public void start() {
        String curr = boardToStr(board);
        Scanner scnr = new Scanner(System.in);
        //game start
        while(true) {
            if (curr.equals(target)) {
                System.out.println("You win!");
                return;
            }
            printBoard(this.board);
            System.out.println("Please Enter you next move: (q for quit, u for up, d for down, l for left and r for right) ");
            String input = scnr.nextLine();
            while (input.length() != 1) {
                System.out.println("Wrong input!");
                System.out.println("Please Enter you next move: (q for quit, u for up, d for down, l for left and r for right) ");
                input = scnr.nextLine();
            }
            char in = input.charAt(0);

            int posZero = curr.indexOf('0');
            if (in == 'q') {
                System.out.println("Goodbye!");
                return;
            } else if (in == 'u') {
                move(board, posZero, 0);
            } else if (in == 'd') {
                move(board, posZero, 1);
            } else if (in == 'l') {
                move(board, posZero, 2);
            } else if (in == 'r') {
                move(board, posZero, 3);
            } else {
                System.out.println("Wrong input!");
                continue;
            }

            curr = boardToStr(board);
        }
    }

    public boolean canSolve(int[][] board) {
        Map<String, String> prev = new HashMap<>();
        String start = boardToStr(board);
        Queue<String> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();

        prev.put(start, start);
        queue.offer(start);
        while (!queue.isEmpty()) {
            String currState = queue.poll();
            visited.add(currState);
            if (currState.equals(this.target)) {
                printPath(prev);
                return true;
            }

            //go next states
            int[][] state = strToBoard(currState);
            int posZero = currState.indexOf('0');
            int zx = posZero / 3;
            int zy = posZero % 3;

            for (int k = 0; k < 4; k++) {
                int nx = zx + dx[k];
                int ny = zy + dy[k];

                if (!checkBound(state, nx, ny)) {
                    continue;
                }
                //move
                state[zx][zy] = state[nx][ny];
                state[nx][ny] = 0;

                String nextState = boardToStr(state);
                if (!visited.contains(nextState)) {
                    prev.put(nextState, currState);
                    queue.offer(nextState);
                }

                //restore 0
                state[nx][ny] = state[zx][zy];
                state[zx][zy] = 0;
            }
        }

        return false;
    }

    private void printPath(Map<String, String> prev) {
        List<String> path = new ArrayList<>();
        String temp = this.target;
        while (!prev.get(temp).equals(temp)) {
            path.add(temp);
            temp = prev.get(temp);
        }
        path.add(temp);
        Collections.reverse(path);
        for (String s : path) {
            printBoard(strToBoard(s));
            System.out.println();
        }
        System.out.println("Done");
    }

    private int[][] strToBoard(String state) {
        int[][] board = new int[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = state.charAt(i * 3 + j) - '0';
            }
        }
        return board;
    }
    public static void main(String[] args) {
        SlidePuzzle puzzle = new SlidePuzzle();
        System.out.println(puzzle.canSolve(puzzle.generate()));
    }
}
