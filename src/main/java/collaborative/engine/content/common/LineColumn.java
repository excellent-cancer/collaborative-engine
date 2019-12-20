package collaborative.engine.content.common;

import org.jetbrains.annotations.NotNull;
import pact.annotation.DataStruct;

/**
 * @author XyParaCrim
 */
@DataStruct
public class LineColumn implements Comparable<LineColumn> {
    /**  */
    public final int line;

    /**  */
    public final int column;

    private LineColumn(int line, int column) {
        this.line = line;
        this.column = column;
    }

    @Override
    public int compareTo(@NotNull LineColumn o) {
        int compared = Integer.compare(line, o.line);
        return compared != 0 ? Integer.compare(column, o.column) : compared;
    }

    public LineColumn previousColumn() {
        return new LineColumn(line, column - 1);
    }

    @Override
    public boolean equals(Object obj) {
        if (super.equals(obj)) {
            return true;
        }

        if (obj instanceof LineColumn) {
            LineColumn another = (LineColumn) obj;

            return another.line == line && another.column == column;
        }

        return false;
    }

    public LineColumn copy() {
        return new LineColumn(line, column);
    }

    public static LineColumn ofIndex(int lineIndex, int columnIndex) {
        return new LineColumn(lineIndex + 1, columnIndex + 1);
    }

    public static LineColumn of(int line, int column) {
        return new LineColumn(line, column);
    }

    public static final LineColumn ORIGIN = new LineColumn(0, 0);
}
