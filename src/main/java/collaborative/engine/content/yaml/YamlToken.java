package collaborative.engine.content.yaml;

import collaborative.engine.content.common.LineColumn;
import collaborative.engine.content.common.Paragraph;
import collaborative.engine.content.parse.Token;
import pact.annotation.DataStruct;

/**
 * @author XyParaCrim
 */
@DataStruct
public class YamlToken extends Token<YamlTokenKind> {

    public static final YamlToken DUMMY = new YamlToken(Paragraph.identical(LineColumn.ORIGIN), YamlTokenKind.DUMMY);

    private YamlToken(Paragraph paragraph, YamlTokenKind kind) {
        super(paragraph, kind);
    }

    private YamlToken(Paragraph paragraph, YamlTokenKind kind, String content) {
        super(paragraph, kind, content);
    }

    @SuppressWarnings("AlibabaLowerCamelCaseVariableNaming")
    public static YamlToken EOF(LineColumn lineColumn) {
        return new YamlToken(Paragraph.identical(lineColumn), YamlTokenKind.EOF);
    }

    @SuppressWarnings("AlibabaLowerCamelCaseVariableNaming")
    public static YamlToken EOF(Paragraph paragraph) {
        return new YamlToken(paragraph, YamlTokenKind.EOF);
    }

    public static YamlToken comment(Paragraph paragraph) {
        return new YamlToken(paragraph, YamlTokenKind.COMMENT, YamlTokenKind.COMMENT.name);
    }

    public static YamlToken literal(Paragraph paragraph, String content) {
        return new YamlToken(paragraph, YamlTokenKind.LITERAL, content);
    }

    public static YamlToken named(Paragraph paragraph, String content) {
        return new YamlToken(paragraph, YamlTokenKind.NAMED, content);
    }

    public static YamlToken error(Paragraph paragraph) {
        return new YamlToken(paragraph, YamlTokenKind.ERROR);
    }

    public static YamlToken split(Paragraph paragraph) {
        return new YamlToken(paragraph, YamlTokenKind.SPLIT, YamlTokenKind.SPLIT.name);
    }

    public static YamlToken item(Paragraph paragraph) {
        return new YamlToken(paragraph, YamlTokenKind.ITEM, YamlTokenKind.ITEM.name);
    }

}
