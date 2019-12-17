package collaborative.engine.content.common;

/**
 * @author XyParaCrim
 */
public class Paragraph {

    private final LineColumn start;

    private final LineColumn end;

    public Paragraph(LineColumn start, LineColumn end) {
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
}
