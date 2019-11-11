package collaborative.engine.operation;

import collaborative.engine.content.ContentProvider;

import java.util.Objects;

public class EditOperation {
    private Position start;
    private Position end;
    private String content;

    public static EditOperation from(EditOperationRequest request) {
        Objects.requireNonNull(request);
        return null;
    }

    public static EditOperation from(ContentProvider.ContentInfo contentInfo) {
        return null;
    }

    public static class Position {
        private int row;
        private int column;
    }
}
