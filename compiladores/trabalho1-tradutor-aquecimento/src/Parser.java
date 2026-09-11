public class Parser {

    private final Scanner scanner;
    private Token current;

    public Parser(Scanner scanner) {
        this.scanner = scanner;
        this.current = scanner.nextToken();
    }

    public void run() {
        expr();
        expectType(TokenType.EOF);
    }

    private void expr() {
        factor();
        while (current.type == TokenType.PLUS || current.type == TokenType.MINUS) {
            if (current.type == TokenType.PLUS) {
                advance();
                factor();
                System.out.println("add");
            } else {
                advance();
                factor();
                System.out.println("sub");
            }
        }
    }

    private void factor() {
        String number = expect(TokenType.NUMBER);
        Integer.parseInt(number);
        System.out.println("push " + number);
    }

    private String expect(TokenType type) {
        expectType(type);
        String text = current.text;
        advance();
        return text;
    }

    private void expectType(TokenType type) {
        if (current.type != type) {
            throw new RuntimeException("Esperado " + type + ", encontrado " + current.type);
        }
    }

    private void advance() {
        current = scanner.nextToken();
    }
}
