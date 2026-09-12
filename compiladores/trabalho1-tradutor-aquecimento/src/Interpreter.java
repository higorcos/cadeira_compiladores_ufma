import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.IntBinaryOperator;

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
            case "add" -> applyBinary((a, b) -> a + b);
            case "sub" -> applyBinary((a, b) -> a - b);
            case "mul" -> applyBinary((a, b) -> a * b);
            case "div" -> applyBinary((a, b) -> a / b);
            case "print" -> System.out.println(stack.pop());
            default -> throw new RuntimeException("Instrucao desconhecida: " + instruction);
        }
    }

    private void applyBinary(IntBinaryOperator operator) {
        int b = stack.pop();
        int a = stack.pop();
        stack.push(operator.applyAsInt(a, b));
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
