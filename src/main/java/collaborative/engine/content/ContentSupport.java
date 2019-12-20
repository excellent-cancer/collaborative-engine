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

    public static Map<String, Object> flatLoadYaml(Path path) throws IOException {
        return Yaml.flat(Files.newBufferedReader(path));
    }

}
