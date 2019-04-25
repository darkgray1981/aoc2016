package D07;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;

public class D07 {

    public static void main(String[] args) throws IOException {

        long startTime = System.nanoTime();

        List<String> input = Files.readAllLines(Paths.get("input.txt"));

        P1(input); // = 115

        P2(input); // = 231

        long endTime = System.nanoTime();
        System.out.println("Time taken: " + (endTime - startTime) / 1e9 + "s");
    }

    private static void P1(List<String> input) {

        int result = 0;

        for (String ip : input) {

            boolean inBracket = false;
            boolean abba = false;

            for (int i = 0; i < ip.length() - 3; i++) {

                if (ip.charAt(i) == '[') {
                    inBracket = true;
                    continue;
                } else if (ip.charAt(i) == ']') {
                    inBracket = false;
                    continue;
                }

                if (ip.charAt(i) == ip.charAt(i+3) && ip.charAt(i+1) == ip.charAt(i+2) && ip.charAt(i) != ip.charAt(i+1)) {
                    if (inBracket) {
                        abba = false;
                        break;
                    }

                    abba = true;
                }
            }

            if (abba) {
                result++;
            }
        }

        System.out.println("TLS: " + result);
    }

    private static void P2(List<String> input) {

        int result = 0;

        for (String ip : input) {

            boolean inBracket = false;
            HashSet<String> inside = new HashSet<>();
            HashSet<String> outside = new HashSet<>();

            for (int i = 0; i < ip.length() - 2; i++) {

                if (ip.charAt(i) == '[') {
                    inBracket = true;
                    continue;
                } else if (ip.charAt(i) == ']') {
                    inBracket = false;
                    continue;
                }

                if (ip.charAt(i) == ip.charAt(i+2) && ip.charAt(i) != ip.charAt(i+1) && ip.charAt(i+1) != '[' && ip.charAt(i+1) != ']') {
                    if (inBracket) {
                        inside.add(ip.substring(i, i+3));
                    } else {
                        outside.add(ip.substring(i, i+3));
                    }
                }
            }

            for (String key : inside) {
                if (outside.contains("" + key.charAt(1) + key.charAt(0) + key.charAt(1))) {
                    result++;
                    break;
                }
            }
        }

        System.out.println("SSL: " + result);
    }
}
