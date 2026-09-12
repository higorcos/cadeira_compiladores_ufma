import java.util.List;

public class Main {

    private static final String SOURCE = "let a = 42 + 5; print a + 6; let b = a * 2 - 3; print b / 3;";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(SOURCE);
        Parser parser = new Parser(scanner);
        List<String> program = parser.translate();

        System.out.println(String.join("\n", program));
        System.out.println("---");

        Interpreter interpreter = new Interpreter(program);
        interpreter.run();
    }
}
