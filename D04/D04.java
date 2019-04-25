package D04;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class D04 {

    public static void main(String[] args) throws IOException {

        long startTime = System.nanoTime();

        List<String> input = Files.readAllLines(Paths.get("input.txt"));

        P1(input); // = 185371

        P2(input); // = 984

        long endTime = System.nanoTime();
        System.out.println("Time taken: " + (endTime - startTime) / 1e9 + "s");
    }

    private static void P1(List<String> input) {

        int result = 0;

        Character[] keys = new Character[] { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };

        for (String line : input) {
            int[] map = new int[keys.length];

            int i = 0;
            for ( ; i < line.length(); i++) {
                char c = line.charAt(i);
                if (c == '-') {
                    continue;
                } else if (c >= '0' && c <= '9') {
                    break;
                }

                map[c - 'a']++;
            }

            int sector = 0;
            for ( ; i < line.length(); i++) {
                char c = line.charAt(i);
                if (c == '[') {
                    break;
                }

                sector = 10 * sector + (c - '0');
            }

            String checksum = line.substring(i+1, line.length()-1);

            Arrays.sort(keys, (a, b) -> {
                if (map[b - 'a'] == map[a - 'a']) {
                    return a - b;
                }
                return map[b - 'a'] - map[a - 'a'];
            });

            boolean matches = true;
            for (int p = 0; p < checksum.length(); p++) {
                matches &= (checksum.charAt(p) == keys[p]);
            }

            if (matches) {
                result += sector;
            }
        }

        System.out.println("ID sum: " + result);
    }

    private static void P2(List<String> input) {

        int result = 0;

        for (String line : input) {

            char[] chars = line.toCharArray();

            int i = 0;
            for (char c : chars) {
                if (c >= '0' && c <= '9') {
                    break;
                }

                i++;
            }
            int end = i;

            int sector = 0;
            for ( ; i < chars.length; i++) {
                if (chars[i] == '[') {
                    break;
                }

                sector = 10 * sector + (chars[i] - '0');
            }

            for (i = 0; i < end; i++) {
                if (chars[i] == '-') {
                    continue;
                }

                chars[i] = (char)('a' + ((((int)chars[i] - 'a') + sector) % ('z'-'a'+1)));
            }

            String decrypted = new String(chars);
            if (decrypted.indexOf("northpole") != -1) {
                result = sector;
                break;
            }
        }

        System.out.println("ID: " + result);
    }
}
