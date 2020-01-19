package collaborative.engine.core.databse;

import collaborative.engine.core.identify.ObjectId;

public class RemoveResult {

    public enum RemoveResultTag {
        /**
         * 表示很遗憾，这个操作并未实现
         */
        UNSUPPORTED
    }

    private final ObjectId objectId;
    private final RemoveResultTag tag;
    private final Exception error;

    private RemoveResult(ObjectId objectId, RemoveResultTag tag) {
        this(objectId, tag, null);
    }

    private RemoveResult(ObjectId objectId, RemoveResultTag tag, Exception error) {
        this.objectId = objectId;
        this.tag = tag;
        this.error = error;
    }

    public ObjectId getObjectId() {
        return objectId;
    }

    public RemoveResultTag getTag() {
        return tag;
    }

    public Exception getError() {
        return error;
    }

    public static RemoveResult unsupported() {
        return new RemoveResult(null, RemoveResultTag.UNSUPPORTED, new UnsupportedOperationException());
    }
}
