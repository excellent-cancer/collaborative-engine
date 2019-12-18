package collaborative.engine.content.common;

import java.util.Objects;

/**
 * @author XyParaCrim
 */
public class Paragraph {

    private final LineColumn start;

    private final LineColumn end;

    private Paragraph(LineColumn start, LineColumn end) {
        if (start.compareTo(end) > 0) {
            throw new IllegalArgumentException(start + ">" + end);
        }
        this.start = start;
        this.end = end;
    }

    public LineColumn startLineColumn() {
        return start;
    }

    public LineColumn endLineColumn() {
        return end;
    }

    public static Paragraph identical(LineColumn lineColumn) {
        return new Paragraph(Objects.requireNonNull(lineColumn), lineColumn.copy());
    }

    public static Paragraph of(LineColumn start, LineColumn end) {
        return new Paragraph(Objects.requireNonNull(start), Objects.requireNonNull(end));
    }
}
