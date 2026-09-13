// Scanner = analisador lexico. É a primeira fase do compilador: lê o
// codigo-fonte caractere por caractere e agrupa em tokens, jogando fora
// o que nao importa pra sintaxe (espaco, comentario). O Parser nunca vê
// um caractere sozinho, só a sequência de tokens que sai daqui — por
// isso a gramática lá no Parser fica muito mais simples.
//
// Cada nextToken() devolve um token e já avança a posição. É "sob
// demanda": o Parser só pede o próximo quando precisa, não gera tudo de
// uma vez.
public class Scanner {

    private final String source;
    private int pos;

    public Scanner(String source) {
        this.source = source;
        this.pos = 0;
    }

    public Token nextToken() {
        skipWhitespace();

        // acabou a entrada -> devolve EOF em vez de null/excecao, assim
        // o parser so precisa checar o tipo do token, sem caso especial
        if (pos >= source.length()) {
            return new Token(TokenType.EOF, "");
        }

        char c = source.charAt(pos);

        // digito: pode ter mais de um vindo (123), por isso chama number()
        if (Character.isDigit(c)) {
            return number();
        }

        // letra: pode ser identificador ou palavra reservada, so sei
        // qual dos dois depois de ler a palavra inteira
        if (Character.isLetter(c)) {
            return identifier();
        }

        // simbolos de 1 caractere só, não precisam de método próprio
        if (c == '+') return symbol(TokenType.PLUS, c);
        if (c == '-') return symbol(TokenType.MINUS, c);
        if (c == '*') return symbol(TokenType.STAR, c);
        if (c == '/') return symbol(TokenType.SLASH, c);
        if (c == '=') return symbol(TokenType.EQUALS, c);
        if (c == ';') return symbol(TokenType.SEMICOLON, c);
        if (c == '(') return symbol(TokenType.LPAREN, c);
        if (c == ')') return symbol(TokenType.RPAREN, c);

        throw new RuntimeException("Caractere inesperado: " + c);
    }

    private Token symbol(TokenType type, char c) {
        pos++;
        return new Token(type, String.valueOf(c));
    }

    // regra do "casamento mais longo": nao para no primeiro digito, vai
    // comendo enquanto for digito, senao "123" virava 3 tokens
    private Token number() {
        StringBuilder sb = new StringBuilder();
        while (pos < source.length() && Character.isDigit(source.charAt(pos))) {
            sb.append(source.charAt(pos));
            pos++;
        }
        return new Token(TokenType.NUMBER, sb.toString());
    }

    // le a palavra toda primeiro e só no final decide se é reservada
    // (let/print) ou identificador normal — mais simples que tentar
    // reconhecer "let" letra por letra separado do resto
    private Token identifier() {
        StringBuilder sb = new StringBuilder();
        while (pos < source.length() && Character.isLetterOrDigit(source.charAt(pos))) {
            sb.append(source.charAt(pos));
            pos++;
        }
        String text = sb.toString();
        if (text.equals("let")) {
            return new Token(TokenType.LET, text);
        }
        if (text.equals("print")) {
            return new Token(TokenType.PRINT, text);
        }
        return new Token(TokenType.ID, text);
    }

    // espaco e comentario nao tem significado nenhum pra gramatica, só
    // organizam o codigo pra quem lê — descarta tudo aqui antes de
    // tentar reconhecer o proximo token de verdade
    private void skipWhitespace() {
        while (pos < source.length()) {
            if (Character.isWhitespace(source.charAt(pos))) {
                pos++;
            } else if (isLineCommentStart()) {
                while (pos < source.length() && source.charAt(pos) != '\n') {
                    pos++;
                }
            } else {
                break;
            }
        }
    }

    // '/' sozinho é divisão, mas '//' é comentário — só dá pra saber
    // olhando 1 caractere à frente sem consumir ainda (lookahead)
    private boolean isLineCommentStart() {
        return source.charAt(pos) == '/' && pos + 1 < source.length() && source.charAt(pos + 1) == '/';
    }
}
