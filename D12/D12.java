package D12;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class D12 {

    public static void main(String[] args) throws IOException {

        long startTime = System.nanoTime();

        List<String> input = Files.readAllLines(Paths.get("input.txt"));

        List<String> test1 = Arrays.asList("cpy 41 a\ninc a\ninc a\ndec a\njnz a 2\ndec a".split("\n"));

        // P1(test1); // = 42
        P1(input); // = 318007

        P2(input); // = 9227661

        long endTime = System.nanoTime();
        System.out.println("Time taken: " + (endTime - startTime) / 1e9 + "s");
    }

    private static void P1(List<String> input) {

        ArrayList<Code> program = parse(input);

        int regs[] = new int[4];

        execute(regs, program);

        System.out.println("A Value: " + regs['a' - 'a']);
    }

    private static void P2(List<String> input) {

        ArrayList<Code> program = parse(input);

        int regs[] = new int[4];
        regs['c' - 'a'] = 1;

        execute(regs, program);

        System.out.println("2nd A Value: " + regs['a' - 'a']);
    }

    private enum Operation { CPY, INC, DEC, JNZ }

    private static class Code {
        Operation op;
        Integer xVal;
        char xChar;
        Integer yVal;
        char yChar;

        Code(String op, String x, String y) {

            switch (op) {
                case "cpy":
                    this.op = Operation.CPY;
                    break;
                case "inc":
                    this.op = Operation.INC;
                    break;
                case "dec":
                    this.op = Operation.DEC;
                    break;
                case "jnz":
                    this.op = Operation.JNZ;
                    break;
            }

            if (x.charAt(0) >= 'a' && x.charAt(0) <= 'z') {
                this.xChar = (char)(x.charAt(0) - 'a');
            } else {
                this.xVal = Integer.parseInt(x);
            }

            if (y != null && y.charAt(0) >= 'a' && y.charAt(0) <= 'z') {
                this.yChar = (char)(y.charAt(0) - 'a');
            } else if (y != null) {
                this.yVal = Integer.parseInt(y);
            }
        }
    }

    private static ArrayList<Code> parse(List<String> input) {

        ArrayList<Code> program = new ArrayList<>();

        for (String line : input) {

            String[] op = line.split(" ");
            program.add(new Code(op[0], op[1], (op.length == 2 ? null : op[2])));
        }

        return program;
    }

    private static void execute(int[] regs, ArrayList<Code> program) {

        for (int i = 0; i < program.size(); i++) {

            Code c = program.get(i);

            switch (c.op) {
                case CPY:
                    regs[c.yChar] = (c.xVal != null ? c.xVal : regs[c.xChar]);
                    break;
                case INC:
                    regs[c.xChar]++;
                    break;
                case DEC:
                    regs[c.xChar]--;
                    break;
                case JNZ:
                    int x = (c.xVal != null ? c.xVal : regs[c.xChar]); 
                    if (x != 0) {
                        i += c.yVal - 1;
                    }
                    break;
            }
        }
    }
}
