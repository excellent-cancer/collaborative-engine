package collaborative.engine.core.databse;

import collaborative.engine.core.identify.ObjectId;

public class RemoveResult {

    public enum RemoveResultTag {
        /**
         * 表示很遗憾，这个操作并未实现
         */
        UNSUPPORTED,
        /**
         * 表示这个文件并不存在
         */
        ABSENT,
        /**
         * 这个资源正在忙碌中
         */
        BUSY,
        /**
         * 表示这个资源已经删除了
         */
        REMOVED,
        /**
         * 不知道什么原因，像是删除，但是实际在操作的线程中并没有删除
         */
        LIKE_REMOVED,
        /**
         * 在执行过程中发生了错误
         */
        FAILURE
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

    public static RemoveResult absent(ObjectId objectId) {
        return new RemoveResult(objectId, RemoveResultTag.ABSENT);
    }

    public static RemoveResult busy(ObjectId objectId) {
        return new RemoveResult(objectId, RemoveResultTag.BUSY);
    }

    public static RemoveResult removed(ObjectId objectId) {
        return new RemoveResult(objectId, RemoveResultTag.REMOVED);
    }

    public static RemoveResult likeRemoved(ObjectId objectId) {
        return new RemoveResult(objectId, RemoveResultTag.LIKE_REMOVED);
    }

    public static RemoveResult failure(ObjectId objectId, Exception error) {
        return new RemoveResult(objectId, RemoveResultTag.FAILURE, error);
    }
}
