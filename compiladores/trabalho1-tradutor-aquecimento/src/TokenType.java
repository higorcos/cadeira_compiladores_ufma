// Isso aqui são as "categorias" de token que o Scanner reconhece.
// Lembrar: token != lexema. O lexema é o pedacinho de texto que apareceu
// no código-fonte ("42", "let", "a"...), o token é a categoria dele
// (NUMBER, LET, ID...). Dois lexemas diferentes podem ser o mesmo token
// (10 e 99 são os dois NUMBER), e dois lexemas parecidos (mesma forma:
// sequência de letras) podem virar tokens diferentes ("let" vira LET
// porque é reservada, "a" vira ID porque não é).
public enum TokenType {
    NUMBER,     // 0, 42, 1000...
    ID,         // nome de variavel: a, contador, x1...
    LET,        // palavra reservada, inicia atribuicao
    PRINT,      // palavra reservada, inicia impressao
    PLUS,       // +
    MINUS,      // - (usado tanto pra subtracao quanto pra menos unario)
    STAR,       // *
    SLASH,      // /
    EQUALS,     // =
    SEMICOLON,  // ; -> fim de comando
    LPAREN,     // (
    RPAREN,     // )
    EOF         // nao é um simbolo real, é so um "acabou a entrada" pra
                // o parser nao precisar tratar fim de arquivo como caso
                // especial toda hora
}
