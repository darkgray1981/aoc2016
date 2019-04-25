package D02;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class D02 {

    public static void main(String[] args) throws IOException {

        long startTime = System.nanoTime();

        List<String> input = Files.readAllLines(Paths.get("input.txt"));

        List<String> test1 = Arrays.asList("ULL RRDDD LURDL UUUUD".split(" "));

        // P1(test1); // = 1985
        P1(input); // = 18843

        // P2(test1); // = 5DB3
        P2(input); // = 67BB9

        long endTime = System.nanoTime();
        System.out.println("Time taken: " + (endTime - startTime) / 1e9 + "s");
    }

    private static void P1(List<String> input) {

        String result = "";

        int x = 1, y = 1;

        for (String line : input) {

            for (int i = 0; i < line.length(); i++) {
                char c = line.charAt(i);

                switch (c) {
                case 'U':
                    y = Math.max(0, y - 1);
                    break;
                case 'R':
                    x = Math.min(2, x + 1);
                    break;
                case 'D':
                    y = Math.min(2, y + 1);
                    break;
                case 'L':
                    x = Math.max(0, x - 1);
                    break;
                }
            }

            result += numpad[y][x];
        }

        System.out.println("Code: " + result);
    }

    private static void P2(List<String> input) {

        String result = "";

        int x = 0, y = 2;

        for (String line : input) {

            for (int i = 0; i < line.length(); i++) {
                char c = line.charAt(i);

                int nx = x, ny = y;

                switch (c) {
                case 'U':
                    ny = Math.max(0, y - 1);
                    break;
                case 'R':
                    nx = Math.min(4, x + 1);
                    break;
                case 'D':
                    ny = Math.min(4, y + 1);
                    break;
                case 'L':
                    nx = Math.max(0, x - 1);
                    break;
                }

                if (codepad[ny][nx] != ' ') {
                    x = nx;
                    y = ny;
                }

            }

            result += codepad[y][x];
        }

        System.out.println("2nd Code: " + result);
    }

    private final static char[][] numpad = new char[][] {
        new char[] { '1', '2', '3' },
        new char[] { '4', '5', '6' },
        new char[] { '7', '8', '9' },
    };

    private final static char[][] codepad = new char[][] {
        new char[] { ' ', ' ', '1', ' ', ' ' },
        new char[] { ' ', '2', '3', '4', ' ' },
        new char[] { '5', '6', '7', '8', '9' },
        new char[] { ' ', 'A', 'B', 'C', ' ' },
        new char[] { ' ', ' ', 'D', ' ', ' ' },
    };
}
