public class Main {

    static String expr;
    static int pos;

    public static void main(String[] args) {
        expr = "9-5+2";
        pos = 0;
        translate();
    }

    static void translate() {
        factor();
        while (pos < expr.length()) {
            char op = expr.charAt(pos);
            if (op == '+') {
                pos++;
                factor();
                System.out.println("add");
            } else if (op == '-') {
                pos++;
                factor();
                System.out.println("sub");
            } else {
                throw new RuntimeException("Esperado '+' ou '-', encontrado: " + op);
            }
        }
    }

    static void factor() {
        char c = expr.charAt(pos);
        if (!Character.isDigit(c)) {
            throw new RuntimeException("Esperado um digito, encontrado: " + c);
        }
        System.out.println("push " + c);
        pos++;
    }
}
