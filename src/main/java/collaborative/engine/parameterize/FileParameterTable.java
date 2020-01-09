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

    @Override
    public void close() throws IOException {
        throw new UnsupportedOperationException();
    }

    public static FileParameterTable create(File file) throws IOException {
        if (!file.exists()) {
            Files.createFile(file.toPath());
        }

        return new FileParameterTable(file);
    }

    public static FileParameterTable create(File file, String child) throws IOException {
        file = FileSupport.childFile(file, child);
        if (!file.exists()) {
            Files.createFile(file.toPath());
        }

        return new FileParameterTable(file);
    }
}
