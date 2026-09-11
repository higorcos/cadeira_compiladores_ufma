public class ScannerDemo {

    public static void main(String[] args) {
        Scanner scanner = new Scanner("9 - 5 + 2");
        Token token;
        do {
            token = scanner.nextToken();
            System.out.println(token);
        } while (token.type != TokenType.EOF);
    }
}
