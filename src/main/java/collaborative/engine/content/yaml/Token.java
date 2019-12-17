package collaborative.engine.content.yaml;

import pact.annotation.DataStruct;

@DataStruct
public class Token {

    private final TokenKind kind;


    private final int line;

    enum Tag {
        // column

    }

    enum TokenKind {
        KEY,
        COMMENT
    }

    static Token eof() {
        return null;
    }

    static Token comment() {
        return null;
    }

    static Token key(String key) {
        return null;
    }

    static Token value() {
        return null;
    }
}
