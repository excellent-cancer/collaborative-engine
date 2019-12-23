package collaborative.engine.content.yaml;

import collaborative.engine.content.core.Parse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static collaborative.engine.content.yaml.YamlTokenKind.NAMED;
import static collaborative.engine.content.yaml.YamlTokenKind.SPLIT;

/**
 * @author XyParaCrim
 */
@SuppressWarnings("unused")
public class FlatParseYaml implements Parse<Map<String, Object>, YamlTokenKind, YamlScanner> {

    private static final Logger LOGGER = LogManager.getLogger(FlatParseYaml.class);

    @Override
    public Map<String, Object> resolve(YamlScanner scanner) {
        ParseYamlTracker parseYamlTracker = ParseYamlTracker.makeAndSkip(scanner);

        while (true) {
            switch (parseYamlTracker.kind()) {
                case NAMED:
                    resolveNamed(parseYamlTracker);
                    break;
                case ITEM:
                    resolveItem(parseYamlTracker);
                    break;
                case EOF:
                    return parseYamlTracker.flat();
                default:
                    throw new RuntimeException();
            }
        }
    }

    private void resolveNamed(@NotNull ParseYamlTracker parseYamlTracker) {
        // 不允许键值为空
        YamlToken key = parseYamlTracker.token();
        if (key.content.isBlank()) {
            throw new RuntimeException();
        }

        if (!parseYamlTracker.isPeer(key)) {
            if (parseYamlTracker.hasParentNamed()) {
                parseYamlTracker.peer(parseYamlTracker.pop());
                return;
            } else {
                throw new RuntimeException();
            }
        }

        // NAMED 的下一个token必须是 SPILT
        parseYamlTracker.nextToken();
        if (!parseYamlTracker.is(SPLIT)) {
            throw new RuntimeException();
        }

        // lastNamed 不为空说明这个named是不嵌套的，必须同列
        parseYamlTracker.nextTokenAndSkipComment();
        switch (parseYamlTracker.kind()) {
            case EOF:
                // 允许此时遇到EOF，即将值设为NULL_VALUE
                parseYamlTracker.save(key);
                break;
            case ITEM:
                resolveItem(parseYamlTracker);
                break;
            case LITERAL:
                // 如果是文字的话，则将当前token的内容设为值
                parseYamlTracker.save(key, parseYamlTracker.token().content);
                parseYamlTracker.peer(key);
                parseYamlTracker.nextTokenAndSkipComment();
                return;
            case NAMED:
                // 如果是命名的话，则说明已经到达下一行，可能会是一下几种情况：
                //   1. 与当前key为同级
                //   2. 当前key嵌套
                //   3. 当前key被嵌套
                YamlToken next = parseYamlTracker.token();

                // 不允许两个连续的Named在同一行
                if (next.isEqLine(key)) {
                    throw new RuntimeException();
                }

                if (next.isEqColumn(key)) {
                    // 在这里不继续迭代，因为不清楚到底有多少同级的key
                    // 跳出去进行while循环，然后再次进入这个行数，虽然已经直到必然
                    // 会再次进入此函数
                    parseYamlTracker.save(key);
                    // 特别地，不需要设置lastNamed，因为已经检测过两个named为同一列
                    // tokenizer.lastNamed = key;
                    return;
                }

                if (next.isGtColumn(key)) {
                    // 当前key嵌套
                    do {
                        parseYamlTracker.push(key);
                        resolveNamed(parseYamlTracker);
                    } while (parseYamlTracker.is(NAMED) && key.isLtColumn(parseYamlTracker.token()));

                    parseYamlTracker.pop();
                    parseYamlTracker.peer(key);
                    return;
                }

                if (next.isLtColumn(key)) {
                    if (parseYamlTracker.hasParentNamed()) {
                        parseYamlTracker.save(key, next.content);
                        return;
                    }
                    throw new RuntimeException();
                }
            default:
                throw new RuntimeException();
        }
    }

    private void resolveItem(ParseYamlTracker parseYamlTracker) {
        throw new UnsupportedOperationException();

/*        parseYamlTracker.nextToken();
        switch (parseYamlTracker.kind()) {
            case LITERAL:
                Stream<YamlToken> values = Stream.of(parseYamlTracker.token());

                parseYamlTracker.nextTokenAndSkipComment();

                if (parseYamlTracker.is(ITEM)) {
                    parseYamlTracker.nextToken();
                    if (parseYamlTracker.is(LITERAL)) {


                    } else {
                        throw new RuntimeException();
                    }
                }
                break;
            case NAMED:
                resolveNamed(parseYamlTracker);
                break;




            default:
                throw new RuntimeException();
        }*/
    }
}
