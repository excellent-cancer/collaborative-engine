package collaborative.engine.content.yaml;


import collaborative.engine.content.common.LineColumn;
import collaborative.engine.content.common.Paragraph;
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

    /**
     * Delegate reader to yamlReader
     */
    private final YamlReader yamlReader;

    /**
     * Number of lines currently scanned to position.
     */
    private int line;

    /**
     * Number of columns currently scanned to position.
     */
    private int column;

    /**
     * The current scanned token.
     */
    private Token token;

    /**
     * Store scanned multiple tokens.
     */
    private LinkedList<Token> scanned;

    /**
     * Indicates whether scanning can continue. If false, scan ended or lexical error.
     */
    private boolean scannable = true;

    /**
     * lexical scanner for yaml file.During the scanning process,
     * Yaml's tokens pop up to record the scanning position.
     *
     * @param source source of yaml file
     */
    public YamlScanner(Reader source) {
        this.column = 0;
        this.scanned = new LinkedList<>();
        this.yamlReader = YamlReader.newYamlReader(source);
        // fix scan(): the number of lines does not increase
        if (this.yamlReader.isEOF()) {
            this.line = 0;
            scanned.push(YamlToken.EOF(lineColumn()));
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
        // some handler of do scanning may scan more than one token.
        if (scanned.isEmpty()) {
            // in this step, the current char can't be EOF.
            yamlReader.readChar();
            if (scanSpaceOrLine() &&
                    scanEOF() &&
                    scanComment()) {
                scanProperty();
            }
        } else {
            token = scanned.pollFirst();
        }
    }

    // Scanner Ultimate

    /**
     * Scan spaceWhite and line terminator and skip it.But it's
     * always true, because does not scan eof.
     *
     * @return whether to scan for new tokens including EOF.
     */
    @SuppressWarnings("SameReturnValue")
    private boolean scanSpaceOrLine() {
        while (true) {
            if (yamlReader.isWhiteSpace()) {
                scanChar();
                continue;
            }
            if (yamlReader.isLineTerminator(true)) {
                scanNextLine();
                yamlReader.readChar();
                continue;
            }
            break;
        }

        // always true, because no matter how scan after all hope to continue
        return true;
    }

    /**
     * Scan yaml comment content and skip it.
     *
     * @return whether to scan for new tokens including EOF.
     */
    private boolean scanComment() {
        if (yamlReader.current(YamlTokenizer.COMMENT_SIGN)) {
            int startLine = currentLine();
            int startColumn = currentColumn() + 1;
            do {
                scanChar();
                column++;
                yamlReader.readChar();
                if (yamlReader.isLineTerminator(true)) {
                    token = YamlToken.comment(from(startColumn, startColumn));
                    scanNextLine();
                    return false;
                }
            } while (!yamlReader.isEOF());

            token = YamlToken.EOF(lineColumn());
            return false;
        }

        return true;
    }

    /**
     * Scan if the current character has ended.
     *
     * @return whether to scan for new tokens including EOF.
     */
    @SuppressWarnings("AlibabaLowerCamelCaseVariableNaming")
    private boolean scanEOF() {
        if (yamlReader.isEOF()) {
            token = YamlToken.EOF(lineColumn());
            return false;
        }
        return true;
    }

    /**
     * Scans a property, including a key and a value. If one
     * is missing, and will throw runtime exception.
     */
    private void scanProperty() {
        if (!yamlReader.isLetterOrDigit()) {
            throw new YamlLexException();
        }

        StringBuilder str = new StringBuilder();
        int startLine = currentLine();
        int startColumn = currentColumn() + 1;
        do {
            str.append(yamlReader.current());
            scanChar();
        } while (!yamlReader.isEOF() && !yamlReader.current(YamlTokenizer.SPLIT_CHARATER) && !yamlReader.isLineTerminator());

        if (yamlReader.current(YamlTokenizer.SPLIT_CHARATER)) {
            // got a token which is key's literal
            token = YamlToken.literal(from(startLine, startColumn), str.toString());
            scanned.push(token);
            // advance one character
            // and for preciseness, push split token to scanned list
            scanChar();
            scanSplit();
        } else {
            scanError();
            return;
        }

        // scan value
        while (yamlReader.isWhiteSpace() && !yamlReader.isEOF()) {
            scanChar();
        }

        if (yamlReader.isLetterOrDigit()) {
            str = new StringBuilder();
            startLine = currentLine();
            startColumn = currentColumn() + 1;
            do {
                str.append(yamlReader.current());
                scanChar();

                if (yamlReader.isLineTerminator(true)) {
                    token = YamlToken.literal(from(startLine, startColumn), str.toString());
                    scanned.push(token);
                    // scan the next line but current line-column still at the end of the previous line
                    scanNextLine();
                    break;
                }
            } while (!yamlReader.isEOF());
        } else {
            scanError();
        }
    }

    /**
     * Scan an error token into the scanned token list and mark
     * {@link YamlScanner} as unscannable.
     */
    private void scanError() {
        token = YamlToken.error(from());
        scanned.push(token);
        scannable = false;
    }

    /**
     * Scan an split token into the scanned token list.
     */
    private void scanSplit() {
        token = YamlToken.split(from());
        scanned.push(token);
    }

    /**
     * Scan an item token into the scanned token list.
     */
    private void scanItem() {
        token = YamlToken.item(from());
        scanned.push(token);
    }

    /**
     * Scan a char, but must confirm the correctness of the current character
     */
    private void scanChar() {
        column++;
        yamlReader.readChar();
    }

    /**
     * Ready to scan next line, but must confirm the current character is line-terminator
     */
    private void scanNextLine() {
        line++;
        column = 0;
    }

    // Utils function

    private Paragraph from(LineColumn startLineColumn) {
        return Paragraph.of(startLineColumn, lineColumn());
    }

    private Paragraph from(int line, int column) {
        return Paragraph.of(LineColumn.of(line, column), lineColumn());
    }

    private Paragraph from() {
        return Paragraph.identical(lineColumn());
    }
}
