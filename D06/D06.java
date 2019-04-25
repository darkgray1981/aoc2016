package D06;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class D06 {

    public static void main(String[] args) throws IOException {

        long startTime = System.nanoTime();

        List<String> input = Files.readAllLines(Paths.get("input.txt"));
        
        List<String> test = Arrays.asList("eedadn drvtee eandsr raavrd atevrs tsrnev sdttsa rasrtv nssdts ntnada svetve tesnvt vntsnd vrdear dvrsen enarar".split(" "));

        // P1(test); // = "easter"
        P1(input); // = "dzqckwsd"

        // P2(test); // = "advent"
        P2(input); // = "lragovly"

        long endTime = System.nanoTime();
        System.out.println("Time taken: " + (endTime - startTime) / 1e9 + "s");
    }

    private static void P1(List<String> input) {

        ArrayList<HashMap<Character, Integer>> mapList = new ArrayList<>();

        for (int i = 0; i < input.get(0).length(); i++) {
            mapList.add(new HashMap<>());

            for (char c = 'a'; c <= 'z'; c++) {
                mapList.get(i).put(c, 0);
            }
        }

        for (String line : input) {
            for (int i = 0; i < line.length(); i++) {
                char c = line.charAt(i);
                mapList.get(i).put(c, mapList.get(i).get(c) + 1);
            }
        }

        String result = "";

        for (HashMap<Character, Integer> m : mapList) {
            int maxVal = 0;
            char maxChar = '*';

            for (Entry<Character, Integer> e : m.entrySet()) {
                if (e.getValue() > maxVal) {
                    maxVal = e.getValue();
                    maxChar = e.getKey();
                }
            }

            result += maxChar;
        }

        System.out.println("Message: " + result);
    }

    private static void P2(List<String> input) {

        ArrayList<HashMap<Character, Integer>> mapList = new ArrayList<>();

        for (int i = 0; i < input.get(0).length(); i++) {
            mapList.add(new HashMap<>());

            for (char c = 'a'; c <= 'z'; c++) {
                mapList.get(i).put(c, 0);
            }
        }

        for (String line : input) {
            for (int i = 0; i < line.length(); i++) {
                char c = line.charAt(i);
                mapList.get(i).put(c, mapList.get(i).get(c) + 1);
            }
        }

        String result = "";

        for (HashMap<Character, Integer> m : mapList) {
            int minVal = input.size()+1;
            char minChar = '*';

            for (Entry<Character, Integer> e : m.entrySet()) {
                if (e.getValue() > 0 && e.getValue() < minVal) {
                    minVal = e.getValue();
                    minChar = e.getKey();
                }
            }

            result += minChar;
        }

        System.out.println("Real Message: " + result);
    }
}
