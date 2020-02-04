package collaborative.engine.core.identify;

import java.io.File;


/**
 * 表识文件的唯一表识和转换成不同形式的功能，还包括它的资源管理等等。
 *
 * @author XyParaCrim
 */
public interface ObjectId {

    /**
     * 返回本地文件虚拟路径，这个文件并不一定存在，即从objectId到文件名的转换
     *
     * @return 代表此objectId的文件
     */
    File location();

    /**
     * 返回本地文件所在的分组目录
     *
     * @return 分组目录
     */
    File groupLocation();

    /**
     * 这里考虑返回一个无法修改资源使用权的对象
     *
     * @param parent 设置一个parent
     * @return ？
     */
    ObjectId unmodifiable(File parent);

    /**
     * 缓存这个objectId
     */
    void cache();

    /**
     * 解除这个objectId的缓存
     */
    void uncache();

    /**
     * 查询这个Id是否已经缓存
     *
     * @return 是否已经缓存
     */
    boolean inCache();

    /**
     * 与{@link ObjectId#release()}对应。尝试独占这个objectId的使用权
     *
     * @return 是否得到其的使用权
     */
    boolean acquire();

    /**
     * 与{@link ObjectId#acquire()}对应。释放这个objectId的使用权
     */
    void release();
}
