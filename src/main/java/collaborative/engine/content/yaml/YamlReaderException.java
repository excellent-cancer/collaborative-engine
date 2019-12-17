package collaborative.engine.content.yaml;

import java.io.IOException;

/**
 * @author XyParaCrim
 */
public class YamlReaderException extends RuntimeException {
    public YamlReaderException(IOException e) {
        super(e);
    }

    public YamlReaderException() {
        super();
    }
}
