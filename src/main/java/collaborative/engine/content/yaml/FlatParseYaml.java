package collaborative.engine.content.yaml;

import collaborative.engine.content.core.Parse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import static collaborative.engine.content.core.Token.TokenKind;
import static collaborative.engine.content.yaml.YamlTokenKind.*;

public class FlatParseYaml implements Parse<Map<String, Object>, YamlTokenKind, YamlScanner> {

    private static final Logger LOGGER = LogManager.getLogger(FlatParseYaml.class);

    private static final String NULL_VALUE = null;

    @Override
    public Map<String, Object> resolve(YamlScanner scanner) {
        Tokenizer tokenizer = new Tokenizer(scanner);

        tokenizer.nextTokenAndSkipComment();

        while (true) {
            switch (tokenizer.kind()) {
                case NAMED:
                    resolveNamed(tokenizer);
                    break;
                case ITEM:
                    resolveItem(tokenizer);
                    break;
                case EOF:
                    return tokenizer.flatProperties;
                default:
                    throw new RuntimeException();
            }
        }
    }

    private void resolveNamed(Tokenizer tokenizer) {
        // 不允许键值为空
        YamlToken key = tokenizer.token();
        if (key.content.isBlank()) {
            throw new RuntimeException();
        }

        if (!tokenizer.eqColumnPeer(key)) {
            if (tokenizer.nested.isEmpty()) {
                throw new RuntimeException();
            } else {
                tokenizer.peer(tokenizer.nested.pop());
                return;
            }
        }

        // NAMED 的下一个token必须是 SPILT
        tokenizer.nextToken();
        if (!tokenizer.is(SPLIT)) {
            throw new RuntimeException();
        }

        // lastNamed 不为空说明这个named是不嵌套的，必须同列
        tokenizer.nextTokenAndSkipComment();
        switch (tokenizer.kind()) {
            case EOF:
                // 允许此时遇到EOF，即将值设为NULL_VALUE
                tokenizer.flatProperties.put(key.content, NULL_VALUE);
                break;
            case ITEM:
                // TODO
                break;
            case LITERAL:
                // 如果是文字的话，则将当前token的内容设为值
                tokenizer.flatProperties.put(key.content, tokenizer.token().content);
                tokenizer.peer(key);
                tokenizer.nextTokenAndSkipComment();
                return;
            case NAMED:
                // 如果是命名的话，则说明已经到达下一行，可能会是一下几种情况：
                //   1. 与当前key为同级
                //   2. 当前key嵌套
                //   3. 当前key被嵌套
                YamlToken next = tokenizer.token();

                // 不允许两个连续的Named在同一行
                if (next.isEqLine(key)) {
                    throw new RuntimeException();
                }

                if (next.isEqColumn(key)) {
                    // 在这里不继续迭代，因为不清楚到底有多少同级的key
                    // 跳出去进行while循环，然后再次进入这个行数，虽然已经直到必然
                    // 会再次进入此函数
                    tokenizer.flatProperties.put(key.content, NULL_VALUE);
                    // 特别地，不需要设置lastNamed，因为已经检测过两个named为同一列
                    // tokenizer.lastNamed = key;
                    return;
                }

                if (next.isGtColumn(key)) {
                    // 当前key嵌套
                    do {
                        tokenizer.nested.push(key);
                        resolveNamed(tokenizer);
                    } while (tokenizer.is(NAMED) && key.isLtColumn(tokenizer.token()));

                    tokenizer.nested.pop();
                    tokenizer.peer(key);
                    return;
                }

                if (next.isLtColumn(key)) {
                    if (tokenizer.nested.isEmpty()) {
                        throw new RuntimeException();
                    }
                    tokenizer.flatProperties.put(key.content, next.content);
                    return;
                }
            default:
                break;
        }
    }

    private void resolveItem(Tokenizer tokenizer) {
        throw new UnsupportedOperationException();
    }


    private static class Tokenizer {

        final YamlScanner yamlScanner;

        final Map<String, Object> flatProperties = new HashMap<>();

        final Stack<YamlToken> nested = new Stack<>();

        YamlToken peerToken = null;

        Tokenizer(YamlScanner yamlScanner) {
            this.yamlScanner = yamlScanner;
        }

        void nextToken() {
            yamlScanner.scan();
        }

        void nextTokenAndSkipComment() {
            do {
                nextToken();
            } while (yamlScanner.currentToken().kind == COMMENT);
        }

        YamlToken token() {
            return yamlScanner.currentToken();
        }

        YamlTokenKind kind() {
            return yamlScanner.currentToken().kind;
        }

        boolean is(TokenKind kind) {
            return yamlScanner.currentToken().kind == kind;
        }

        void peer(YamlToken yamlToken) {
            peerToken = yamlToken;
        }

        boolean eqColumnPeer(YamlToken yamlToken) {
            return peerToken == null || peerToken.isEqColumn(yamlToken);
        }
    }
}
