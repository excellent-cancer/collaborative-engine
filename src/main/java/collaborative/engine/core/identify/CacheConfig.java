package collaborative.engine.core.identify;

import java.util.WeakHashMap;
import java.util.function.Supplier;

/**
 * 这个类指明了缓存的相关细节。并且指定了{@link Identifier}与{@link Cache}的关系，
 * 因为外部无需知道它们的关系。
 */
final class CacheConfig {

    // 缓存具体的数据常量

    /**
     * 缓存最小容量的指数值
     */
    final static int MIN_CACHE_CAPACITY_BITS = 5;

    /**
     * 缓存最小容量的指数值
     */
    final static int MAX_CACHE_CAPACITY_BITS = 11;

    /**
     * 在多线程环境下，最大尝试次数
     */
    final static int MAX_TRY_COUNT = 8;

    /**
     * 最大检查碰撞发生处后面的单元格
     */
    final static int MAX_PROBE_COUNT = 8;

    // 指定了{@link Identifier}与{@link Cache}的关系

    /**
     * 储存Identifier与Cache的使用情况
     */
    final static WeakHashMap<Class<? extends Identifier>, Supplier<Cache<?>>> CACHE_USE;

    static {
        CACHE_USE = new WeakHashMap<>(1);
        CACHE_USE.put(SimpleIdentifier.class, SimpleCache::new);
    }

    @SuppressWarnings("unchecked")
    static <T extends ObjectId> Cache<T> newCache(Class<? extends Identifier> identifierType) {
        if (CACHE_USE.containsKey(identifierType)) {
            return (Cache<T>) CACHE_USE.get(identifierType).get();
        }
        throw new IllegalArgumentException();
    }
}
