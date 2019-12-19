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

    public final String content;

    public Token(Paragraph paragraph, TokenKind kind) {
        this(paragraph, kind, "");
    }

    public Token(Paragraph paragraph, TokenKind kind, String content) {
        this.paragraph = Objects.requireNonNull(paragraph);
        this.kind = Objects.requireNonNull(kind);
        this.content = content;
    }

    @Marked
    public interface TokenKind {
    }
}
