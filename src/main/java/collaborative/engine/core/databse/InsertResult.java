package collaborative.engine.core.databse;

import collaborative.engine.core.identify.ObjectId;

/**
 * 文件数据库插入操作的结果信息
 *
 * @author XyParaCrim
 */
public class InsertResult {

    public enum InsertResultTag {
        /**
         * 成功插入文件，很顺利，并未受到任何阻碍
         */
        INSERTED,
        /**
         * 表示很遗憾，这个操作并未实现
         */
        UNSUPPORTED,
        /**
         * 在执行过程中发生了错误
         */
        FAILURE
    }

    private final ObjectId objectId;
    private final InsertResultTag tag;
    private final Exception error;

    public InsertResult(ObjectId objectId, InsertResultTag tag) {
        this(objectId, tag, null);
    }

    public InsertResult(ObjectId objectId, InsertResultTag tag, Exception error) {
        this.objectId = objectId;
        this.tag = tag;
        this.error = error;
    }

    public ObjectId getObjectId() {
        return objectId;
    }

    public InsertResultTag getTag() {
        return tag;
    }

    public Exception getError() {
        return error;
    }

    public static InsertResult inserted(ObjectId objectId) {
        return new InsertResult(objectId, InsertResultTag.INSERTED);
    }

    public static InsertResult unsupported() {
        // 因为这方法在实际过程中，应该很难调用几次，所以就不使用静态变量
        return new InsertResult(null, InsertResultTag.UNSUPPORTED, new UnsupportedOperationException());
    }

    public static InsertResult failure(ObjectId objectId, Exception error) {
        return new InsertResult(objectId, InsertResultTag.FAILURE, error);
    }
}
