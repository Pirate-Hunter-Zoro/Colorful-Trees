
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/*
* Problem Link: https://open.kattis.com/contests/rdaswb/problems/colorfultrees
*/
public class ColorfulTrees {

    static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    static int nextInt() throws IOException {
        return Integer.parseInt(reader.readLine().trim());
    }

    static int[] nextIntArray() throws IOException {
        return Arrays.stream(reader.readLine().trim().split(" ")).mapToInt(Integer::parseInt).toArray();
    }

    static class Edge {
        int from;
        int to;
        int idx;

        public Edge(int from, int to, int idx) {
            this.from = from;
            this.to = to;
            this.idx = idx;
        }
    }

    static class Node {
        int color;
        ArrayList<Edge> edges = new ArrayList<>();

        public Node(int c) {
            this.color = c;
        }
    }

    public static void main(String[] args) throws IOException {

        int n = Integer.parseInt(reader.readLine().trim());

        Node[] nodes = new Node[n];
        Map<Integer, Integer> totalCount = new HashMap<>();
        for (int i = 0; i < nodes.length; i++) {
            int color = nextInt();
            if (totalCount.containsKey(color)) {
                totalCount.put(color, totalCount.get(color) + 1);
            } else {
                totalCount.put(color, 1);
            }
            nodes[i] = new Node(color);
        }

        for (int i = 0; i < nodes.length - 1; i++) {
            int[] fromTo = nextIntArray();
            int from = fromTo[0];
            int to = fromTo[1];
            Edge edge = new Edge(from, to, i);
            nodes[from].edges.add(edge);
            nodes[to].edges.add(edge);
        }

        int[] cache = new int[n - 1];
        for (int i = 0; i < cache.length; i++) {
            cache[i] = -1;
        }

        solve(0, 0, -1, nodes, cache, totalCount);

        for (int i = 0; i < cache.length; i++) {
            System.out.println(cache[i]);
        }

    }

    static Map<Integer, Integer> solve(int child, int parent, int edgeIdx, Node[] nodes, int[] cache,
            Map<Integer, Integer> totalCount) {
        Map<Integer, Integer> union = null;
        for (Edge e : nodes[child].edges) {
            if (e.to == parent)
                continue;
            Map<Integer, Integer> tmp = solve(e.to, e.from, e.idx, nodes, cache, totalCount);
            if (union == null) {
                union = tmp;
                cache[edgeIdx] = cache[e.idx];
                continue;
            }
            if (union.size() < tmp.size()) {
                Map<Integer, Integer> swap = union;
                union = tmp;
                tmp = swap;
            }
            cache[edgeIdx] += cache[e.idx];
            for (int k : tmp.keySet()) {
                // add (k, v) to union
                // compute changes to solution
                if (union.containsKey(k)) {
                    // remove double counting
                    cache[edgeIdx] -= 2 * union.get(k) * tmp.get(k);
                    union.put(k, union.get(k) + tmp.get(k));
                } else {
                    union.put(k, tmp.get(k));
                }
            }
        }
        
        // Base case

        return union;
    }

}
