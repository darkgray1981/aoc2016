package D09;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class D09 {

    public static void main(String[] args) throws IOException {

        long startTime = System.nanoTime();

        List<String> input = Files.readAllLines(Paths.get("input.txt"));
        String line = input.get(0);

        // P1("X(8x2) (3x3)AB CY"); // = 18
        P1(line); // = 107035

        // P2("(25x3)(3x3)ABC(2x3)XY(5x2)PQRSTX(18x9)(3x2)TWO(5x7)SEVEN"); // = 445
        P2(line); // = 11451628995

        long endTime = System.nanoTime();
        System.out.println("Time taken: " + (endTime - startTime) / 1e9 + "s");
    }

    private static void P1(String input) {

        if (input.indexOf(' ') != -1) {
            input = input.replaceAll("\\s+", "");
        }

        long result = decompressedSize(input, false);

        System.out.println("Decompressed: " + result);
    }

    private static void P2(String input) {

        if (input.indexOf(' ') != -1) {
            input = input.replaceAll("\\s+", "");
        }

        long result = decompressedSize(input, true);

        System.out.println("Fully Decompressed: " + result);
    }

    private static long decompressedSize(String input, boolean recurse) {

        long size = 0;

        int i = 0;
        while (i < input.length()) {
            char c = input.charAt(i);

            if (c == '(') {
                int j = input.indexOf(')', i+1);

                String[] axb = input.substring(i+1, j).split("x");

                int a = Integer.parseInt(axb[0]);
                int b = Integer.parseInt(axb[1]);

                long sub = a;
                if (recurse) {
                    sub = decompressedSize(input.substring(j+1, j+1+a), recurse);
                }

                size += b * sub;

                i = j+a;

            } else {
                size++;
            }

            i++;
        }

        return size;
    }    
}
