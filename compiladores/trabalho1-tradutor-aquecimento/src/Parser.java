import java.util.ArrayList;
import java.util.List;

// Parser descendente recursivo com traducao dirigida pela sintaxe.
//
// Gramatica que to seguindo aqui:
//
// stmtList -> stmt*
// stmt     -> 'let' ID '=' expr ';'
//          | 'print' expr ';'
// expr     -> term (('+' | '-') term)*
// term     -> factor (('*' | '/') factor)*
// factor   -> NUMBER | ID | '(' expr ')' | '-' factor
//
// A ideia da descida recursiva: cada nao-terminal vira um metodo
// (stmtList -> stmtList(), expr -> expr()...), e o corpo do metodo
// segue o lado direito da producao — nao-terminal chama outro metodo,
// terminal consome/checa um token. O codigo literalmente tem a cara da
// gramatica.
//
// Por que expr/term/factor separados e nao só um "expr" com tudo junto?
// É assim que a precedencia aparece: term fica "mais fundo" na
// recursao, entao e resolvido primeiro, e por isso * e / grudam mais
// forte nos operandos que + e -.
//
// E ao mesmo tempo que reconhece a estrutura, já vai emitindo a
// traducao (por isso "dirigida pela sintaxe" — a acao semantica de
// cada producao é o emit() correspondente). Nao tem uma fase separada
// de "primeiro reconhece tudo, depois gera codigo".
//
// O parser é preditivo (LL(1)): só olhando o token atual já da pra
// saber que producao seguir, sem nunca ter que voltar atras. Da pra
// ver isso bem claro no stmt() — ver LET ou PRINT já diz tudo.
public class Parser {

    private final Scanner scanner;
    private final List<String> program = new ArrayList<>();
    private Token current;

    public Parser(Scanner scanner) {
        this.scanner = scanner;
        this.current = scanner.nextToken();
    }

    // traduz o programa inteiro e devolve as instrucoes pos-fixas, na
    // ordem que o Interpreter vai executar
    public List<String> translate() {
        stmtList();
        expectType(TokenType.EOF);
        return program;
    }

    // stmtList -> stmt*
    private void stmtList() {
        while (current.type() != TokenType.EOF) {
            stmt();
        }
    }

    // stmt -> 'let' ... | 'print' ...
    // o token atual ja diz sozinho qual caminho seguir
    private void stmt() {
        if (current.type() == TokenType.LET) {
            letStmt();
        } else if (current.type() == TokenType.PRINT) {
            printStmt();
        } else {
            throw new RuntimeException("Comando invalido, encontrado " + current.type());
        }
    }

    // stmt -> 'let' ID '=' expr ';'
    // depois que expr() calcula o valor (fica no topo da pilha em
    // tempo de execucao), emite "pop ID" pra guardar na variavel
    private void letStmt() {
        expect(TokenType.LET);
        String id = expect(TokenType.ID);
        expect(TokenType.EQUALS);
        expr();
        expect(TokenType.SEMICOLON);
        emit("pop " + id);
    }

    // stmt -> 'print' expr ';'
    // depois de calcular expr(), emite "print" pra tirar o valor do
    // topo da pilha e mostrar
    private void printStmt() {
        expect(TokenType.PRINT);
        expr();
        expect(TokenType.SEMICOLON);
        emit("print");
    }

    // expr -> term (('+' | '-') term)*
    // associativo a esquerda: "9 - 5 + 2" vira (9-5)+2, porque cada
    // volta do while ja emite a operacao assim que le o proximo term
    private void expr() {
        term();
        while (current.type() == TokenType.PLUS || current.type() == TokenType.MINUS) {
            TokenType operator = current.type();
            advance();
            term();
            emit(operator == TokenType.PLUS ? "add" : "sub");
        }
    }

    // term -> factor (('*' | '/') factor)*
    // igual ao expr(), mas um nivel mais fundo na gramatica — por isso
    // e resolvido primeiro e da precedencia pro * e / sobre + e -
    private void term() {
        factor();
        while (current.type() == TokenType.STAR || current.type() == TokenType.SLASH) {
            TokenType operator = current.type();
            advance();
            factor();
            emit(operator == TokenType.STAR ? "mul" : "div");
        }
    }

    // factor -> NUMBER | ID | '(' expr ')' | '-' factor
    // o caso '(' expr ')' e o que quebra a precedencia padrao: como
    // chama expr() de novo aqui dentro, tudo que ta entre parenteses
    // vira uma "unidade só" pra quem chamou (term/expr), do mesmo jeito
    // que na matematica normal
    private void factor() {
        if (current.type() == TokenType.MINUS) {
            advance();
            factor();
            emit("neg");
        } else if (current.type() == TokenType.NUMBER) {
            String number = expect(TokenType.NUMBER);
            Integer.parseInt(number);
            emit("push " + number);
        } else if (current.type() == TokenType.ID) {
            emit("push " + expect(TokenType.ID));
        } else if (current.type() == TokenType.LPAREN) {
            advance();
            expr();
            expect(TokenType.RPAREN);
        } else {
            throw new RuntimeException("Esperado NUMBER, ID ou '(', encontrado " + current.type());
        }
    }

    // acao semantica: vai guardando a traducao que ta sendo montada
    private void emit(String instruction) {
        program.add(instruction);
    }

    // "come" o token atual se for do tipo esperado (senao é erro de
    // sintaxe) e ja avanca pro proximo, devolvendo o texto — uso
    // quando o valor do terminal importa (o numero, o nome da variavel)
    private String expect(TokenType type) {
        expectType(type);
        String text = current.text();
        advance();
        return text;
    }

    // só confere o tipo, sem consumir
    private void expectType(TokenType type) {
        if (current.type() != type) {
            throw new RuntimeException("Esperado " + type + ", encontrado " + current.type());
        }
    }

    // pede o proximo token pro scanner (o lookahead que o parser usa)
    private void advance() {
        current = scanner.nextToken();
    }
}
