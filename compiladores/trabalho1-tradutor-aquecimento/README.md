# Trabalho 1 — Tradutor de Aquecimento

Tradutor de expressões aritméticas e comandos `let`/`print` para notação
pós-fixa (`push`, `pop`, `add`, `sub`, `mul`, `div`, `print`), com um
interpretador que executa essa saída sobre uma pilha e uma tabela de
variáveis.

Implementado seguindo os 8 passos do tutorial "Tradução dirigida por
sintaxe" da disciplina de Compiladores, com suporte adicional a `*` e `/`.

## Estrutura

- `src/TokenType.java`, `src/Token.java` — tipos de token
- `src/Scanner.java` — analisador léxico
- `src/Parser.java` — analisador sintático descendente recursivo (emite a
  tradução em pós-fixa)
- `src/Interpreter.java` — executa a tradução sobre uma pilha e um mapa de
  variáveis
- `src/Main.java` — programa de exemplo

## Gramática

```
stmtList → stmt*
stmt     → 'let' ID '=' expr ';'
         | 'print' expr ';'
expr     → term (('+' | '-') term)*
term     → factor (('*' | '/') factor)*
factor   → NUMBER | ID
```

## Como compilar e executar

```bash
javac -d out src/*.java
java -cp out Main
```

Exemplo de entrada (definido em `Main.java`):

```
let a = 42 + 5; print a + 6; let b = a * 2 - 3; print b / 3;
```
