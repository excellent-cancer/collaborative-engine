package collaborative.engine.core.identify;

import java.io.File;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReferenceArray;

/**
 * 实现草稿
 *
 * @author XyParaCrim
 * @temp 不明确其的组成结构
 */
public class SimpleIdentifier implements Identifier {

    private final static int MIN_CACHE_CAPACITY_BITS = 5;

    private final static int MAX_CACHE_CAPACITY_BITS = 11;

    private final Cache cache = new Cache(MIN_CACHE_CAPACITY_BITS);

    @Override
    public ObjectId newId() {
        return new ObjectUUID(cache);
    }

    @Override
    public ObjectId toObjectId(String name) {
        return new ObjectUUID(name, cache);
    }

    private static class ObjectUUID implements ObjectId {

        final Cache cache;
        final UUID uuid;
        final File location;
        final File group;

        ObjectUUID(Cache cache) {
            this.cache = cache;
            this.uuid = UUID.randomUUID();
            this.location = fileFor(uuid);
            this.group = this.location.getParentFile();
        }

        ObjectUUID(String name, Cache cache) {
            this.cache = cache;
            this.uuid = UUID.fromString(name);
            this.location = fileFor(uuid);
            this.group = this.location.getParentFile();
        }

        @Override
        public File location() {
            return location;
        }

        @Override
        public File groupLocation() {
            return location.getParentFile();
        }

        @Override
        public void cache() {
            cache.add(this);
        }

        @Override
        public void uncache() {
            cache.remove(this);
        }

        @Override
        public boolean acquire() {
            return false;
        }

        @Override
        public void release() {
            throw new UnsupportedOperationException();
        }

        @Override
        public ObjectId unmodifiable() {
            return null;
        }

        @Override
        public String toString() {
            return uuid.toString();
        }
    }

    private static final class Cache {

        private final static ObjectUUID REMOVED = new ObjectUUID(null);

        private final static int MAX_TRY_COUNT = 8;

        private final AtomicReferenceArray<ObjectUUID> container;

        private final int bits;

        private final int shift;

        Cache(int bits) {
            this.bits = bits;
            this.shift = 32 - bits;
            this.container = new AtomicReferenceArray<>(1 << bits);
        }

        void add(ObjectUUID objectId) {
            int index = index(objectId);
            for (int n = 0; n < MAX_TRY_COUNT; ) {
                ObjectUUID uuid = container.get(index);
                if (objectId.equals(uuid)) {
                    return;
                }

                if (container.compareAndSet(index, uuid, objectId)) {
                    if (uuid == null || uuid.equals(REMOVED)) {
                        return;
                    }

                    objectId = uuid;
                    if (++index == container.length()) {
                        index = 0;
                    }
                    n++;
                }
            }
        }

        boolean contains(ObjectUUID objectId) {
            int index = index(objectId);
            for (int n = 0; n < MAX_TRY_COUNT; ) {
                ObjectUUID uuid = container.get(index);
                if (uuid == null) {
                    return false;
                }

                if (uuid.equals(objectId)) {
                    return true;
                }

                if (++index == container.length()) {
                    index = 0;
                }
                n++;
            }
            return false;
        }

        void remove(ObjectUUID objectId) {
            int index = index(objectId);
            for (int n = 0; n < MAX_TRY_COUNT; ) {
                ObjectUUID uuid = container.get(index);
                if (uuid == null) {
                    return;
                }

                if (uuid.equals(objectId)) {
                    if (container.compareAndSet(index, uuid, REMOVED)) {
                        return;
                    } else {
                        continue;
                    }
                }

                if (++index == container.length()) {
                    index = 0;
                }
                n++;
            }
        }

        int index(ObjectUUID objectId) {
            return objectId.uuid.hashCode() >>> shift;
        }
    }

    // Util Methods

    private static File fileFor(UUID uuid) {
        String uuidStr = uuid.toString();
        String group = uuidStr.substring(0, 2);
        String name = uuidStr.substring(2);
        return new File(group, name);
    }
}
