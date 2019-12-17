package collaborative.engine.content.yaml;


import collaborative.engine.content.core.ContentScanner;
import collaborative.engine.content.core.Token;

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

    /**
     * lexical scanner for yaml file.During the scanning process,
     * Yaml's words pop up to record the scanning position.
     *
     * @param source source of yaml file
     */
    public YamlScanner(Reader source) {
        this.column = 0;
        this.scanned = new LinkedList<>();
        this.yamlReader = YamlReader.newYamlReader(source);
        if (this.yamlReader.isEOF()) {
            this.line = 0;
            scanned.push(YamlToken.EOF());
        } else {
            this.line = 1;
        }
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
            // in this step, the current char can't be EOF.
            yamlReader.readChar();
            if (scanSpaceOrLine() &&
                    scanEOF() &&
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
     *
     * @return whether to scan for new tokens including EOF.
     */
    private boolean scanSpaceOrLine() {
        while (true) {
            if (yamlReader.isWhiteSpace()) {
                column++;
                yamlReader.readChar();
                continue;
            }
            if (yamlReader.isLineTerminator(true)) {
                column = 0;
                line++;
                yamlReader.readChar();
                continue;
            }
            break;
        }

        return true;
    }

    /**
     * Scan yaml comment content and skip it.
     *
     * @return whether to scan for new tokens including EOF.
     */
    private boolean scanComment() {
        if (yamlReader.current(YamlTokenizer.COMMENT_SIGN)) {
            do {
                column++;
                yamlReader.readChar();
                if (yamlReader.isLineTerminator(true)) {
                    line++;
                    column = 0;
                    token = YamlToken.comment();
                    return false;
                }
            } while (!yamlReader.isEOF());

            token = YamlToken.EOF();
            return false;
        }

        return true;
    }

    /**
     *
     *
     * @return whether to scan for new tokens including EOF.
     */
    private boolean scanEOF() {
        if (yamlReader.isEOF()) {
            token = YamlToken.EOF();
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
            column++;
            yamlReader.readChar();
        } while (!yamlReader.isEOF() && !yamlReader.isSplitCharater() && !yamlReader.isLineTerminator());

        if (!yamlReader.isSplitCharater()) {
            throw new YamlReaderException();
        } else {
            column++;
            yamlReader.readChar();
        }

        scanned.push(YamlToken.key(str.toString()));

        // scan value
        while (yamlReader.isWhiteSpace() && !yamlReader.isEOF()) {
            column++;
            yamlReader.readChar();
        }

        if (yamlReader.isLetterOrDigit()) {
            str = new StringBuilder();
            do {
                str.append(yamlReader.current());
                column++;
                yamlReader.readChar();

                if (yamlReader.isLineTerminator(true)) {
                    column = 0;
                    line++;
                    break;
                }
            } while (!yamlReader.isEOF());
        } else {
            throw new YamlReaderException();
        }
    }
}
