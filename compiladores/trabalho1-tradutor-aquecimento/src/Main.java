import java.util.List;

// Exemplo juntando as 3 pecas, que é basicamente o pipeline de um
// compilador de verdade:
//
// codigo-fonte -[Scanner]-> tokens -[Parser]-> pos-fixa -[Interpreter]-> resultado
//
// Scanner faz a analise lexica (texto vira tokens), Parser faz a
// analise sintatica e já traduz pra pos-fixa, Interpreter executa essa
// traducao. Cada etapa só conhece a saida da etapa anterior — o Parser
// nunca olha o texto original, o Interpreter nunca olha token nem
// gramatica.
public class Main {

    private static final String SOURCE = """
            // exemplo basico
            let a = 42 + 5; print a + 6;
            let b = a * 2 - 3; print b / 3;

            // extensoes: parenteses e menos unario
            let c = (a + b) * 2; print c;
            let d = -a + 10; print d;
            """;

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
