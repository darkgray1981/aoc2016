package D21;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class D21 {

    public static void main(String[] args) throws IOException {

        long startTime = System.nanoTime();

        List<String> input = Files.readAllLines(Paths.get("input.txt"));

        P1(input); // = agcebfdh

        P2(input); // = afhdbegc

        long endTime = System.nanoTime();
        System.out.println("Time taken: " + (endTime - startTime) / 1e9 + "s");
    }

    private static void P1(List<String> input) {

        char[] password = "abcdefgh".toCharArray();
        char[] buffer = new char[password.length];

        for (String line : input) {

            String[] parts = line.split(" ");

            if (line.startsWith("swap position")) {

                swapPos(password, Integer.parseInt(parts[2]), Integer.parseInt(parts[5]));

            } else if (line.startsWith("swap letter")) {

                swapLetter(password, parts[2].charAt(0), parts[5].charAt(0));

            } else if (line.startsWith("rotate based")) {

                rotatePos(password, buffer, parts[6].charAt(0));

            } else if (line.startsWith("rotate left")) {

                rotateLeftRight(password, buffer, -1 * Integer.parseInt(parts[2]));

            } else if (line.startsWith("rotate right")) {

                rotateLeftRight(password, buffer, Integer.parseInt(parts[2]));

            } else if (line.startsWith("reverse")) {

                reverse(password, Integer.parseInt(parts[2]), Integer.parseInt(parts[4]));

            } else if (line.startsWith("move")) {

                move(password, Integer.parseInt(parts[2]), Integer.parseInt(parts[5]));

            }
        }

        System.out.println("Scrambled: " + new String(password));
    }

    private static void P2(List<String> input) {

        char[] password = "fbgdceah".toCharArray();
        char[] buffer = new char[password.length];

        for (int i = input.size()-1; i >= 0; i--) {

            String line = input.get(i);

            String[] parts = line.split(" ");

            if (line.startsWith("swap position")) {

                swapPos(password, Integer.parseInt(parts[5]), Integer.parseInt(parts[2]));

            } else if (line.startsWith("swap letter")) {

                swapLetter(password, parts[5].charAt(0), parts[2].charAt(0));

            } else if (line.startsWith("rotate based")) {

                char[] temp = new char[password.length];
                int count = 0;

                while (true) {
                    count++;
                    System.arraycopy(password, 0, temp, 0, password.length);
    
                    rotateLeftRight(temp, buffer, count);
                    rotatePos(temp, buffer, parts[6].charAt(0));

                    if (Arrays.equals(password, temp)) {
                        break;
                    }
                }

                rotateLeftRight(password, buffer, count);

            } else if (line.startsWith("rotate left")) {

                rotateLeftRight(password, buffer, +1 * Integer.parseInt(parts[2]));

            } else if (line.startsWith("rotate right")) {

                rotateLeftRight(password, buffer, -1 * Integer.parseInt(parts[2]));

            } else if (line.startsWith("reverse")) {

                reverse(password, Integer.parseInt(parts[2]), Integer.parseInt(parts[4]));

            } else if (line.startsWith("move")) {

                move(password, Integer.parseInt(parts[5]), Integer.parseInt(parts[2]));

            }
        }

        System.out.println("Unscrambled: " + new String(password));
    }

    private static void swapPos(char[] arr, int x, int y) {

        char temp = arr[x];
        arr[x] = arr[y];
        arr[y] = temp;
    }

    private static void swapLetter(char[] arr, char x, char y) {

        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == x) {
                arr[i] = y;
            } else if (arr[i] == y) {
                arr[i] = x;
            }
        }
    }

    private static void rotateLeftRight(char[] arr, char[] buf, int steps) {

        steps = steps % arr.length;

        for (int i = 0; i < arr.length; i++) {
            buf[(arr.length + i + steps) % arr.length] = arr[i];
        }

        System.arraycopy(buf, 0, arr, 0, arr.length);
    }

    private static void rotatePos(char[] arr, char[] buf, char x) {

        int pos = -1;

        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == x) {
                pos = i;
                break;
            }
        }

        rotateLeftRight(arr, buf, 1 + pos + (pos >= 4 ? 1 : 0));
    }

    private static void reverse(char[] arr, int x, int y) {

        for (int i = x, j = y; i < j; i++, j--) {
            char temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }
    }

    private static void move(char[] arr, int x, int y) {

        char temp = arr[x];
        if (x < y) {
            System.arraycopy(arr, x+1, arr, x, y-x);
        } else {
            for (int i = x; i > y; i--) {
                arr[i] = arr[i-1];
            }
        }
        arr[y] = temp;
    }
}
