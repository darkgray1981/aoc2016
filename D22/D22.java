package D22;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

public class D22 {

    public static void main(String[] args) throws IOException {

        long startTime = System.nanoTime();

        List<String> input = Files.readAllLines(Paths.get("input.txt"));

        List<String> test1 = Arrays.asList("Filesystem            Size  Used  Avail  Use%\n/dev/grid/node-x0-y0   10T    8T     2T   80%\n/dev/grid/node-x0-y1   11T    6T     5T   54%\n/dev/grid/node-x0-y2   32T   28T     4T   87%\n/dev/grid/node-x1-y0    9T    7T     2T   77%\n/dev/grid/node-x1-y1    8T    0T     8T    0%\n/dev/grid/node-x1-y2   11T    7T     4T   63%\n/dev/grid/node-x2-y0   10T    6T     4T   60%\n/dev/grid/node-x2-y1    9T    8T     1T   88%\n/dev/grid/node-x2-y2    9T    6T     3T   66%\n".split("\n"));

        P1(input); // = 903

        // P2(test1); // = 7
        P2(input); // = 215

        long endTime = System.nanoTime();
        System.out.println("Time taken: " + (endTime - startTime) / 1e9 + "s");
    }

    private static void P1(List<String> input) {

        ArrayList<Node> nodes = new ArrayList<>();

        for (String line : input) {

            if (!line.startsWith("/dev")) {
                continue;
            }

            String[] parts = line.split("T?\\s+");

            String[] coords = parts[0].split("-");
            int x = Integer.parseInt(coords[1].substring(1));
            int y = Integer.parseInt(coords[2].substring(1));

            int size = Integer.parseInt(parts[1]);
            int used = Integer.parseInt(parts[2]);
            int avail = Integer.parseInt(parts[3]);
            
            nodes.add(new Node(x, y, size, used, avail));
        }

        int result = 0;

        for (Node a : nodes) {
            if (a.used == 0) {
                continue;
            }

            for (Node b : nodes) {
                if (a != b && a.used <= b.avail) {
                    result++;
                }
            }    
        }

        System.out.println("Viable: " + result);
    }

    private static void P2(List<String> input) {

        ArrayList<Node> nodes = new ArrayList<>();
        Node origin = null;

        int xMax = 0, yMax = 0;
        for (String line : input) {

            if (!line.startsWith("/dev")) {
                continue;
            }

            String[] parts = line.split("T?\\s+");

            String[] coords = parts[0].split("-");
            int x = Integer.parseInt(coords[1].substring(1));
            int y = Integer.parseInt(coords[2].substring(1));

            xMax = Math.max(xMax, x);
            yMax = Math.max(yMax, y);

            int size = Integer.parseInt(parts[1]);
            int used = Integer.parseInt(parts[2]);
            int avail = Integer.parseInt(parts[3]);
            
            nodes.add(new Node(x, y, size, used, avail));

            if (used == 0) {
                origin = nodes.get(nodes.size()-1);
            }
        }

        int w = xMax+1;
        int h = yMax+1;

        Grid grid = new Grid(w, h, new char[h][w]);

        for (Node n : nodes) {
            if (n.used <= origin.avail) {
                grid.data[n.y][n.x] = '.';
            } else {
                grid.data[n.y][n.x] = '#';
            }
        }

        State s = new State(origin.x, origin.y, xMax, 0, w, h, 0);

        HashMap<Integer, Integer> best = new HashMap<>();
        best.put(s.hash(), s.depth);

        PriorityQueue<State> pq = new PriorityQueue<>();
        pq.add(s);
        
        int result = Integer.MAX_VALUE;

        while (!pq.isEmpty()) {

            s = pq.remove();

            if (s.tx == 0 && s.ty == 0) {
                result = Math.min(result, s.depth);
                continue;
            } else if (s.depth >= result) {
                continue;
            }

            for (int[] delta : directions) {
                int nx = s.x+delta[0];
                int ny = s.y+delta[1];
    
                if (!grid.viable(nx, ny)) {
                    continue;
                }

                State next = new State(nx, ny, s.tx, s.ty, s.w, s.h, s.depth+1);
                if (nx == next.tx && ny == next.ty) {
                    next.tx = s.x;
                    next.ty = s.y;
                }

                Integer b = best.get(next.hash());
                if (b == null || next.depth < b) {
                    best.put(next.hash(), next.depth);
                    pq.add(next);
                }
            }
        }

        System.out.println("Steps: " + result);
    }

    private static class Node {
        int x;
        int y;
        int size;
        int used;
        int avail;

        Node(int x, int y, int size, int used, int avail) {
            this.x = x;
            this.y = y;
            this.size = size;
            this.used = used;
            this.avail = avail;
        }
    }

    private static final int[][] directions = new int[][]{
        new int[]{0, -1}, // up
        new int[]{+1, 0}, // right
        new int[]{0, +1}, // down
        new int[]{-1, 0}, // left
    };

    private static class Grid {
        int w;
        int h;
        char[][] data;

        Grid(int w, int h, char[][] data) {
            this.w = w;
            this.h = h;
            this.data = data;
        }

        boolean viable(int x, int y) {

            if (x < 0 || x >= this.w || y < 0 || y >= this.h) {
                return false;
            }
    
            return this.data[y][x] != '#';
        }

        char get(int x, int y) {

            if (x < 0 || x >= this.w || y < 0 || y >= this.h) {
                return '#';
            }
    
            return this.data[y][x];
        }

        void print() {

            for (int y = 0; y < h; y++) {
                for (int x = 0; x < w; x++) {
                    System.out.print(this.get(x, y));
                }
                System.out.println();
            }
        }
    }

    private static class State implements Comparable<State> {
        int x;
        int y;
        int tx;
        int ty;
        int w;
        int h;
        int depth;

        State(int x, int y, int tx, int ty, int w, int h, int depth) {
            this.x = x;
            this.y = y;
            this.tx = tx;
            this.ty = ty;
            this.w = w;
            this.h = h;
            this.depth = depth;
        }

        public int hash() {
            return (w*h*(this.ty*w + this.tx) + this.y*w + this.x);
        }

        @Override
        public int compareTo(State o) {
            return this.hash() - o.hash();
        }
    }
}
