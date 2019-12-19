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

    private Paragraph(LineColumn same) {
        this.start = this.end = same;
    }

    public LineColumn start() {
        return start;
    }

    public LineColumn end() {
        return end;
    }

    public static Paragraph identical(LineColumn lineColumn) {
        return new Paragraph(Objects.requireNonNull(lineColumn));
    }

    public static Paragraph identical(int line, int column) {
        return new Paragraph(LineColumn.of(line, column));
    }

    public static Paragraph of(LineColumn start, LineColumn end) {
        return new Paragraph(Objects.requireNonNull(start), Objects.requireNonNull(end));
    }
}
