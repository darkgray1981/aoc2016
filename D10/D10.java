package D10;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayDeque;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class D10 {

    public static void main(String[] args) throws IOException {

        long startTime = System.nanoTime();

        List<String> input = Files.readAllLines(Paths.get("input.txt"));

        P1(input); // = 116

        P2(input); // = 23903

        long endTime = System.nanoTime();
        System.out.println("Time taken: " + (endTime - startTime) / 1e9 + "s");
    }

    private static void P1(List<String> input) {

        HashMap<Integer, Bot> bots = new HashMap<>();
        HashMap<Integer, Integer> outputs = new HashMap<>();

        process(input, bots, outputs);

        int result = 0;

        for (Entry<Integer, Bot> e : bots.entrySet()) {
            Bot b = e.getValue();
            if (b.High() == 61 && b.Low() == 17) {
                result = e.getKey();
                break;
            }
        }

        System.out.println("Bot ID: " + result);
    }

    private static void P2(List<String> input) {

        HashMap<Integer, Bot> bots = new HashMap<>();
        HashMap<Integer, Integer> outputs = new HashMap<>();

        process(input, bots, outputs);

        int result = outputs.get(0) * outputs.get(1) * outputs.get(2);

        System.out.println("Product: " + result);
    }

    static class Bot {
        public String lowType;
        public int lowId;

        public String highType;
        public int highId;

        public Integer a;
        public Integer b;

        public Bot(String lowType, int lowId, String highType, int highId) {
            this.lowType = lowType;
            this.lowId = lowId;

            this.highType = highType;
            this.highId = highId;
        }

        public void Add(int value) {
            if (a == null) {
                a = value;
            } else if (b == null) {
                b = value;
            }
        }

        public boolean Ready() {
            return a != null && b != null;
        }

        public Integer Low() {
            if (a <= b) {
                return a;
            }
            return b;
        }

        public Integer High() {
            if (a > b) {
                return a;
            }
            return b;
        }
    }

    private static void process(List<String> input, HashMap<Integer, Bot> bots, HashMap<Integer, Integer> outputs) {

        class Value {
            String type;
            int id;
            int value;

            Value(String type, int id, int value) {
                this.type = type;
                this.id = id;
                this.value = value;
            }
        }

        ArrayDeque<Value> values = new ArrayDeque<>();

        Pattern valuePattern = Pattern.compile("value (\\d+) goes to bot (\\d+)");
        Pattern botPattern = Pattern.compile("bot (\\d+) gives low to (\\w+) (\\d+) and high to (\\w+) (\\d+)");

        for (String line : input) {

            Matcher m = valuePattern.matcher(line);
            if (m.find()) {
                int val = Integer.parseInt(m.group(1));
                int id = Integer.parseInt(m.group(2));

                values.push(new Value("bot", id, val));
                
            } else {
                m = botPattern.matcher(line);
                m.find();

                int botId = Integer.parseInt(m.group(1));
                String lowType = m.group(2);
                int lowId = Integer.parseInt(m.group(3));
                String highType = m.group(4);
                int highId = Integer.parseInt(m.group(5));

                bots.put(botId, new Bot(lowType, lowId, highType, highId));
            }
        }

        while (!values.isEmpty()) {

            Value v = values.poll();

            if (v.type.equals("output")) {

                outputs.put(v.id, v.value);

            } else {

                Bot bot = bots.get(v.id);
                bot.Add(v.value);

                if (bot.Ready()) {
                    values.push(new Value(bot.lowType, bot.lowId, bot.Low()));
                    values.push(new Value(bot.highType, bot.highId, bot.High()));
                }
            }
        }
    }
}
