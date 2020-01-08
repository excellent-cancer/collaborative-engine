package collaborative.engine.content.parse;

import collaborative.engine.content.common.Paragraph;
import pact.annotation.DataStruct;
import pact.annotation.Marked;

import java.util.Objects;

/**
 * @author XyParaCrim
 */
@DataStruct
public class Token<T extends Token.TokenKind> {

    public final Paragraph paragraph;

    public final T kind;

    public final String content;

    public Token(Paragraph paragraph, T kind) {
        this(paragraph, kind, "");
    }

    public Token(Paragraph paragraph, T kind, String content) {
        this.paragraph = Objects.requireNonNull(paragraph);
        this.kind = Objects.requireNonNull(kind);
        this.content = content;
    }

    public boolean isEqLine(Token<T> another) {
        return paragraph.start().line == another.paragraph.start().line;
    }

    public boolean isEqColumn(Token<T> another) {
        return paragraph.start().column == another.paragraph.start().column;
    }

    public boolean isLtColumn(Token<T> another) {
        return paragraph.start().column < another.paragraph.start().column;
    }

    public boolean isGtColumn(Token<T> another) {
        return paragraph.start().column > another.paragraph.start().column;
    }

    @Marked
    public interface TokenKind {
    }
}
