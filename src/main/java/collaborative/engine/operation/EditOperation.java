package collaborative.engine.operation;

import collaborative.engine.content.ContentProvider;

import java.util.Objects;

public class EditOperation {
    private Position start;
    private Position end;
    private String[] content;
    private String baseVersion;

    public static EditOperation from(EditOperationRequest request) {
        Objects.requireNonNull(request);
        return null;
    }

    public static EditOperation from(ContentProvider.ContentInfo contentInfo) {
        return null;
    }

    public Position getStart() {
        return start;
    }

    public void setStart(Position start) {
        this.start = start;
    }

    public Position getEnd() {
        return end;
    }

    public void setEnd(Position end) {
        this.end = end;
    }

    public String[] getContent() {
        return content;
    }

    public void setContent(String[] content) {
        this.content = content;
    }

    public String getBaseVersion() {
        return baseVersion;
    }

    public void setBaseVersion(String baseVersion) {
        this.baseVersion = baseVersion;
    }

    public static class Position {
        private int row;
        private int column;
    }
}
