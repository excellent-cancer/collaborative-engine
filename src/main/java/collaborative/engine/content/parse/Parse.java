package collaborative.engine.content.parse;

@FunctionalInterface
public interface Parse<T, S extends Token.TokenKind, V extends ContentScanner<S>> {

    T resolve(V scanner);

}
