import java.io.*;
import java.util.*;

public class AvoidingYourBoss {



    static class Edge {
        int to, cost; //destinasi dan biaya perjalanan
        Edge(int to, int cost) {
            this.to = to;
            this.cost = cost;
        }
    }




    static final int INF = 1000000000; //untuk representasi inf
    static ArrayList<Edge>[] graph; //graf representasi adjacency list
    static int numPlaces; //jumlah tempat






    public static void main(String[] args) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line;
        //Input
        while ((line = reader.readLine()) != null && !line.isEmpty()) {
            int[] params = parseInput(line, reader);
            int bossHome = params[0]; //BH
            int office = params[1]; //OF
            int yourHome = params[2]; //YH
            int market = params[3]; //M

            int safeCost = findSafePath(bossHome, office, yourHome, market);

            //Output
            if (safeCost == INF) {
                System.out.println("MISSION IMPOSSIBLE.");
            } else {
                System.out.println(safeCost);
            }
        }
    }



    static int[] parseInput(String firstLine, BufferedReader reader) throws IOException {
        StringTokenizer st = new StringTokenizer(firstLine);
        numPlaces = Integer.parseInt(st.nextToken());
        int numRoads = Integer.parseInt(st.nextToken());
        int bossHome = Integer.parseInt(st.nextToken());
        int office = Integer.parseInt(st.nextToken());
        int yourHome = Integer.parseInt(st.nextToken());
        int market = Integer.parseInt(st.nextToken());

        buildGraph(numRoads, reader); //membangun graf

        return new int[]{bossHome, office, yourHome, market};
    }




    static void buildGraph(int numRoads, BufferedReader reader) throws IOException {
        graph = new ArrayList[numPlaces + 1];
        for (int i = 0; i <= numPlaces; i++) {
            graph[i] = new ArrayList<>();
        }

        for (int i = 0; i < numRoads; i++) {
            StringTokenizer st = new StringTokenizer(reader.readLine());
            int place1 = Integer.parseInt(st.nextToken());
            int place2 = Integer.parseInt(st.nextToken());
            int cost = Integer.parseInt(st.nextToken());

            graph[place1].add(new Edge(place2, cost));
            graph[place2].add(new Edge(place1, cost));
        }
    }

    static int findSafePath(int bossHome, int office, int yourHome, int market) {
        if (yourHome == market) {
            return INF;
        }

        //Dijkstra dari BH dan OF
        int[] distFromBossHome = dijkstra(bossHome, new boolean[numPlaces + 1]);
        int[] distFromOffice = dijkstra(office, new boolean[numPlaces + 1]);

        //Menandai tempat-tempat yang tidak aman(ada di jalur terpendek BH ke OF)
        boolean[] unsafePlaces = markUnsafePlaces(distFromBossHome, distFromOffice, yourHome);

        //Dijkstra dari YH menghindari tempat-tempat tidak aman
        int[] distFromYourHome = dijkstra(yourHome, unsafePlaces);
        return distFromYourHome[market];
    }

    static boolean[] markUnsafePlaces(int[] distFromBossHome, int[] distFromOffice, int yourHome) {
        //Menghitung jarak terpendek dari BH ke OF
        int bossShortestPath = distFromBossHome[distFromOffice.length - 1];
        //Menandai tempat-tempat yang ada di jalur terpendek
        boolean[] unsafe = new boolean[numPlaces + 1];

        for (int place = 1; place <= numPlaces; place++) {
            //Cek apakah tempat tersebut ada di jalur terpendek
            boolean onShortestPath = distFromBossHome[place] != INF
                    && distFromOffice[place] != INF //ada di jalur
                    // cek path BH -> place -> OF jarak totalnya sesuai dengan jarak terpendek
                    && distFromBossHome[place] + distFromOffice[place] == bossShortestPath;
            unsafe[place] = onShortestPath; // tandai sebagai tidak aman jika ada di jalur terpendek
        }

        unsafe[yourHome] = false; // YH selalu aman
        return unsafe;
    }

    static int[] dijkstra(int start, boolean[] blockedPlaces) {
        int[] distance = new int[numPlaces + 1];
        Arrays.fill(distance, INF);
        distance[start] = 0;

        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[1]));
        pq.add(new int[]{start, 0});

        while (!pq.isEmpty()) {
            int[] current = pq.poll();
            int place = current[0];
            int dist = current[1];

            if (dist > distance[place]) continue; //Sudah ada jarak yang lebih pendek

            for (Edge edge : graph[place]) {
                //Lewati tempat yang diblokir
                if (blockedPlaces[edge.to]) continue;

                int newDist = distance[place] + edge.cost;
                if (newDist < distance[edge.to]) {
                    distance[edge.to] = newDist;
                    pq.add(new int[]{edge.to, newDist});
                }
            }
        }

        return distance;
    }
}