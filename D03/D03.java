package D03;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class D03 {

    public static void main(String[] args) throws IOException {

        long startTime = System.nanoTime();

        List<String> input = Files.readAllLines(Paths.get("input.txt"));

        P1(input); // = 1050

        P2(input); // = 1921

        long endTime = System.nanoTime();
        System.out.println("Time taken: " + (endTime - startTime) / 1e9 + "s");
    }

    private static void P1(List<String> input) {

        int result = 0;

        Pattern p = Pattern.compile("(\\d+)\\s+(\\d+)\\s+(\\d+)");

        for (String line : input) {

            Matcher m = p.matcher(line);
            m.find();

            int a = Integer.parseInt(m.group(1));
            int b = Integer.parseInt(m.group(2));
            int c = Integer.parseInt(m.group(3));

            if (a + b > c && a + c > b && b + c > a) {
                result++;
            }
        }

        System.out.println("Possible: " + result);
    }

    private static void P2(List<String> input) {

        int result = 0;

        Pattern p = Pattern.compile("(\\d+)\\s+(\\d+)\\s+(\\d+)");

        int a = 0, aa = 0, aaa = 0, b = 0, bb = 0, bbb = 0, c = 0, cc = 0, ccc = 0;
        int count = 0;

        for (String line : input) {
            count++;

            aaa = aa;
            aa = a;
            bbb = bb;
            bb = b;
            ccc = cc;
            cc = c;

            Matcher m = p.matcher(line);
            m.find();

            a = Integer.parseInt(m.group(1));
            b = Integer.parseInt(m.group(2));
            c = Integer.parseInt(m.group(3));

            if (count % 3 == 0) {
                if (a + aa > aaa && a + aaa > aa && aa + aaa > a) {
                    result++;
                }

                if (b + bb > bbb && b + bbb > bb && bb + bbb > b) {
                    result++;
                }

                if (c + cc > ccc && c + ccc > cc && cc + ccc > c) {
                    result++;
                }
            }
        }

        System.out.println("2nd Possible: " + result);
    }
}
