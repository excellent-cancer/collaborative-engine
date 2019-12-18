package collaborative.engine.content.core;

import collaborative.engine.content.common.Paragraph;
import pact.annotation.DataStruct;
import pact.annotation.Marked;

import java.util.Objects;

/**
 * @author XyParaCrim
 */
@DataStruct
public class Token {
    public final Paragraph paragraph;

    public final TokenKind kind;

    public Token(Paragraph paragraph, TokenKind kind) {
        this.kind = Objects.requireNonNull(kind);
        this.paragraph = Objects.requireNonNull(paragraph);
    }

    @Marked
    public interface TokenKind {
    }
}
