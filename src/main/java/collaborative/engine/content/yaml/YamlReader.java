package collaborative.engine.content.yaml;

import collaborative.engine.BufferSupport;
import collaborative.engine.content.core.ContentReader;

import java.io.IOException;
import java.io.Reader;
import java.util.Objects;

/**
 * @author XyParaCrim
 */
public class YamlReader implements ContentReader {

    private final Reader sourceReader;

    private char[] buffer = BufferSupport.newBuffer();

    private char curr;

    private int index;

    private int limit;

    private boolean eof = false;

    private YamlReader(Reader sourceReader) {
        this.sourceReader = Objects.requireNonNull(sourceReader);
    }

    public boolean read() {
        if (!eof) {
            int count;
            try {
                count = sourceReader.read(buffer);
            } catch (IOException e) {
                throw new YamlReaderException(e);
            }

            if (count > 0) {
                index = 0;
                limit = count;
                eof = true;
            }
        }

        return eof;
    }

    @Override
    public void readChar() {
        if (index < limit || read()) {
            curr = buffer[index++];
        }
    }

    @Override
    public boolean isEOF() {
        return eof;
    }

    @Override
    public char current() {
        return curr;
    }

    public static YamlReader newYamlReader(Reader reader) {
        return new YamlReader(reader);
    }
}
