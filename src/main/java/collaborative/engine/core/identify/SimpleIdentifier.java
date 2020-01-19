package collaborative.engine.core.identify;

import java.io.File;
import java.util.UUID;

/**
 * 实现草稿
 *
 * @author XyParaCrim
 * @temp 不明确其的组成结构
 */
public class SimpleIdentifier implements Identifier {

    @Override
    public ObjectId newId() {
        return new ObjectUUID();
    }

    @Override
    public ObjectId toObjectId(String name) {
        return null;
    }

    private static class ObjectUUID implements ObjectId {

        final UUID uuid;
        final File location;

        public ObjectUUID() {
            this.uuid = UUID.randomUUID();
            this.location = fileFor(uuid);
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
        public void used() {

        }

        @Override
        public ObjectId unmodifiable() {
            return null;
        }

        private static File fileFor(UUID uuid) {
            String uuidStr = uuid.toString();
            String group = uuidStr.substring(0, 2);
            String name = uuidStr.substring(2);
            return new File(group, name);
        }
    }
}
