package collaborative.engine.core.identify;

import java.util.concurrent.atomic.AtomicReferenceArray;

/**
 * 闭散列（开式寻址）。特别地，在删除的时候或者无法探测的时候，会重新创建一个新的
 * 数组容器。因为这个散列长度太小，造成的聚合可能性增大，则恶化的可能性也增大。
 *
 * @param <T> 特定的{@link ObjectId}类型
 */
class SimpleCache<T extends ObjectId> extends Cache<T> {

    /**
     * 表示缓存容量的指数值
     */
    private final int capacityPower;

    /**
     * 表示计算缓存容量是所需移动的位数
     */
    private final int capacityPowerShift;

    /**
     * 缓存容器
     */
    private volatile AtomicReferenceArray<T> container;

    /**
     * 初始化最小容量的缓存
     */
    SimpleCache() {
        this(CacheConfig.MIN_CACHE_CAPACITY_BITS);
    }

    /**
     * 根据指定的缓存容量初始化缓存
     *
     * @param capacityPower 缓存容量的指数值
     */
    SimpleCache(int capacityPower) {
        this.capacityPower = capacityPower;
        this.capacityPowerShift = Integer.SIZE - capacityPower;
        this.container = new AtomicReferenceArray<>(1 << capacityPower);
    }

    /**
     * 通过{@link ObjectId}实例的hashCode的最后{@link SimpleCache#capacityPowerShift}
     * 的位数作为缓存数组存放位置
     *
     * @param objectId 实现的{@link ObjectId}实例
     * @return 缓存数组存放位置
     */
    private int position(T objectId) {
        return objectId.hashCode() >>> capacityPowerShift;
    }

    @Override
    void add(T objectId) {
        int index = position(objectId);
        AtomicReferenceArray<T> cache = container;
        for (int probeCount = 0; probeCount < CacheConfig.MAX_PROBE_COUNT; ) {
            T inCache = cache.get(index);
            if (inCache == null) {
                if (cache.compareAndSet(index, null, objectId)) {
                    return;
                }
            } else {
                if (inCache.equals(objectId)) {
                    return;
                }
                if (++index == cache.length()) {
                    index = 0;
                }
                probeCount++;
            }
        }
    }

    @Override
    void remove(T objectId) {
        if (contains(objectId)) {
            container = new AtomicReferenceArray<>(1 << capacityPower);
        }
    }

    @Override
    boolean contains(T objectId) {
        int index = position(objectId);
        AtomicReferenceArray<T> cache = container;
        for (int probeCount = 0; probeCount < CacheConfig.MAX_PROBE_COUNT; probeCount++) {
            T inCache = cache.get(index);
            if (inCache == null) {
                return false;
            }
            if (inCache.equals(objectId)) {
                return true;
            }
            if (++index == cache.length()) {
                index = 0;
            }
        }

        return false;
    }
}
