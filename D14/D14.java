package D14;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class D14 {

    public static void main(String[] args) throws IOException {

        long startTime = System.nanoTime();

        List<String> input = Files.readAllLines(Paths.get("input.txt"));
        String line = input.get(0);

        // P1("abc"); // = 22728
        P1(line); // = 25427

        // P2("abc"); // = 22551
        P2(line); // = 22045

        long endTime = System.nanoTime();
        System.out.println("Time taken: " + (endTime - startTime) / 1e9 + "s");
    }

    private static void P1(String input) {

        int result = padKey(input, false);

        System.out.println("Index: " + result);
    }

    private static void P2(String input) {

        int result = padKey(input, true);

        System.out.println("Stretched Index: " + result);
    }

    private final static char[] hex = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    private static byte[] hexify(byte[] bytes) {

        byte[] result = new byte[32];

        for (int i = 0; i < bytes.length; i++) {
            result[2*i] = (byte)hex[(bytes[i] >> 4) & 0xF];
            result[2*i+1] = (byte)hex[bytes[i] & 0xF];
        }

        return result;
    }

    private static byte[] hash(MessageDigest md, String input, boolean stretch) {

        byte[] md5 = md.digest((input).getBytes());
        if (stretch) {
            for (int i = 0; i < 2016; i++) {
                md5 = md.digest(hexify(md5));
            }
        }

        return md5;
    }

    private static int padKey(String input, boolean stretch) {

        ArrayList<ArrayList<Integer>> threes = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            threes.add(new ArrayList<>());
        }

        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        TreeSet<Integer> found = new TreeSet<>();
        int n = 0, limit = 1000;
        while (found.size() < 64 || limit-- > 0) {

            byte[] md5 = hash(md, input + n, stretch);

            boolean used = false;
            int lastb = (md5[0] >> 4) & 0xF;
            int lasti = 0;

            for (int i = 1; i < 32; i++) {
                int b;
                if (i % 2 == 0) {
                    b = (md5[i/2] >> 4) & 0xF;
                } else {
                    b = md5[i/2] & 0xF;
                }

                if (b != lastb) {
                    if (!used && i - lasti >= 3) {
                        threes.get(lastb).add(n);
                        used = true;
                    }
                    if (i - lasti >= 5) {
                        for (int index : threes.get(lastb)) {
                            if (n != index && n - index <= 1000) {
                                found.add(index);
                            }
                        }
                        threes.get(lastb).clear();
                        threes.get(lastb).add(n);
                    }

                    lastb = b;
                    lasti = i;
                }
            }

            if (!used && 32 - lasti >= 3) {
                threes.get(lastb).add(n);
            }
            if (32 - lasti >= 5) {
                for (int index : threes.get(lastb)) {
                    if (n != index && n - index <= 1000) {
                        found.add(index);
                    }
                }
                threes.get(lastb).clear();
                threes.get(lastb).add(n);
            }

            n++;
        }

        int result = 0, count = 64;
        for (int index : found) {
            if (--count <= 0) {
                result = index;
                break;
            }
        }

        return result;
    }
}
