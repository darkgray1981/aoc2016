package D16;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class D16 {

    public static void main(String[] args) throws IOException {

        long startTime = System.nanoTime();

        List<String> input = Files.readAllLines(Paths.get("input.txt"));
        String line = input.get(0);

        // P12("10000", 20); // = 01100
        P12(line, 272); // = 00000100100001100

        P12(line, 35651584); // = 00011010100010010

        long endTime = System.nanoTime();
        System.out.println("Time taken: " + (endTime - startTime) / 1e9 + "s");
    }

    private static void P12(String input, int length) {

        char[] data = input.toCharArray();

        data = dragon(data, length);

        System.out.println("Checksum: " + checksum(data, length));
    }

    private static char[] dragon(char[] a, int length) {

        char[] b = new char[length];
        System.arraycopy(a, 0, b, 0, Math.min(a.length, b.length));

        int len = a.length;
        while (len < length) {
            b[len] = '0';

            int nextLen = Math.min(2*len+1, length);
            for (int i = len+1, j = len-1; i < nextLen; i++, j--) {
                b[i] = (b[j] == '1' ? '0' : '1');
            }

            len = nextLen;
        }

        return b;
    }

    private static String checksum(char[] ca, int length) {

        while (true) {
            if (length % 4 == 0) {
                length /= 4;
                for (int i = 0; i < length; i++) {
                    ca[i] = ((ca[4*i] == ca[4*i+1]) == (ca[4*i+2] == ca[4*i+3]) ? '1' : '0');
                }
            } else if (length % 2 == 0) {
                length /= 2;
                for (int i = 0; i < length; i++) {
                    ca[i] = (ca[2*i] == ca[2*i+1] ? '1' : '0');
                }
            } else {
                break;
            }
        }

        return new String(ca, 0, length);
    }
}
