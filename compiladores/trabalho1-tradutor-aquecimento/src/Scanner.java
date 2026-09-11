public class Scanner {

    private final String source;
    private int pos;

    public Scanner(String source) {
        this.source = source;
        this.pos = 0;
    }

    public Token nextToken() {
        skipWhitespace();

        if (pos >= source.length()) {
            return new Token(TokenType.EOF, "");
        }

        char c = source.charAt(pos);

        if (Character.isDigit(c)) {
            return number();
        }

        if (Character.isLetter(c)) {
            return identifier();
        }

        if (c == '+') {
            pos++;
            return new Token(TokenType.PLUS, "+");
        }

        if (c == '-') {
            pos++;
            return new Token(TokenType.MINUS, "-");
        }

        if (c == '*') {
            pos++;
            return new Token(TokenType.STAR, "*");
        }

        if (c == '/') {
            pos++;
            return new Token(TokenType.SLASH, "/");
        }

        if (c == '=') {
            pos++;
            return new Token(TokenType.EQUALS, "=");
        }

        if (c == ';') {
            pos++;
            return new Token(TokenType.SEMICOLON, ";");
        }

        throw new RuntimeException("Caractere inesperado: " + c);
    }

    private Token number() {
        StringBuilder sb = new StringBuilder();
        while (pos < source.length() && Character.isDigit(source.charAt(pos))) {
            sb.append(source.charAt(pos));
            pos++;
        }
        return new Token(TokenType.NUMBER, sb.toString());
    }

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

    private void skipWhitespace() {
        while (pos < source.length() && Character.isWhitespace(source.charAt(pos))) {
            pos++;
        }
    }
}
