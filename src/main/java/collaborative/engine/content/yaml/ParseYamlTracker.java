package collaborative.engine.content.yaml;

import collaborative.engine.content.core.Token;
import pact.annotation.NotQualified;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import static collaborative.engine.content.yaml.YamlTokenKind.COMMENT;

@NotQualified
class ParseYamlTracker {

    private static final String NULL_VALUE = null;

    private final YamlScanner yamlScanner;

    private final Map<String, Object> flatProperties;

    private final Stack<YamlToken> nestedTokens = new Stack<>();

    private String key = "";

    private YamlToken peerToken = null;

    ParseYamlTracker(YamlScanner yamlScanner) {
        this.yamlScanner = yamlScanner;
        this.flatProperties = new HashMap<>();
    }

    // Control scan flow

    void nextToken() {
        yamlScanner.scan();
    }

    void nextTokenAndSkipComment() {
        do {
            nextToken();
        } while (yamlScanner.currentToken().kind == COMMENT);
    }

    // Get or query current Token

    YamlToken token() {
        return yamlScanner.currentToken();
    }

    YamlTokenKind kind() {
        return yamlScanner.currentToken().kind;
    }

    boolean is(Token.TokenKind kind) {
        return yamlScanner.currentToken().kind == kind;
    }

    // Record message of parse process

    void peer(YamlToken yamlToken) {
        peerToken = yamlToken;
    }

    boolean isPeer(YamlToken yamlToken) {
        return peerToken == null || peerToken.isEqColumn(yamlToken);
    }

    void push(YamlToken yamlToken) {
        nestedTokens.push(yamlToken);
        key += "." + yamlToken.content;
    }

    YamlToken pop() {
        YamlToken token = nestedTokens.pop();
        key = !nestedTokens.empty() ? key.substring(key.length() - token.content.length()) : "";
        return token;
    }

    boolean hasParentNamed() {
        return !nestedTokens.isEmpty();
    }

    // Save key-value

    void save(YamlToken keyToken, Object value) {
        flatProperties.put(nestedTokens.empty() ? keyToken.content : key + "." + keyToken.content, value);
    }

    void save(YamlToken keyToken) {
        save(keyToken, NULL_VALUE);
    }

    Map<String, Object> flat() {
        return flatProperties;
    }

    static ParseYamlTracker makeAndSkip(YamlScanner scanner) {
        ParseYamlTracker parseYamlTracker = new ParseYamlTracker(scanner);
        parseYamlTracker.nextTokenAndSkipComment();
        return parseYamlTracker;
    }
}
