package collaborative.engine.content.yaml;

import pact.support.BufferSupport;
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
        this.sourceReader = Objects.requireNonNull(sourceReader, "require Reader to reader yaml's file");
    }

    /**
     * Try to read chars to buffer from {@code sourceReader reader}.
     *
     * @return whether to read characters into buffer
     */
    public boolean read() {
        boolean isReadToBuffer = false;
        if (!eof) {
            int count;
            try {
                count = sourceReader.read(buffer);
            } catch (IOException e) {
                throw new YamlLexException(e);
            }

            if (count > 0) {
                index = 0;
                limit = count;
                isReadToBuffer = true;
            } else {
                eof = true;
            }
        }

        return isReadToBuffer;
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
