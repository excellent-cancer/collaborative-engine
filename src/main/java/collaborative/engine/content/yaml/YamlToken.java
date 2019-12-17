package collaborative.engine.content.yaml;

import collaborative.engine.content.common.Paragraph;
import collaborative.engine.content.core.Token;

public class YamlToken extends Token {

    public YamlToken(Paragraph paragraph) {
        super(paragraph);
    }

    @SuppressWarnings("AlibabaLowerCamelCaseVariableNaming")
    public static YamlToken EOF() {
        return null;
    }

    public static YamlToken comment() {
        return null;
    }

    public static YamlToken key(String key) {
        return null;
    }

    public static YamlToken value(String value) {
        return null;
    }
}
