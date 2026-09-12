public record Token(TokenType type, String text) {

    @Override
    public String toString() {
        return type + "(" + text + ")";
    }
}
