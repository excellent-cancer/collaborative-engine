package collaborative.engine.core.databse;

import collaborative.engine.core.Collaboratory;
import collaborative.engine.core.ContentSystem;
import collaborative.engine.core.identify.Identifier;
import collaborative.engine.core.identify.ObjectId;

import java.io.File;
import java.util.*;

public class FileDatabase implements AutoCloseable {

    private final Collaboratory collaboratory;

    private final File directory;

    private final ContentSystem contentSystem;

    private final Identifier identifier;

    public FileDatabase(Collaboratory collaboratory, File directory, ContentSystem contentSystem) {
        this.collaboratory = collaboratory;
        this.directory = directory;
        this.contentSystem = contentSystem;
        this.identifier = ContentSystem.newIdentifier();
    }

    public ObjectId insert(File file, InsertOption... options) {
        Set<InsertOption> optionSet;
        if (options.length > 0) {
            optionSet = new HashSet<>(options.length);
            Collections.addAll(optionSet, options);
        } else {
            optionSet = Collections.emptySet();
        }
        return insert(file, optionSet);
    }

    private ObjectId insert(File file, Collection<InsertOption> options) {
        // 信任这次插入操作
        if (options.contains(InsertOption.UNSUSPECTING)) {
            ObjectId objectId = identifier.newId();
            File objectFile = objectId.toFile(directory);
            objectFile.getParentFile().mkdir();

            // TODO 加上retry次数，objectId独占功能
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

    }
}
