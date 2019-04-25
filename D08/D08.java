package D08;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class D08 {

    public static void main(String[] args) throws IOException {

        long startTime = System.nanoTime();

        List<String> input = Files.readAllLines(Paths.get("input.txt"));

        List<String> test1 = Arrays.asList("rect 3x2\nrotate column x=1 by 1\nrotate row y=0 by 4\nrotate column x=1 by 1".split("\n"));

        // P1(test1); // = 6
        P1(input); // = 110

        P2(input); // = zjhrkcplyj

        long endTime = System.nanoTime();
        System.out.println("Time taken: " + (endTime - startTime) / 1e9 + "s");
    }

    private static void P1(List<String> input) {

        boolean[][] screen = renderScreen(input, 50, 6);

        int result = countLit(screen);
        System.out.println("Lit: " + result);
    }

    private static void P2(List<String> input) {

        boolean[][] screen = renderScreen(input, 50, 6);

        System.out.println("Code:\n");
        print(screen);
    }

    private static boolean[][] renderScreen(List<String> input, int w, int h) {

        boolean[][] screen = new boolean[h][w];

        Pattern rect = Pattern.compile("rect (\\d+)x(\\d+)");
        Pattern rotate = Pattern.compile("rotate (\\w+) .=(\\d+) by (\\d+)");

        for (String line : input) {

            Matcher m = rect.matcher(line);
            if (m.find()) {
                int a = Integer.parseInt(m.group(1));
                int b = Integer.parseInt(m.group(2));

                rect(a, b, screen);

            } else {
                m = rotate.matcher(line);
                m.find();

                String dir = m.group(1);
                int a = Integer.parseInt(m.group(2));
                int b = Integer.parseInt(m.group(3));

                if (dir.equals("row")) {
                    rotateRow(a, b, screen);
                } else {
                    rotateCol(a, b, screen);
                }
            }
        }

        return screen;
    }

    private static void rect(int a, int b, boolean[][] screen) {

        for (int y = 0; y < b; y++) {
            for (int x = 0; x < a; x++) {
                screen[y][x] = true;
            }
        }
    }

    private static void rotateRow(int a, int b, boolean[][] screen) {

        boolean[] temp = new boolean[screen[0].length];

        for (int x = 0; x < temp.length; x++) {
            temp[(x + b) % temp.length] = screen[a][x];
        }

        screen[a] = temp;
    }

    private static void rotateCol(int a, int b, boolean[][] screen) {

        boolean[] temp = new boolean[screen.length];

        for (int y = 0; y < temp.length; y++) {
            temp[(y + b) % temp.length] = screen[y][a];
        }

        for (int y = 0; y < temp.length; y++) {
            screen[y][a] = temp[y];
        }
    }

    private static void print(boolean[][] screen) {

        for (int y = 0; y < screen.length; y++) {
            for (int x = 0; x < screen[0].length; x++) {
                System.out.print(screen[y][x] ? '#' : ' ');
            }
            System.out.println();
        }

        System.out.println();
    }

    private static int countLit(boolean[][] screen) {

        int lit = 0;

        for (int y = 0; y < screen.length; y++) {
            for (int x = 0; x < screen[0].length; x++) {
                lit += (screen[y][x] ? 1 : 0);
            }
        }

        return lit;
    }
}
