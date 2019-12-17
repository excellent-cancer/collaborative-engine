package collaborative.engine.content;

import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Map;

/**
 * @author XyParaCrim
 */
public final class ContentSupport {

    // TO-REMOVE
    public static Map<String, Object> loadYaml(Path path) throws IOException {
        Map<String, Object> properties = new Yaml().load(Files.newBufferedReader(path));
        return properties != null ? properties : Collections.emptyMap();
    }


}
