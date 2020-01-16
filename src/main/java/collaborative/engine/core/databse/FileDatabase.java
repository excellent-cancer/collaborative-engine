package collaborative.engine.core.databse;

import collaborative.engine.core.Collaboratory;
import collaborative.engine.core.ContentSystem;
import collaborative.engine.core.identify.Identifier;
import collaborative.engine.core.identify.ObjectId;
import pact.support.FileSupport;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * 本地文件数据库，提供一致添加、删除等文件操作。并且实现文件的并发访问与读写。
 *
 * @author XyParaCrim
 */
@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class FileDatabase implements AutoCloseable {

    private final Collaboratory collaboratory;

    private final File directory;

    private final ContentSystem contentSystem;

    private final Identifier identifier;

    public FileDatabase(Collaboratory collaboratory, File directory, ContentSystem contentSystem) {
        this.collaboratory = collaboratory;
        this.directory = directory;
        this.contentSystem = contentSystem;
        this.identifier = contentSystem.newIdentifier();
    }

    public boolean contains(ObjectId objectId) {
        if (identifier.contains(objectId)) {
            return true;
        }

        if (objectId.location().exists()) {
            identifier.add(objectId);
            return true;
        }

        return false;
    }

    public ObjectId insert(File file, InsertOption... options) throws IOException {
        Set<InsertOption> optionSet;
        if (options.length > 0) {
            optionSet = new HashSet<>(options.length);
            Collections.addAll(optionSet, options);
        } else {
            optionSet = Collections.emptySet();
        }
        return insert(file, optionSet);
    }

    private ObjectId insert(File file, Collection<InsertOption> options) throws IOException {
        // 插入的为临时文件，并且信任这次插入操作
        if (options.contains(PromissoryInsertOption.TEMP)) {
            ObjectId objectId;
            while (contains(objectId = identifier.newId())) {
                objectId.used();
            }

            FileSupport.mkdir(objectId.groupLocation());
            FileSupport.atomicMove(file, objectId.location());
            identifier.add(objectId);

            return objectId.unmodifiable();
        } else {
            Objects.requireNonNull(file);
            throw new UnsupportedOperationException();
        }
    }

    public File location() {
        return directory;
    }

    @Override
    public void close() throws Exception {
        // TODO
    }
}
