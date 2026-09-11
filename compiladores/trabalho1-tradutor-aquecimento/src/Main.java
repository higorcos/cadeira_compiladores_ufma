public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner("9 - 5 + 2");
        Parser parser = new Parser(scanner);
        parser.run();
    }
}
