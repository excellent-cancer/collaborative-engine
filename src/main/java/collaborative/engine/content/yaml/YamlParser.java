package collaborative.engine.content.yaml;

import collaborative.engine.content.core.Token;

import java.util.*;

import static collaborative.engine.content.yaml.YamlToken.YamlTokenKind.*;

public class YamlParser {

    private final YamlScanner yamlScanner;

    private boolean isParsed = false;

    private Token token = null;

    private Map<String, Object> result = new HashMap<>();

    public YamlParser(YamlScanner yamlScanner) {
        this.yamlScanner = yamlScanner;
    }

    private void nextToken() {
        yamlScanner.scan();
        token = yamlScanner.currentToken();
    }

    private void skipComment() {
        do {
            nextToken();
            ;
        } while (token.kind == COMMENT);
    }

    public void parse() {
        while (true) {
            skipComment();

            if (token.kind == NAMED) {
                parseNamed();
                continue;
            }

            if (token.kind == ITEM) {
                parseItem();
                continue;
            }

            if (token.kind == EOF) {
                return;
            }

            throw new RuntimeException();
        }
    }

    private void parseNamed() {
        Token named = token;
        if (named.content.isBlank()) {
            throw new RuntimeException();
        }

        skipComment();
        throwIfNot(SPLIT);

        skipComment();
        if (token.kind == EOF) {
            result.put(named.content, null);
            return;
        }


        if (token.kind == LITERAL) {
            result.put(named.content, token.content);
            return;
        }

        if (token.kind == NAMED) {
            if (token.paragraph.start().column > named.paragraph.start().column) {
                parserSubNamed(named.content);
            } else {
                result.put(named.content, null);
                return;
            }
        }

        if (token.kind == ITEM) {
            // TODO
        }

        throw new RuntimeException();
    }

    private void parserSubNamed(String parent) {
        Token named = token;
        skipComment();
        throwIfNot(SPLIT);
        skipComment();

        if (token.kind == EOF) {
            result.put(parent + "." + named.content, null);
            return;
        }

        if (token.kind == LITERAL) {
            result.put(parent + "." + named.content, token.content);
            return;
        }

        if (token.kind == NAMED) {
            if (token.paragraph.start().column < named.paragraph.start().column) {
                result.put(parent + "." + named.content, null);
                return;
            }

            while (token.paragraph.start().column >= named.paragraph.start().column) {


            }

            if (token.paragraph.start().column > named.paragraph.start().column) {
                parserSubNamed(parent + "." + named.content);
            } else {
                result.put(parent + "." + named.content, null);
            }
        }

    }

    private void throwIfNot(YamlToken.YamlTokenKind kind) {
        if (token.kind != kind) {
            throw new RuntimeException();
        }
    }

    private void parseItem() {

    }
}
