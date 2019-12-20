package collaborative.engine.content;

import collaborative.engine.content.yaml.Yaml;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

/**
 * @author XyParaCrim
 */
public final class ContentSupport {

    // TO-REMOVE
    public static Map<String, Object> loadYaml(Path path) throws IOException {
        return Yaml.load(Files.newBufferedReader(path));
    }


}
