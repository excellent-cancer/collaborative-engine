package collaborative.engine.core.identify;


import java.util.Objects;
import java.util.function.Predicate;

/**
 * 帮助和解释方法。
 *
 * @author XyParaCrim
 */
public final class IdentifyUtil {

    // 相关具体数据常量

    /**
     * 当objectId出现重复时，重新尝试的次数
     */
    private static final int TRY_STRICTLY_COUNT = 0;

    // 字面意思解释一些小步骤在干什么

    /**
     * 查询objectId是否存在于缓存中
     *
     * @param objectId 指定的objectId
     * @return 是否存在于缓存中
     */
    public static boolean hasBeenCached(ObjectId objectId) {
        return objectId != null && objectId.inCache();
    }

    /**
     * 查询指定的objectId文件是否存在于文件系统中，若存在则随便使其进入缓存中。
     *
     * @param objectId 指定的objectId
     * @return 是否已经创建于文件系统中
     */
    public static boolean hasBeenCreatedWithCache(ObjectId objectId) {
        if (objectId != null && objectId.location().exists()) {
            objectId.cache();
            return true;
        }

        return false;
    }

    /**
     * 返回一个未使用的objectId，如果无法创建则会抛出相关的致命错误
     *
     * @param identifier objectId生产器
     * @param contains   未使用的测试方/法
     * @return 未使用的objectId
     */
    public static ObjectId newStrictObject(Identifier identifier, Predicate<ObjectId> contains) {
        Objects.requireNonNull(identifier);
        Objects.requireNonNull(contains);
        ObjectId objectId;
        for (int count = TRY_STRICTLY_COUNT; count > -1; count--) {
            objectId = identifier.newId();
            if (!contains.test(objectId)) {
                return objectId;
            }
        }

        // TODO 更正这个抛出的错误
        throw new Error();
    }
}
