package collaborative.engine.core.identify;

import pact.support.FileSupport;

import java.io.File;
import java.util.UUID;

/**
 * 实现草稿
 *
 * @author XyParaCrim
 * @temp 不明确其的组成结构
 */
public class SimpleIdentifier implements Identifier {

    private final Cache<ObjectUUID> cache = CacheConfig.newCache(getClass());

    @Override
    public ObjectId newId() {
        return new ObjectUUID();
    }

    @Override
    public ObjectId toObjectId(String name) {
        return new ObjectUUID(name);
    }

    private class ObjectUUID implements ObjectId {

        final UUID uuid;
        final File location;
        final File group;

        ObjectUUID() {
            this.uuid = UUID.randomUUID();
            this.location = fileFor(uuid);
            this.group = this.location.getParentFile();
        }

        ObjectUUID(String name) {
            this.uuid = UUID.fromString(name);
            this.location = fileFor(uuid);
            this.group = this.location.getParentFile();
        }

        ObjectUUID(ObjectUUID objectId, File parent) {
            this.uuid = objectId.uuid;
            this.location = FileSupport.resolve(parent, objectId.location);
            this.group = FileSupport.resolve(parent, objectId.group);
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
        public boolean inCache() {
            return cache.contains(this);
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
        public ObjectId unmodifiable(File parent) {
            return new ObjectUUID(this, parent);
        }

        @Override
        public String toString() {
            return uuid.toString();
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
