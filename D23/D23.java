package D23;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class D23 {

    public static void main(String[] args) throws IOException {

        long startTime = System.nanoTime();

        List<String> input = Files.readAllLines(Paths.get("input.txt"));

        List<String> test1 = Arrays.asList("cpy 2 a\ntgl a\ntgl a\ntgl a\ncpy 1 a\ndec a\ndec a".split("\n"));

        // P1(test1, 7); // = 3
        P1(input, 7); // = 10953

        P2(input, 12); // = 479007513

        long endTime = System.nanoTime();
        System.out.println("Time taken: " + (endTime - startTime) / 1e9 + "s");
    }

    private static void P1(List<String> input, int initial) {

        ArrayList<Code> program = parse(input);

        int regs[] = new int[4];
        regs['a' - 'a'] = initial;

        execute(regs, program);

        System.out.println("A Value: " + regs['a' - 'a']);
    }

    private static void P2(List<String> input, int initial) {

        ArrayList<Code> program = parse(input);

        int result = 1;
        for (int i = 2; i <= initial; i++) {
            result *= i;
        }

        result += program.get(19).xVal * program.get(20).xVal;

        System.out.println("2nd A Value: " + result);
    }

    private enum Operation { CPY, INC, DEC, JNZ, TGL }

    private static class Code {
        Operation op;
        Integer xVal;
        char xChar;
        Integer yVal;
        char yChar;
        boolean skip;

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
                case "tgl":
                    this.op = Operation.TGL;
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

        void toggle() {

            switch (this.op) {
                case CPY:
                    this.op = Operation.JNZ;
                    skip = false;
                    break;
                case INC:
                    this.op = Operation.DEC;
                    break;
                case DEC:
                    this.op = Operation.INC;
                    break;
                case JNZ:
                    this.op = Operation.CPY;
                    if (this.yVal != null) {
                        skip = true;
                    }
                    break;
                case TGL:
                    this.op = Operation.INC;
                    break;
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

            if (c.skip) {
                continue;
            }

            int x, y;
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
                    x = (c.xVal != null ? c.xVal : regs[c.xChar]);
                    y = (c.yVal != null ? c.yVal : regs[c.yChar]);
                    if (x != 0) {
                        i += y - 1;
                    }
                    break;
                case TGL:
                    x = (c.xVal != null ? c.xVal : regs[c.xChar]);
                    if (i + x >= 0 && i + x < program.size()) {
                        program.get(i + x).toggle();
                    }
                    break;
            }
        }
    }
}
