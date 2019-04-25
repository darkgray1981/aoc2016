package D20;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class D20 {

    public static void main(String[] args) throws IOException {

        long startTime = System.nanoTime();

        List<String> input = Files.readAllLines(Paths.get("input.txt"));

        List<String> test1 = Arrays.asList("5-8\n0-2\n4-7".split("\n"));

        // P1(test1); // = 3
        P1(input); // = 14975795

        // P2(test1, 9); // = 2
        P2(input, 4294967295L); // = 101

        long endTime = System.nanoTime();
        System.out.println("Time taken: " + (endTime - startTime) / 1e9 + "s");
    }

    private static void P1(List<String> input) {

        ArrayList<Range> ranges = parse(input);

        long result = 0;
        for (Range r : ranges) {
            if (result < r.low) {
                break;
            }
            result = Math.max(result, r.high+1);
        }

        System.out.println("IP: " + result);
    }

    private static void P2(List<String> input, long limit) {

        ArrayList<Range> ranges = parse(input);

        long result = 0, from = 0;
        for (Range r : ranges) {
            if (from < r.low) {
                result += r.low - from;
            }
            from = Math.max(from, r.high+1);
        }

        result += limit - from + 1;

        System.out.println("IPs: " + result);
    }

    private static ArrayList<Range> parse(List<String> input) {

        ArrayList<Range> ranges = new ArrayList<>();
        
        for (String line : input) {
            String[] parts = line.split("-");
            ranges.add(new Range(Long.parseLong(parts[0]), Long.parseLong(parts[1])));
        }

        Collections.sort(ranges);

        return ranges;
    }

    private static class Range implements Comparable<Range> {
        long low;
        long high;

        Range(long low, long high) {
            this.low = low;
            this.high = high;
        }

        @Override
        public int compareTo(Range o) {
            if (this.low == o.low) {
                return Long.compare(this.high, o.high);
            }
            return Long.compare(this.low, o.low);
        }

        @Override
        public String toString() {
            return "[" + this.low + " <-> " + this.high + "]";
        }
    }
}
