import java.util.ArrayList;
import java.util.List;

public class Parser {

    private final Scanner scanner;
    private final List<String> program = new ArrayList<>();
    private Token current;

    public Parser(Scanner scanner) {
        this.scanner = scanner;
        this.current = scanner.nextToken();
    }

    public List<String> translate() {
        stmtList();
        expectType(TokenType.EOF);
        return program;
    }

    private void stmtList() {
        while (current.type != TokenType.EOF) {
            stmt();
        }
    }

    private void stmt() {
        if (current.type == TokenType.LET) {
            letStmt();
        } else if (current.type == TokenType.PRINT) {
            printStmt();
        } else {
            throw new RuntimeException("Comando invalido, encontrado " + current.type);
        }
    }

    private void letStmt() {
        expect(TokenType.LET);
        String id = expect(TokenType.ID);
        expect(TokenType.EQUALS);
        expr();
        expect(TokenType.SEMICOLON);
        emit("pop " + id);
    }

    private void printStmt() {
        expect(TokenType.PRINT);
        expr();
        expect(TokenType.SEMICOLON);
        emit("print");
    }

    private void expr() {
        term();
        while (current.type == TokenType.PLUS || current.type == TokenType.MINUS) {
            TokenType operator = current.type;
            advance();
            term();
            emit(operator == TokenType.PLUS ? "add" : "sub");
        }
    }

    private void term() {
        factor();
        while (current.type == TokenType.STAR || current.type == TokenType.SLASH) {
            TokenType operator = current.type;
            advance();
            factor();
            emit(operator == TokenType.STAR ? "mul" : "div");
        }
    }

    private void factor() {
        if (current.type == TokenType.NUMBER) {
            String number = expect(TokenType.NUMBER);
            Integer.parseInt(number);
            emit("push " + number);
        } else if (current.type == TokenType.ID) {
            emit("push " + expect(TokenType.ID));
        } else {
            throw new RuntimeException("Esperado NUMBER ou ID, encontrado " + current.type);
        }
    }

    private void emit(String instruction) {
        program.add(instruction);
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
