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
        int startLine = line;
        int startColumn = column + 1;
        boolean afterItem = false;

        // At this step, the current character must not be a comment, whiteSpace, or EOF.
        while (yamlReader.current(YamlTokenizer.ITEM_SIGN)) {
            scanChar();
            if (yamlReader.isWhiteSpace()) {
                scanItem();
                scanChar();
            } else if (yamlReader.isEOF() || yamlReader.isLineTerminator(true)) {
                scanError();
                return;
            } else {
                // if the character immediately following - is not whiteSpace,
                // it means that it is not an array item, but a literal
                // that starts with -
                afterItem = true;
                startColumn = column;
                break;
            }
        }

        StringBuilder str = new StringBuilder();
        if (afterItem) {
            // a literal that starts with -
            str.append(YamlTokenizer.ITEM_SIGN);
        }

        // scan forward until it encounter a :, and : followed by
        // a white space. Scans for errors if it was not encountered
        // until the next line or EOF
        do {
            str.append(yamlReader.current());
            scanChar();
            if (!scanMore()) {
                return;
            }
        } while (scanSplit());

        // got a token which is key's literal
        // push literal token
        token = YamlToken.literal(
                from(startLine, startColumn, token.paragraph.start().previousColumn()),
                str.toString()
        );
        scanned.push(token);

        scanValue();
    }

    /**
     * Scan the entire line of literal after the split token
     */
    private void scanValue() {
        // scan the whole line
        int startLine = line;
        int startColumn = column + 1;

        int whiteSpaceSize = 0;
        int literalSize = 0;

        StringBuilder literal = new StringBuilder();

        while (!(yamlReader.isLineTerminator(true) && yamlReader.isEOF())) {
            if (yamlReader.isWhiteSpace()) {
                if (literalSize > 0) {
                    whiteSpaceSize++;
                }
            } else {
                if (whiteSpaceSize > 0) {
                    repeatWhiteSpace(literal, whiteSpaceSize);
                    whiteSpaceSize = 0;
                }
                literalSize++;
                literal.append(yamlReader.current());
            }
            scanChar();
        }

        token = YamlToken.literal(from(startLine, startColumn), literal.toString());
        scanned.push(token);

        if (yamlReader.isEOF()) {
            scanEOF();
        } else {
            // scan the next line but current line-column still at the end of the previous line
            scanNextLine();
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
     *
     * @return whether to scan to the separator
     */
    private boolean scanSplit() {
        if (yamlReader.current(YamlTokenizer.SPLIT_SIGN)) {
            scanChar();
            if (yamlReader.isWhiteSpace()) {
                // advance one character
                scanChar();
                // and for preciseness, push split token to scanned list
                token = YamlToken.split(from(line, column - 2));
                scanned.push(token);

                return true;
            }
        }

        return false;
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

    private boolean scanMore() {
        if (yamlReader.isEOF() || yamlReader.isLineTerminator()) {
            scanError();
            return false;
        }
        return true;
    }

    // Utils function

    private Paragraph from(LineColumn startLineColumn) {
        return Paragraph.of(startLineColumn, lineColumn());
    }

    private Paragraph from(int line, int column) {
        return Paragraph.of(LineColumn.of(line, column), lineColumn());
    }

    private Paragraph from(int line, int column, LineColumn endLineColumn) {
        return Paragraph.of(LineColumn.of(line, column), endLineColumn);
    }

    private Paragraph from() {
        return Paragraph.identical(lineColumn());
    }

    private void repeatWhiteSpace(StringBuilder builder, int count) {
        while (count-- > 0) {
            builder.append(' ');
        }
    }
}
