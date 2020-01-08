package collaborative.engine.content.yaml;

import collaborative.engine.content.parse.Token;
import pact.annotation.DataStruct;

/**
 * This enum defines partial token used by the Yaml scanner. Similar
 * to Token#TokenKind from sun-tools.
 */
@DataStruct
public enum YamlTokenKind implements Token.TokenKind {
    /**
     * virtual token or origin line-column
     */
    DUMMY(),
    /**
     * end of yaml content
     */
    EOF(),
    /**
     * where the lexical error first appeared in the text
     */
    ERROR(),
    /**
     * line comment
     */
    COMMENT("#", '#'),
    /**
     * key delimiter
     */
    NAMED(),
    /**
     * consecutive reasonable strings in text
     */
    LITERAL(),
    /**
     * value delimiter
     */
    SPLIT(":", ':'),
    /**
     * the beginning of an array item
     */
    ITEM("-", '-');

    public final String name;

    public Character sign;

    YamlTokenKind() {
        this(null, null);
    }

    YamlTokenKind(String name, Character sign) {
        this.name = name;
        this.sign = sign;
    }
}
