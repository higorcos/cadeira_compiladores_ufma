import java.util.List;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner("let a = 42 + 5; print a + 6; let b = a * 2 - 3; print b / 3;");
        Parser parser = new Parser(scanner);
        List<String> program = parser.translate();

        System.out.println(String.join("\n", program));
        System.out.println("---");

        Interpreter interpreter = new Interpreter(program);
        interpreter.run();
    }
}
