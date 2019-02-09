/**
 *    arrays of string represents diff types of blocks and how many crowns in each block
 *
 *    score = connected num of block of same type * how many crowns total
 *
 *    find total score
 */
class KingDomino {
    private int[] dirX = {0, 0, -1, 1};
    private int[] dirY = {1, -1, 0, 0};

    public static void main(String[] args) {
        String[] input = {"S0 W1 W1 W0 L2",
                "W0 W0 T0 T0 T0",
                "W0 W1 T0 M2 M1",
                "S0 L0 S1 S0 S0",
                "M0 R2 R0 S1 T0"};
        KingDomino sol = new KingDomino();
        System.out.println(sol.getScore(input));
    }

    public int getScore(String[] input) {
        if (input == null || input.length == 0) {
            return 0;
        }
        // parse string and create borad
        int row = input.length;
        int col = input[0].split(" ").length;
        String[][] grid = new String[row][col];
        boolean[][] visited = new boolean[row][col];
        for (int i = 0; i < row; i++) {
            String[] temp = input[i].split(" ");
            for (int j = 0; j < col; j++) {
                grid[i][j] = temp[j];
            }
        }
        // for each entry, run DFS
        int totalScore = 0;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (!visited[i][j]) {
                    int[] point = new int[]{0};
                    int[] area = new int[]{0};
                    dfs(grid, visited, i, j, grid[i][j].charAt(0), area, point);
                    totalScore += point[0] * area[0];
                }
            }
        }
        return totalScore;
    }

    private boolean inBound(int i, int j, int row, int col) {
        return i >= 0 && i < row && j >= 0 && j < col;
    }

    private void dfs(String[][] grid, boolean[][] visited, int i, int j, char type, int[] area, int[] point) {
        if (!inBound(i, j, grid.length, grid[0].length) || visited[i][j] || grid[i][j].charAt(0) != type) {
            return;
        }
        visited[i][j] = true;
        point[0] += grid[i][j].charAt(1) - '0';
        area[0]++;
        for (int k = 0; k < 4; k++) {
            dfs(grid, visited, i + dirX[k], j + dirY[k], type, area, point);
        }
    }
}