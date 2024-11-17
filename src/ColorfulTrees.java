
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
        HashMap<Integer, Integer> colorCounts = new HashMap<>();

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

        solve(0, -1, nodes, cache, totalCount);

        for (int i = 0; i < cache.length; i++) {
            System.out.println(cache[i]);
        }

    }

    static void solve(int current, int parent, Node[] nodes, int[] cache,
            Map<Integer, Integer> totalCount) {
        // For the current node, we need to calculate the counts of each colors in its
        // subtree
        int largestCount = -1; // We'll need to keep count of the largest hash map - all smaller ones will be
                               // merged into this one and apply to this current node
        HashMap<Integer, Integer> largestMap = null;
        for (Edge edge : nodes[current].edges) {
            int child = (edge.from == current) ? edge.to : edge.from;
            if (child != parent) {
                solve(child, current, nodes, cache, totalCount);
                // Look at the color counts associated with this child
                HashMap<Integer, Integer> childSubtreeColorMap = nodes[child].colorCounts;
                if (childSubtreeColorMap.size() > largestCount) {
                    largestCount = childSubtreeColorMap.size();
                    largestMap = childSubtreeColorMap;
                }
                for (int color : childSubtreeColorMap.keySet()) {
                    int colorSubtreeCount = childSubtreeColorMap.get(color);
                    int totalColorCount = totalCount.get(color);
                    cache[edge.idx] += (totalColorCount - colorSubtreeCount) * colorSubtreeCount;
                }
            }
        }
        if (largestMap != null) {
            for (Edge edge : nodes[current].edges) {
                int child = (edge.from == current) ? edge.to : edge.from;
                if (child != parent) {
                    HashMap<Integer, Integer> childMap = nodes[child].colorCounts;
                    if (childMap != largestMap) {
                        for (int color : childMap.keySet()) {
                            if (!largestMap.containsKey(color)) {
                                largestMap.put(color, 0);
                            }
                            largestMap.put(color, largestMap.get(color) + childMap.get(color));
                        }
                    }
                }
            }
            nodes[current].colorCounts = largestMap;
        }
        if (!nodes[current].colorCounts.containsKey(nodes[current].color)) {
            nodes[current].colorCounts.put(nodes[current].color, 0);
        }
        nodes[current].colorCounts.put(nodes[current].color, nodes[current].colorCounts.get(nodes[current].color) + 1);
    }

}
