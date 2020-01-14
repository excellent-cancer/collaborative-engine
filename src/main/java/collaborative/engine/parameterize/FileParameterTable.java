package collaborative.engine.parameterize;

import collaborative.engine.content.ContentSupport;
import pact.support.FileSupport;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

public class FileParameterTable extends ParameterTable implements AutoCloseable {

    private final File file;
    private final Map<String, Object> properties;

    private FileParameterTable(File file) throws IOException {
        this.file = file;
        this.properties = ContentSupport.flatLoadYaml(file.toPath());
    }

    public File getLocation() {
        return file;
    }

    @Override
    public void close() throws IOException {
        throw new RuntimeException();
    }

    public static FileParameterTable create(File file) throws IOException {
        if (!file.exists()) {
            Files.createFile(file.toPath());
        }

        return new FileParameterTable(file);
    }

    public static FileParameterTable create(File file, String child, boolean createIfNotExist) throws IOException {
        file = FileSupport.childFile(file, child);
        if (!file.exists()) {
            if (createIfNotExist) {
                Files.createFile(file.toPath());
            } else {
                throw new IOException();
            }
        }

        return new FileParameterTable(file);
    }
}
