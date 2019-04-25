package D13;

import java.awt.Point;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.List;

public class D13 {

    public static void main(String[] args) throws IOException {

        long startTime = System.nanoTime();

        List<String> input = Files.readAllLines(Paths.get("input.txt"));
        String line = input.get(0);

        // P1("10", 7, 4); // = 11
        P1(line, 31, 39); // = 96

        P2(line, 50); // = 141

        long endTime = System.nanoTime();
        System.out.println("Time taken: " + (endTime - startTime) / 1e9 + "s");
    }

    private static void P1(String input, int tx, int ty) {

        int favNum = Integer.parseInt(input);
        Point pos = new Point(1, 1);

        ArrayDeque<Point> queue = new ArrayDeque<>();
        queue.add(pos);

        HashSet<Point> seen = new HashSet<>();
        seen.add(pos);
        
        int result = 0;
        outer: while (!queue.isEmpty()) {

            int size = queue.size();

            for (int i = 0; i < size; i++) {

                pos = queue.remove();

                if (pos.x == tx && pos.y == ty) {
                    break outer;
                }

                for (int[] delta : directions) {

                    Point np = new Point(pos.x+delta[0], pos.y+delta[1]);

                    if (isSpace(np, favNum) && seen.add(np)) {
                        queue.add(np);
                    }
                }
            }

            result++;
        }

        System.out.println("Steps: " + result);
    }

    private static void P2(String input, int limit) {

        int favNum = Integer.parseInt(input);
        Point pos = new Point(1, 1);

        ArrayDeque<Point> queue = new ArrayDeque<>();
        queue.add(pos);

        HashSet<Point> seen = new HashSet<>();
        seen.add(pos);
        
        int steps = 0;
        while (!queue.isEmpty() && steps++ < limit) {

            int size = queue.size();

            for (int i = 0; i < size; i++) {

                pos = queue.remove();

                for (int[] delta : directions) {

                    Point np = new Point(pos.x+delta[0], pos.y+delta[1]);

                    if (isSpace(np, favNum) && seen.add(np)) {
                        queue.add(np);
                    }
                }
            }
        }

        System.out.println("Locations: " + seen.size());
    }

    private static final int[][] directions = new int[][]{
        new int[]{0, -1}, // up
        new int[]{+1, 0}, // right
        new int[]{0, +1}, // down
        new int[]{-1, 0}, // left
    };

    private static boolean isSpace(Point p, int favNum) {

        if (p.x < 0 || p.y < 0) {
            return false;
        }

        int num = favNum + p.x*p.x + 3*p.x + 2*p.x*p.y + p.y + p.y*p.y;

        boolean result = true;
        while (num != 0) {
            result = !result;
            num &= num - 1;
        }

        return result;
    }
}
