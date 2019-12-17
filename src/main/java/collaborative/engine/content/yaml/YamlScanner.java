package collaborative.engine.content.yaml;


import collaborative.engine.content.common.LineColumn;
import collaborative.engine.content.core.ContentScanner;

import java.io.Reader;
import java.util.LinkedList;

/**
 * Built-in lexical analyzer for yaml file to avoid to use
 * third-part library.
 *
 * @author XyParaCrim
 */
@SuppressWarnings("unused")
public class YamlScanner implements ContentScanner {
    private final YamlReader yamlReader;

    /** Number of lines currently scanned to position. */
    private int line;

    /** Number of columns currently scanned to position. */
    private int column;

    /** The current scanned token. */
    private Token token;

    /** Store scanned multiple tokens. */
    private LinkedList<Token> scanned;

    public YamlScanner(Reader reader) {
        this.line = 0;
        this.column = 0;
        this.scanned = new LinkedList<>();
        this.yamlReader = YamlReader.newYamlReader(reader);
    }

    @Override
    public Token currentToken() {
        return token;
    }

    @Override
    public int currentLine() {
        return line;
    }

    @Override
    public int currentColumn() {
        return column;
    }

    @Override
    public void scan() {
        // 一些scan操作会连续读入多个token
        if (scanned.isEmpty()) {
            yamlReader.readChar();
            if (scanSpaceOrLine() &&
                    scanEof() &&
                    scanComment()) {
                scanProperty();
            }
        }

        token = scanned.pollFirst();
    }

    // Scanner Ultimate

    /**
     * Scan spaceWhite and line terminator and skip it.But it's
     * always true, because does not scan eof.
     * @return whether to scan for new tokens including EOF.
     */
    private boolean scanSpaceOrLine() {
        while (yamlReader.isWhiteSpace() ||
                yamlReader.isLineTerminator(true)) {
            yamlReader.readChar();
        }

        return true;
    }

    /**
     * Scan yaml comment content and skip it.
     * @return whether to scan for new tokens including EOF.
     */
    private boolean scanComment() {
        if (yamlReader.isCommentSign()) {
            do {
                yamlReader.readChar();
                if (yamlReader.isLineTerminator(true)) {
                    line++;
                    token = Token.comment();
                    return false;
                }
            } while (!yamlReader.isEof());

            token = Token.eof();
            return false;
        }

        return true;
    }

    private boolean scanEof() {
        if (yamlReader.isEof()) {
            token = Token.eof();
            return false;
        }
        return true;
    }

    private void scanProperty() {
        if (!yamlReader.isLetterOrDigit()) {
            throw new YamlReaderException();
        }

        StringBuilder str = new StringBuilder();

        do {
            str.append(yamlReader.current());
            yamlReader.readChar();
        } while (!yamlReader.isEof() && !yamlReader.isSplitCharater());

        if (!yamlReader.isSplitCharater()) {
            throw new YamlReaderException();
        }

        scanned.push(Token.key(str.toString()));

        // scan value
        while (yamlReader.isWhiteSpace() && !yamlReader.isEof()) {
            yamlReader.readChar();
        }

        if (yamlReader.isLetterOrDigit()) {
            str = new StringBuilder();
            str.append(yamlReader.current());

            while (!yamlReader.isEof()) {
                yamlReader.readChar();
                if (yamlReader.isLineTerminator(true)) {
                    break;
                }
                str.append(yamlReader.current());
            }
        } else {
            throw new YamlReaderException();
        }
    }
}
