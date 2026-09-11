public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner("let a = 42 + 5; let b = a - 3;");
        Parser parser = new Parser(scanner);
        parser.run();
    }
}
