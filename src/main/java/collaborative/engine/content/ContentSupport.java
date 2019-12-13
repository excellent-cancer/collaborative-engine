package collaborative.engine.content;

import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public final class ContentSupport {

    // TO-REMOVE
    public static Map<String, Object> loadYaml(Path path) throws IOException {
        return new Yaml().load(Files.newBufferedReader(path));
    }
}
