public class Parser {

    private final Scanner scanner;
    private Token current;

    public Parser(Scanner scanner) {
        this.scanner = scanner;
        this.current = scanner.nextToken();
    }

    public void run() {
        expr();
        expect(TokenType.EOF);
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
        expect(TokenType.NUMBER);
        System.out.println("push " + current.text);
        advance();
    }

    private void expect(TokenType type) {
        if (current.type != type) {
            throw new RuntimeException("Esperado " + type + ", encontrado " + current.type);
        }
    }

    private void advance() {
        current = scanner.nextToken();
    }
}
