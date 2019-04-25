package D19;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class D19 {

    public static void main(String[] args) throws IOException {

        long startTime = System.nanoTime();

        List<String> input = Files.readAllLines(Paths.get("input.txt"));
        String line = input.get(0);

        // P1("5"); // = 3
        P1(line); // = 1841611

        // P2("5"); // = 2
        P2(line); // = 1423634

        long endTime = System.nanoTime();
        System.out.println("Time taken: " + (endTime - startTime) / 1e9 + "s");
    }

    private static void P1(String input) {

        int count = Integer.parseInt(input);

        Node first = new Node((count % 2 == 0 ? 1 : 3));
        Node last = first;
        for (int i = first.id + 2; i <= count; i += 2) {
            last.next = new Node(i);
            last = last.next;
        }

        last.next = first;

        last = first;
        while (count > 1) {
            last.next = last.next.next;
            last = last.next;
            count--;
        }

        System.out.println("Winner: " + last.id);
    }

    private static void P2(String input) {

        int count = Integer.parseInt(input);

        Node first = new Node(1);
        Node middle = first;
        Node last = first;
        for (int i = 2; i <= count; i++) {
            if (i == count/2+1) {
                middle = last;
            }
            last.next = new Node(i);
            last = last.next;
        }

        last.next = first;

        while (count > 1) {

            middle.next = middle.next.next;

            if (count % 2 == 1) {
                middle = middle.next;
            }
            count--;
        }

        System.out.println("2nd Winner: " + middle.id);
    }

    private static class Node {
        int id;
        Node next;

        Node(int id) {
            this.id = id;
        }
    }
}
