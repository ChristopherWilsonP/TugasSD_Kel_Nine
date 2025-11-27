import java.util.*;

public class HippityHopscotch {
    static int n, k;
    static int[][] grid;
    static long[][] memo; // menyimpan result subproblems untuk menghindari kalkulasi ulang

    // Direction vectors: atas, bawah, kiri, kanan
    static int[] dr = {-1, 1, 0, 0};
    static int[] dc = {0, 0, -1, 1};

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        //input
        if (sc.hasNextInt()) {
            int t = sc.nextInt(); //banyak case yang mau dikerjakan

            while (t-- > 0) {
                n = sc.nextInt(); //Grid size
                k = sc.nextInt(); //Max jump distance

                grid = new int[n][n];
                memo = new long[n][n];

                //membaca grid dan inisialisasi memoization table
                for(int i = 0; i < n; i++) {
                    for(int j = 0; j < n; j++) {
                        grid[i][j] = sc.nextInt();
                        memo[i][j] = -1; //-1 artinya belum dihitung
                    }
                }
                //Start DFS dari (0,0)
                System.out.println(dfs(0, 0));

                if (t > 0) {
                    System.out.println();
                }
            }
        }
        sc.close();
    }

    //Rekursi dfs dengan memoization
    static long dfs(int r, int c) {
        //kalau kita sudah menghitung nilai untuk (r,c), kembalikan nilainya memo
        if (memo[r][c] != -1) return memo[r][c];

        long maxFromNextStep = 0; //menyimpan nilai maksimum dari langkah selanjutnya

        //coba keempat arah
        for (int i = 0; i < 4; i++) {
            //coba melompat dari 1 sampai k langkah
            for (int dist = 1; dist <= k; dist++) {
                int nr = r + (dr[i] * dist);
                int nc = c + (dc[i] * dist);

                //cek batas grid
                if (nr >= 0 && nr < n && nc >= 0 && nc < n) {
                    //check destinasi lebih besar dari yang sekarang
                    if (grid[nr][nc] > grid[r][c]) {
                        long pathValue = dfs(nr, nc);
                        //update max jika pathValue lebih besar dari maxFromNextStep
                        if (pathValue > maxFromNextStep) {
                            maxFromNextStep = pathValue;
                        }
                    }
                } else {
                    //keluar dari grid, tidak perlu cek jarak lebih jauh
                    break;
                }
            }
        }
        //return calue cell sekarang + path terbaik yang ditemukan dari sini
        return memo[r][c] = grid[r][c] + maxFromNextStep;
    }
}