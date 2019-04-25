package D01;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.AbstractMap.SimpleEntry;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;

public class D01 {

    public static void main(String[] args) throws IOException {

        long startTime = System.nanoTime();

        List<String> input = Files.readAllLines(Paths.get("input.txt"));
        String line = input.get(0);

        P1(line); // = 300

        // P2("R8, R4, R4, R8"); // = 4
        P2(line); // = 159

        long endTime = System.nanoTime();
        System.out.println("Time taken: " + (endTime - startTime) / 1e9 + "s");
    }

    private static void P1(String input) {

        int dir = 0;
        int x = 0, y = 0;

        for (String inst : input.split(", ")) {

            char turn = inst.charAt(0);
            int steps = Integer.parseInt(inst.substring(1));

            switch (turn) {
            case 'L':
                dir = (4 + dir - 1) % 4;
                break;
            case 'R':
                dir = (dir + 1) % 4;
                break;
            }

            switch (directions[dir]) {
            case 'N':
                y -= steps;
                break;
            case 'E':
                x += steps;
                break;
            case 'S':
                y += steps;
                break;
            case 'W':
                x -= steps;
                break;
            }
        }

        int result = Math.abs(x) + Math.abs(y);

        System.out.println("Blocks: " + result);
    }

    private static void P2(String input) {

        HashSet<Entry<Integer, Integer>> set = new HashSet<>();

        int dir = 0;
        int x = 0, y = 0;

        set.add(new SimpleEntry<>(x, y));

        outer: for (String inst : input.split(", ")) {

            char turn = inst.charAt(0);
            int steps = Integer.parseInt(inst.substring(1));

            switch (turn) {
            case 'L':
                dir = (4 + dir - 1) % 4;
                break;
            case 'R':
                dir = (dir + 1) % 4;
                break;
            }

            int dx = 0, dy = 0;

            switch (directions[dir]) {
            case 'N':
                dy = -1;
                break;
            case 'E':
                dx = 1;
                break;
            case 'S':
                dy = 1;
                break;
            case 'W':
                dx = -1;
                break;
            }

            while (steps-- > 0) {
                x += dx;
                y += dy;

                if (!set.add(new SimpleEntry<>(x, y))) {
                    break outer;
                }
            }
        }

        int result = Math.abs(x) + Math.abs(y);

        System.out.println("Blocks again: " + result);
    }

    private final static char[] directions = new char[] { 'N', 'E', 'S', 'W' };
}
