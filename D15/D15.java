package D15;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class D15 {

    public static void main(String[] args) throws IOException {

        long startTime = System.nanoTime();

        List<String> input = Files.readAllLines(Paths.get("input.txt"));

        P1(input); // = 148737

        P2(input); // = 2353212

        long endTime = System.nanoTime();
        System.out.println("Time taken: " + (endTime - startTime) / 1e9 + "s");
    }

    private static void P1(List<String> input) {

        ArrayList<Disc> discs = parse(input);

        int result = drop(discs);

        System.out.println("Time: " + result);
    }

    private static void P2(List<String> input) {

        ArrayList<Disc> discs = parse(input);

        discs.add(new Disc(discs.size(), 11, 0));

        int result = drop(discs);

        System.out.println("2nd Time: " + result);
    }

    private static class Disc {
        int id;
        int positions;
        int start;

        Disc(int id, int positions, int start) {
            this.id = id;
            this.positions = positions;
            this.start = start;
        }

        boolean Pass(int time) {
            return (start + time) % positions == 0;
        }
    }

    private static ArrayList<Disc> parse(List<String> input) {

        ArrayList<Disc> discs = new ArrayList<>();

        Pattern pat = Pattern.compile("Disc #(\\d+) has (\\d+) positions; .*? (\\d+).");

        for (String line : input) {

            Matcher m = pat.matcher(line);
            if (m.find()) {
                discs.add(new Disc(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)), Integer.parseInt(m.group(3))));
            } else {
                throw new RuntimeException();
            }
        }

        return discs;
    }

    private static int drop(ArrayList<Disc> discs) {

        int result = 0;

        outer: while (true) {

            int time = result;
            for (Disc d : discs) {
                if (!d.Pass(++time)) {
                    result++;
                    continue outer;
                }
            }

            break;
        }

        return result;
    }
}
