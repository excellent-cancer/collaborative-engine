package collaborative.engine.content.core;

import collaborative.engine.content.common.LineColumn;

/**
 * @author XyParaCrim
 */
public interface ContentScanner {

    int currentLine();

    int currentColumn();

    Token currentToken();

    void scan();

    default LineColumn lineColumn() {
        return LineColumn.of(currentLine(), currentColumn());
    }
}
