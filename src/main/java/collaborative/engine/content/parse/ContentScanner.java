package collaborative.engine.content.parse;

import collaborative.engine.content.common.LineColumn;

/**
 * @author XyParaCrim
 */
public interface ContentScanner<T extends Token.TokenKind> {

    int currentLine();

    int currentColumn();

    Token<T> currentToken();

    void scan();

    boolean scannable();

    default LineColumn lineColumn() {
        return LineColumn.of(currentLine(), currentColumn());
    }
}
