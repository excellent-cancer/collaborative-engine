package collaborative.engine.content.yaml;

import java.io.Reader;
import java.util.Map;

public final class Yaml {

    public static Map<String, Object> load(Reader reader) {
        return new FlatParseYaml().resolve(new YamlScanner(reader));
    }
}
