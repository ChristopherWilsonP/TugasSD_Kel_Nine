import java.util.*;

public class HippityHopscotch {
    static int n, k;
    static int[][] grid;
    static long[][] memo; // Stores results of subproblems to avoid re-calculation

    // Direction vectors: Up, Down, Left, Right
    static int[] dr = {-1, 1, 0, 0};
    static int[] dc = {0, 0, -1, 1};

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Check if there is input
        if (sc.hasNextInt()) {
            int t = sc.nextInt(); // Number of test cases

            while (t-- > 0) {
                n = sc.nextInt(); // Grid size
                k = sc.nextInt(); // Max jump distance

                grid = new int[n][n];
                memo = new long[n][n];

                // Read grid and initialize memo table
                for(int i = 0; i < n; i++) {
                    for(int j = 0; j < n; j++) {
                        grid[i][j] = sc.nextInt();
                        memo[i][j] = -1; // -1 represents "unvisited" or "not calculated yet"
                    }
                }

                // Start DFS from (0,0)
                System.out.println(dfs(0, 0));

                // Print a blank line between outputs of consecutive cases
                if (t > 0) {
                    System.out.println();
                }
            }
        }
        sc.close();
    }

    // Recursive function with Memoization (DFS)
    static long dfs(int r, int c) {
        // If we already calculated the max pennies from this spot, return it
        if (memo[r][c] != -1) return memo[r][c];

        long maxFromNextStep = 0;

        // Try moving in all 4 directions
        for (int i = 0; i < 4; i++) {
            // Try jumping distances from 1 to k
            for (int dist = 1; dist <= k; dist++) {
                int nr = r + (dr[i] * dist);
                int nc = c + (dc[i] * dist);

                // 1. Check Bounds
                if (nr >= 0 && nr < n && nc >= 0 && nc < n) {
                    // 2. Check Rule: Destination must have MORE pennies
                    if (grid[nr][nc] > grid[r][c]) {
                        // Recursively find max path from that neighbor
                        long pathValue = dfs(nr, nc);
                        if (pathValue > maxFromNextStep) {
                            maxFromNextStep = pathValue;
                        }
                    }
                } else {
                    // If we go out of bounds in this direction, jumping further is also out of bounds
                    break;
                }
            }
        }

        // Current cell value + the best path we found from here
        return memo[r][c] = grid[r][c] + maxFromNextStep;
    }
}