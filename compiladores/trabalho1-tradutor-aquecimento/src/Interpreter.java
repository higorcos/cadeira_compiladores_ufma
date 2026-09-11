import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Interpreter {

    private final List<String> program;
    private final Deque<Integer> stack = new ArrayDeque<>();
    private final Map<String, Integer> variables = new HashMap<>();

    public Interpreter(List<String> program) {
        this.program = program;
    }

    public void run() {
        for (String instruction : program) {
            execute(instruction);
        }
    }

    private void execute(String instruction) {
        String[] parts = instruction.split(" ");
        switch (parts[0]) {
            case "push" -> stack.push(valueOf(parts[1]));
            case "pop" -> variables.put(parts[1], stack.pop());
            case "add" -> {
                int b = stack.pop();
                int a = stack.pop();
                stack.push(a + b);
            }
            case "sub" -> {
                int b = stack.pop();
                int a = stack.pop();
                stack.push(a - b);
            }
            case "mul" -> {
                int b = stack.pop();
                int a = stack.pop();
                stack.push(a * b);
            }
            case "div" -> {
                int b = stack.pop();
                int a = stack.pop();
                stack.push(a / b);
            }
            case "print" -> System.out.println(stack.pop());
            default -> throw new RuntimeException("Instrucao desconhecida: " + instruction);
        }
    }

    private int valueOf(String token) {
        if (Character.isDigit(token.charAt(0))) {
            return Integer.parseInt(token);
        }
        if (!variables.containsKey(token)) {
            throw new RuntimeException("Variavel nao definida: " + token);
        }
        return variables.get(token);
    }
}
