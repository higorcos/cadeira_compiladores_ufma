import java.util.List;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner("let a = 42 + 5; print a + 6;");
        Parser parser = new Parser(scanner);
        List<String> program = parser.translate();

        for (String instruction : program) {
            System.out.println(instruction);
        }
        System.out.println("---");

        Interpreter interpreter = new Interpreter(program);
        interpreter.run();
    }
}
