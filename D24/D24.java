package D24;

import java.awt.Point;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class D24 {

    public static void main(String[] args) throws IOException {

        long startTime = System.nanoTime();

        List<String> input = Files.readAllLines(Paths.get("input.txt"));

        List<String> test1 = Arrays.asList("###########\n#0.1.....2#\n#.#######.#\n#4.......3#\n###########".split("\n"));

        // P1(test1); // = 14
        P1(input); // = 502
    
        P2(input); // = 724
    
        long endTime = System.nanoTime();
        System.out.println("Time taken: " + (endTime - startTime) / 1e9 + "s");
    }

    private static void P1(List<String> input) {

        HashMap<Character, HashMap<Character, Integer>> map = prepare(input);

        int result = path('0', map.size(), new ArrayList<Character>(map.keySet()), map, false);
        
        System.out.println("Shortest: " + result);
    }

    private static void P2(List<String> input) {

        HashMap<Character, HashMap<Character, Integer>> map = prepare(input);

        int result = path('0', map.size(), new ArrayList<Character>(map.keySet()), map, true);
        
        System.out.println("Shortest back: " + result);
    }
 
    private static final int[][] directions = new int[][]{
        new int[]{0, -1}, // up
        new int[]{+1, 0}, // right
        new int[]{0, +1}, // down
        new int[]{-1, 0}, // left
    };

    private static HashMap<Character, HashMap<Character, Integer>> prepare(List<String> input) {

        TreeMap<Character, Point> poi = new TreeMap<>();
        HashMap<Character, HashMap<Character, Integer>> map = new HashMap<>();

        char[][] grid = new char[input.size()][input.get(0).length()];

        for (int y = 0; y < input.size(); y++) {

            String line = input.get(y);
            for (int x = 0; x < line.length(); x++) {
                grid[y][x] = line.charAt(x);
                
                if (grid[y][x] >= '0' && grid[y][x] <= '9') {
                    poi.put(grid[y][x], new Point(x, y));
                    map.put(grid[y][x], new HashMap<>());
                }
            }
        }

        for (Map.Entry<Character, Point> e : poi.entrySet()) {

            Character origin = e.getKey();
            Point p = e.getValue();

            HashSet<Point> seen = new HashSet<>();
            seen.add(p);

            ArrayDeque<Point> queue = new ArrayDeque<>();
            queue.add(p);

            for (int steps = 0; !queue.isEmpty(); steps++) {
                for (int i = 0, size = queue.size(); i < size; i++) {

                    p = queue.remove();

                    if (grid[p.y][p.x] >= '0' && grid[p.y][p.x] <= '9') {
                        map.get(origin).put(grid[p.y][p.x], steps);
                    }

                    for (int[] delta : directions) {
                        int nx = p.x+delta[0];
                        int ny = p.y+delta[1];

                        if (grid[ny][nx] != '#' && seen.add(new Point(nx, ny))) {
                            queue.add(new Point(nx, ny));
                        }
                    }
                }
            }
        }

        return map;
    }
   
    private static int path(Character c, int left, ArrayList<Character> list, HashMap<Character, HashMap<Character, Integer>> map, boolean back) {

        if (left <= 1) {
            return (back ? map.get(c).get('0') : 0);
        }

        int shortest = Integer.MAX_VALUE;

        int index = list.indexOf(c);
        list.set(index, '#');
        for (Character d : list) {
            if (d != '#') {
                shortest = Math.min(shortest, path(d, left-1, list, map, back) + map.get(c).get(d));
            }
        }
        list.set(index, c);

        return shortest;
    }
}
