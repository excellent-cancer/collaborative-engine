package collaborative.engine.content.yaml;

import java.io.IOException;

/**
 * @author XyParaCrim
 */
public class YamlLexException extends RuntimeException {
    public YamlLexException(IOException e) {
        super(e);
    }

    public YamlLexException() {
        super();
    }
}
