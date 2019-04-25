package D05;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class D05 {

    public static void main(String[] args) throws IOException {

        long startTime = System.nanoTime();

        List<String> input = Files.readAllLines(Paths.get("input.txt"));
        String line = input.get(0);

        // P1("abc"); // = 18f47a30
        P1(line); // = 4543c154

        // P2("abc"); // = 05ace8e3
        P2(line); // = 1050cbbd

        long endTime = System.nanoTime();
        System.out.println("Time taken: " + (endTime - startTime) / 1e9 + "s");
    }

    private static void P1(String input) {

        String result = "";

        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        int n = 0;
        while (result.length() < 8) {

            byte[] md5 = md.digest((input + n).getBytes());

            if (md5[0] == 0 && md5[1] == 0 && (md5[2] >> 4) == 0) {
                result += hex[md5[2] & 0xF];
            }

            n++;
        }

        System.out.println("Password: " + result);
    }

    private static void P2(String input) {

        char[] result = new char[8];

        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        int n = 0, filled = 0;
        while (filled < 8) {

            byte[] md5 = md.digest((input + n).getBytes());

            if (md5[0] == 0 && md5[1] == 0 && (md5[2] >> 4) == 0) {
                int pos = md5[2] & 0xF;
                if (pos >= 0 && pos < 8 && result[pos] == 0) {
                    result[pos] = hex[(md5[3] >> 4) & 0xF];
                    filled++;
                }
            }

            n++;
        }

        System.out.println("2nd Password: " + new String(result));
    }

    private final static char[] hex = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
}
