package collaborative.engine.content.yaml;

import pact.annotation.NotNull;

import java.io.Reader;
import java.util.Map;

/**
 * About the facade of yaml content.
 *
 * @author XyParaCrim
 */
public final class Yaml {

    public static final FlatParseYaml FLAT_PARSE_YAML = new FlatParseYaml();

    /**
     * Read the contents of the yaml file through a reader and parse it into
     * a flat structure.
     *
     * @param reader source of yaml's file
     * @return flat key-value
     */
    @NotNull
    public static Map<String, Object> flat(Reader reader) {
        return FLAT_PARSE_YAML.resolve(new YamlScanner(reader));
    }
}
