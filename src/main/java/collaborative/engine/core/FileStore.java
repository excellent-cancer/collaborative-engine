package collaborative.engine.core;

import java.io.File;

public class FileStore implements AutoCloseable {

    private final Collaboratory collaboratory;

    private final File directory;

    public FileStore(Collaboratory collaboratory, File directory) {
        this.collaboratory = collaboratory;
        this.directory = directory;
    }

    public File location() {
        return directory;
    }

    @Override
    public void close() throws Exception {

    }
}
