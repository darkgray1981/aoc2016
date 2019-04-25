package D18;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class D18 {

    public static void main(String[] args) throws IOException {

        long startTime = System.nanoTime();

        List<String> input = Files.readAllLines(Paths.get("input.txt"));
        String line = input.get(0);

        // P12(".^^.^.^^^^", 10); // = 38
        P12(line, 40); // = 1978

        P12(line, 400000); // = 20003246

        long endTime = System.nanoTime();
        System.out.println("Time taken: " + (endTime - startTime) / 1e9 + "s");
    }

    private static void P12(String input, int rows) {

        int result = 0;
        int len = input.length();

        byte[] prev = new byte[len + 2];
        byte[] next = new byte[len + 2];

        for (int i = 0; i < len; i++) {
            if (input.charAt(i) == '^') {
                prev[i+1] = 1;
            } else {
                result++;
            }
        }

        for (int n = 1; n < rows; n++) {
            for (int i = 1; i <= len; i++) {

                int mask = (prev[i-1] << 2) | (prev[i] << 1) | prev[i+1];
                if (map[mask]) {
                    next[i] = 1;
                } else {
                    next[i] = 0;
                    result++;
                }
            }
    
            byte[] temp = prev;
            prev = next;
            next = temp;
        }

        System.out.println("Safe: " + result);
    }

    private final static boolean[] map = new boolean[] {
        false, // 000 : 0
        true,  // 001 : 1
        false, // 010 : 2
        true,  // 011 : 3
        true,  // 100 : 4
        false, // 101 : 5
        true,  // 110 : 6
        false, // 111 : 7
    };
}
