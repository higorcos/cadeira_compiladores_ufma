// Token = par (tipo, texto). O Parser trabalha em cima do "tipo" (ex:
// "agora eu quero um NUMBER") e só olha pro "texto" quando o valor
// realmente importa (montar o "push 42" ou saber o nome da variavel).
//
// Virou um record porque é só isso mesmo: dois campos imutáveis. O Java
// já gera o construtor e os getters (type()/text()) sozinho, só
// precisei sobrescrever o toString pra ficar mais facil de debugar
// (printar um token no meio do código e entender o que é sem esforço).
public record Token(TokenType type, String text) {

    @Override
    public String toString() {
        return type + "(" + text + ")";
    }
}
