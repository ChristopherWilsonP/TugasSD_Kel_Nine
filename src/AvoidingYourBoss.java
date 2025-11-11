
import java.io.*;
import java.util.*;

public class AvoidingYourBoss {

    static class Edge {
        int to, cost;
        Edge(int to, int cost) {
            this.to = to;
            this.cost = cost;
        }
    }

    static final int INF = 1000000000;
    static int P, R, BH, OF, YH, M;
    static ArrayList<Edge>[] graph;

    static int[] dijkstra(int start, boolean[] blocked) {
        int[] dist = new int[P + 1];
        Arrays.fill(dist, INF);
        dist[start] = 0;

        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[1]));
        pq.add(new int[]{start, 0});

        while (!pq.isEmpty()) {
            int[] cur = pq.poll();
            int u = cur[0];
            int d = cur[1];

            if (d > dist[u]) continue;

            for (Edge e : graph[u]) {
                int v = e.to;
                int w = e.cost;
                if (blocked[v]) continue; // skip unsafe nodes

                if (dist[v] > dist[u] + w) {
                    dist[v] = dist[u] + w;
                    pq.add(new int[]{v, dist[v]});
                }
            }
        }
        return dist;
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line;

        while ((line = br.readLine()) != null && !line.isEmpty()) {
            StringTokenizer st = new StringTokenizer(line);
            P = Integer.parseInt(st.nextToken());
            R = Integer.parseInt(st.nextToken());
            BH = Integer.parseInt(st.nextToken());
            OF = Integer.parseInt(st.nextToken());
            YH = Integer.parseInt(st.nextToken());
            M = Integer.parseInt(st.nextToken());

            graph = new ArrayList[P + 1];
            for (int i = 0; i <= P; i++) {
                graph[i] = new ArrayList<>();
            }

            for (int i = 0; i < R; i++) {
                st = new StringTokenizer(br.readLine());
                int a = Integer.parseInt(st.nextToken());
                int b = Integer.parseInt(st.nextToken());
                int c = Integer.parseInt(st.nextToken());
                graph[a].add(new Edge(b, c));
                graph[b].add(new Edge(a, c));
            }

            // Run Dijkstra for boss’s home and office
            boolean[] noneBlocked = new boolean[P + 1];
            int[] distBH = dijkstra(BH, noneBlocked);
            int[] distOF = dijkstra(OF, noneBlocked);
            int bossShortest = distBH[OF];

            // Mark unsafe nodes (on any shortest path between BH and OF)
            boolean[] unsafe = new boolean[P + 1];
            for (int i = 1; i <= P; i++) {
                if (distBH[i] != INF && distOF[i] != INF &&
                        distBH[i] + distOF[i] == bossShortest) {
                    unsafe[i] = true;
                }
            }

            // Your home is always safe to start from
            unsafe[YH] = false;

            // Now run Dijkstra from your home to market, avoiding unsafe nodes
            int[] distYou = dijkstra(YH, unsafe);

            if (YH == M || distYou[M] == 0) {
                System.out.println("MISSION IMPOSSIBLE.");
            } else if (distYou[M] == INF) {
                System.out.println("MISSION IMPOSSIBLE.");
            } else {
                System.out.println(distYou[M]);
            }
        }
    }
}