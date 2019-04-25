package D17;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayDeque;
import java.util.List;

public class D17 {

    public static void main(String[] args) throws IOException {

        long startTime = System.nanoTime();

        List<String> input = Files.readAllLines(Paths.get("input.txt"));
        String line = input.get(0);

        // P1("kglvqrro"); // = DDUDRLRRUDRD
        P1(line); // = DRRDRLDURD

        // P2("kglvqrro"); // = 492
        P2(line); // = 618

        long endTime = System.nanoTime();
        System.out.println("Time taken: " + (endTime - startTime) / 1e9 + "s");
    }

    private static void P1(String input) {

        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        Position pos = new Position(0, 0, input);

        ArrayDeque<Position> queue = new ArrayDeque<>();
        queue.add(pos);
        
        String result = input;
        while (!queue.isEmpty()) {

            pos = queue.remove();

            if (pos.x == GRID_SIZE-1 && pos.y == GRID_SIZE-1) {
                result = pos.hash.substring(input.length());
                break;
            }

            byte[] md5 = md.digest((pos.hash).getBytes());

            if (pos.y-1 >= 0 && ((md5[0] >> 4) & 0xF) > 10) {
                queue.add(new Position(pos.x, pos.y-1, pos.hash + 'U'));
            }

            if (pos.y+1 < GRID_SIZE && (md5[0] & 0xF) > 10) {
                queue.add(new Position(pos.x, pos.y+1, pos.hash + 'D'));
            }

            if (pos.x-1 >= 0 && ((md5[1] >> 4) & 0xF) > 10) {
                queue.add(new Position(pos.x-1, pos.y, pos.hash + 'L'));
            }

            if (pos.x+1 < GRID_SIZE && (md5[1] & 0xF) > 10) {
                queue.add(new Position(pos.x+1, pos.y, pos.hash + 'R'));
            }
        }

        System.out.println("Path: " + result);
    }

    private static void P2(String input) {

        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        Position pos = new Position(0, 0, input);

        ArrayDeque<Position> stack = new ArrayDeque<>();
        stack.add(pos);
        
        int result = 0;
        while (!stack.isEmpty()) {

            pos = stack.pop();

            if (pos.x == GRID_SIZE-1 && pos.y == GRID_SIZE-1) {
                result = Math.max(result, pos.hash.length() - input.length());
                continue;
            }

            byte[] md5 = md.digest((pos.hash).getBytes());

            if (pos.y-1 >= 0 && ((md5[0] >> 4) & 0xF) > 10) {
                stack.push(new Position(pos.x, pos.y-1, pos.hash + 'U'));
            }

            if (pos.y+1 < GRID_SIZE && (md5[0] & 0xF) > 10) {
                stack.push(new Position(pos.x, pos.y+1, pos.hash + 'D'));
            }

            if (pos.x-1 >= 0 && ((md5[1] >> 4) & 0xF) > 10) {
                stack.push(new Position(pos.x-1, pos.y, pos.hash + 'L'));
            }

            if (pos.x+1 < GRID_SIZE && (md5[1] & 0xF) > 10) {
                stack.push(new Position(pos.x+1, pos.y, pos.hash + 'R'));
            }
        }

        System.out.println("Longest: " + result);
    }

    private static final int GRID_SIZE = 4;

    private static class Position {
        int x;
        int y;
        String hash;

        Position(int x, int y, String hash) {
            this.x = x;
            this.y = y;
            this.hash = hash;
        }
    }
}
