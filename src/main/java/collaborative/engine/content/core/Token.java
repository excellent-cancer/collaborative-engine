package collaborative.engine.content.core;

import collaborative.engine.content.common.Paragraph;
import pact.annotation.DataStruct;

import java.util.Objects;

/**
 * @author XyParaCrim
 */
@DataStruct
public class Token {
    public final Paragraph paragraph;

    public Token(Paragraph paragraph) {
        this.paragraph = Objects.requireNonNull(paragraph);
    }
}
