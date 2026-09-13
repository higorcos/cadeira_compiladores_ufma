import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.IntBinaryOperator;

// Executa a traducao pos-fixa (RPN) que o Parser gerou, usando uma
// maquina de pilha.
//
// Lembrar: pos-fixa poe o operador depois dos operandos ("2 3 +" em vez
// de "2 + 3"). A sacada é que isso já resolve precedencia e parenteses
// la na traducao — a ordem das instrucoes sozinha ja diz a ordem certa
// de avaliar. Por isso aqui nao preciso saber nada de gramatica ou
// precedencia, só processo a lista de instrucoes numa passada só, da
// esquerda pra direita.
//
// O mecanismo sempre se repete:
// - "push X" empilha um valor (numero ou variavel)
// - um operador desempilha o que precisa, calcula e empilha o resultado
// - "pop X" desempilha o topo e guarda na variavel X
// - "print" desempilha o topo e mostra
//
// No fim de uma expressao completa sobra só o resultado, sozinho no
// topo da pilha.
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
            case "div" -> applyBinary((a, b) -> {
                // essa checagem aqui NAO é igual ao parseInt la no
                // Parser.factor() (que nunca falha de verdade). Divisao
                // por zero só existe em tempo de execucao, depende do
                // valor da variavel — o parser nao tem como prever isso
                // só olhando a estrutura da expressao
                if (b == 0) {
                    throw new RuntimeException("Divisao por zero");
                }
                return a / b;
            });
            case "neg" -> stack.push(-stack.pop());
            case "print" -> System.out.println(stack.pop());
            default -> throw new RuntimeException("Instrucao desconhecida: " + instruction);
        }
    }

    // padrao repetido em add/sub/mul/div: desempilha 2 valores — o "b"
    // foi o ultimo que entrou — aplica a operacao e empilha o
    // resultado. A ordem "b antes de a" importa pq sub e div nao sao
    // comutativas (5 - 2 != 2 - 5)
    private void applyBinary(IntBinaryOperator operator) {
        int b = stack.pop();
        int a = stack.pop();
        stack.push(operator.applyAsInt(a, b));
    }

    // o "push" pode vir com um numero literal ou o nome de uma
    // variavel ja definida — decido qual é so olhando se comeca com
    // digito
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
