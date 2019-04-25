package D11;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class D11 {

    public static void main(String[] args) throws IOException {

        long startTime = System.nanoTime();

        List<String> input = Files.readAllLines(Paths.get("input.txt"));

        List<String> test1 = Arrays.asList("The first floor contains a hydrogen-compatible microchip and a lithium-compatible microchip.\nThe second floor contains a hydrogen generator.\nThe third floor contains a lithium generator.\nThe fourth floor contains nothing relevant.".split("\n"));

        // P1(test1); // = 11
        P1(input); // = 33

        P2(input); // = 57

        long endTime = System.nanoTime();
        System.out.println("Time taken: " + (endTime - startTime) / 1e9 + "s");
    }

    private static void P1(List<String> input) {

        State s = parse(input);

        int result = transfer(s);

        System.out.println("Steps: " + result);
    }

    private static void P2(List<String> input) {

        State s = parse(input);

        s.generators.get(0).add("elerium");
        s.microchips.get(0).add("elerium");
        s.generators.get(0).add("dilithium");
        s.microchips.get(0).add("dilithium");
        s.items += 4;

        int result = transfer(s);

        System.out.println("More steps: " + result);
    }

    private static final int FLOORS = 4;
    private static final int TOP_FLOOR = 3;
    private static final int BOTTOM_FLOOR = 0;

    private static class State {
        int elevator;
        int steps;
        int items;

        ArrayList<TreeSet<String>> generators;
        ArrayList<TreeSet<String>> microchips;

        State() {
            this.elevator = BOTTOM_FLOOR;
            this.steps = 0;
            this.items = 0;

            this.generators = new ArrayList<>(FLOORS);
            this.microchips = new ArrayList<>(FLOORS);
        }

        State(State o) {
            this.elevator = o.elevator;
            this.steps = o.steps;
            this.items = o.items;

            this.generators = new ArrayList<>(FLOORS);
            for (TreeSet<String> s : o.generators) {
                this.generators.add(new TreeSet<>(s));
            }

            this.microchips = new ArrayList<>(FLOORS);
            for (TreeSet<String> s : o.microchips) {
                this.microchips.add(new TreeSet<>(s));
            }
        }

        boolean Done() {
            return (this.elevator == TOP_FLOOR
                    && this.items == (this.generators.get(TOP_FLOOR).size() + this.microchips.get(TOP_FLOOR).size()));
        }

        boolean Safe() {

            for (int i = 0; i < FLOORS; i++) {
                if (this.generators.get(i).size() == 0) {
                    continue;
                } else if (this.elevator == i && this.microchips.get(i).size() == 0) {
                    return false;
                }

                for (String chip : this.microchips.get(i)) {
                    if (!this.generators.get(i).contains(chip)) {
                        return false;
                    }
                }
            }

            return true;
        }

        public String toString() {

            StringBuilder sb = new StringBuilder();

            for (int i = TOP_FLOOR; i >= 0; i--) {
                sb.append("F" + (i+1) + ": " + (this.elevator == i ? "E" : " "));

                for (String gen : this.generators.get(i)) {
                    sb.append(" " + gen);
                }

                sb.append(" ::");

                for (String chip : this.microchips.get(i)) {
                    sb.append(" " + chip);
                }

                sb.append("\n");
            }

            return sb.toString();
        }
    }

    private static State parse(List<String> input) {

        State original = new State();

        Pattern genPattern = Pattern.compile("(\\w+) generator");
        Pattern chipPattern = Pattern.compile("(\\w+)-compatible microchip");

        for (String line : input) {

            TreeSet<String> generators = new TreeSet<>();
            TreeSet<String> microchips = new TreeSet<>();

            Matcher m = genPattern.matcher(line);
            while (m.find()) {
                generators.add(m.group(1));
                original.items++;
            }

            m = chipPattern.matcher(line);
            while (m.find()) {
                microchips.add(m.group(1));
                original.items++;
            }

            original.generators.add(generators);
            original.microchips.add(microchips);
        }

        return original;
    }

    private static int transfer(State original) {

        HashSet<String> seen = new HashSet<>();
        seen.add(original.toString());

        ArrayDeque<State> queue = new ArrayDeque<>();
        queue.add(original);

        int[] stairs = { -1, 1 };
        int result = 0;

        while (!queue.isEmpty()) {

            State s = queue.remove();
            if (s.Done()) {
                result = s.steps;
                break;
            }

            s.steps++;

            // Bring one/two generators up/downstairs
            for (String genA : s.generators.get(s.elevator)) {

                for (String genB : s.generators.get(s.elevator).tailSet(genA, true)) {
                    for (int dir : stairs) {
                        if (s.elevator + dir < BOTTOM_FLOOR || s.elevator + dir > TOP_FLOOR) {
                            continue;
                        }

                        State updown = new State(s);
                        updown.generators.get(updown.elevator).remove(genA);
                        updown.generators.get(updown.elevator).remove(genB);
                        updown.elevator += dir;
                        updown.generators.get(updown.elevator).add(genA);
                        updown.generators.get(updown.elevator).add(genB);
                        if (updown.Safe()) {
                            String hash = updown.toString();
                            if (!seen.contains(hash)) {
                                queue.add(updown);
                                seen.add(hash);
                            }
                        }
                    }
                }

                // Bring one generator and one microchip up/downstairs
                for (String chip : s.microchips.get(s.elevator)) {
                    for (int dir : stairs) {
                        if (s.elevator + dir < BOTTOM_FLOOR || s.elevator + dir > TOP_FLOOR) {
                            continue;
                        }

                        State updown = new State(s);
                        updown.generators.get(updown.elevator).remove(genA);
                        updown.microchips.get(updown.elevator).remove(chip);
                        updown.elevator += dir;
                        updown.generators.get(updown.elevator).add(genA);
                        updown.microchips.get(updown.elevator).add(chip);
                        if (updown.Safe()) {
                            String hash = updown.toString();
                            if (!seen.contains(hash)) {
                                queue.add(updown);
                                seen.add(hash);
                            }
                        }
                    }
                }
            }

            // Bring one/two microchips up/downstairs
            for (String chipA : s.microchips.get(s.elevator)) {
                for (String chipB : s.microchips.get(s.elevator).tailSet(chipA, true)) {
                    for (int dir : stairs) {
                        if (s.elevator + dir < BOTTOM_FLOOR || s.elevator + dir > TOP_FLOOR) {
                            continue;
                        }

                        State updown = new State(s);
                        updown.microchips.get(updown.elevator).remove(chipA);
                        updown.microchips.get(updown.elevator).remove(chipB);
                        updown.elevator += dir;
                        updown.microchips.get(updown.elevator).add(chipA);
                        updown.microchips.get(updown.elevator).add(chipB);
                        if (updown.Safe()) {
                            String hash = updown.toString();
                            if (!seen.contains(hash)) {
                                queue.add(updown);
                                seen.add(hash);
                            }
                        }
                    }
                }
            }
        }

        return result;
    }
}
