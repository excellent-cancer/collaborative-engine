package collaborative.engine.content.yaml;


import collaborative.engine.content.common.LineColumn;
import collaborative.engine.content.common.Paragraph;
import collaborative.engine.content.core.ContentScanner;
import collaborative.engine.content.core.Token;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Reader;
import java.util.LinkedList;
import java.util.UUID;

import static collaborative.engine.content.yaml.YamlToken.YamlTokenKind.*;

/**
 * Built-in lexical analyzer for yaml file to avoid to use
 * third-part library.
 *
 * @author XyParaCrim
 */
@SuppressWarnings("unused")
public class YamlScanner implements ContentScanner {

    private static final Logger LOGGER = LogManager.getLogger(YamlScanner.class);

    /**
     * To identify the scanning process.
     */
    private final String scanId = UUID.randomUUID().toString().substring(0, 8);

    /**
     * Delegate reader to yamlReader
     */
    private final YamlReader yamlReader;

    /**
     * The current scanned token.
     */
    private Token token;

    /**
     * Store scanned multiple tokens.
     */
    LinkedList<Token> scanned;

    /**
     * Record scan locations and generate tokens.
     */
    private ScanYamlTracker tracker;

    /**
     * Indicates whether scanning can continue. If false, scan ended or lexical error.
     */
    private boolean isReachErrorOrEFO = false;

    /**
     * lexical scanner for yaml file.During the scanning process,
     * Yaml's tokens pop up to record the scanning position.
     *
     * @param source source of yaml file
     */
    public YamlScanner(Reader source) {
        this.token = YamlToken.DUMMY;
        this.scanned = new LinkedList<>();
        this.yamlReader = YamlReader.newYamlReader(source);
        this.tracker = new ScanYamlTracker();
        scanInitialized();
    }

    // Main Export

    @Override
    public Token currentToken() {
        return token;
    }

    @Override
    public int currentLine() {
        return token.paragraph.end().line;
    }

    @Override
    public int currentColumn() {
        return token.paragraph.end().column;
    }

    @Override
    public boolean scannable() {
        return !(isReachErrorOrEFO ||
                (isReachErrorOrEFO = token.kind == YamlToken.YamlTokenKind.ERROR || token.kind == YamlToken.YamlTokenKind.EOF));
    }

    @Override
    public void scan() {
        if (scannable()) {
            // some handler of do scanning may scan more than one token.
            if (scanned.isEmpty()) {
                // in this step, the current char can't be EOF.
                if (scanSpaceOrLine() &&
                        scanEOF() &&
                        scanComment() &&
                        scanItems() &&
                        scanNamed()) {
                    scanValue();
                }
            }

            token = scanned.pollLast();
        }
    }

    // Scanner Ultimate

    /**
     * fix scan(): the number of lines does not increase
     */
    private void scanInitialized() {
        if (yamlReader.read()) {
            tracker.skipLine();
        } else {
            tracker.saveEOFToken();
        }
    }

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
                tracker.skipWhiteSpace();
                continue;
            }
            if (yamlReader.isLineTerminator(true)) {
                tracker.skipLine();
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
        if (yamlReader.current(COMMENT.sign)) {
            tracker.markLineColumn();
            do {
                tracker.skipChar();
                if (yamlReader.isLineTerminator(true)) {
                    tracker.saveCommentToken();
                    tracker.skipLine();
                    return false;
                }
                if (yamlReader.isEOF()) {
                    tracker.saveCommentToken();
                    tracker.saveEOFToken();
                    return false;
                }
            } while (true);
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
            tracker.saveEOFToken();
            return false;
        }
        return true;
    }

    /**
     * Scan if the current character is ITEM_SIGN. If scan it,
     * it will scan the whole line.
     *
     * @return whether to scan for new tokens including EOF.
     */
    private boolean scanItems() {
        if (yamlReader.current(ITEM.sign)) {
            tracker.markLineColumn();

            // At this step, the current character must not be a comment, whiteSpace, or EOF.
            boolean includeItemSign = false;
            do {
                tracker.skipChar();
                if (yamlReader.isWhiteSpace()) {
                    tracker.saveItemToken();
                    tracker.skipChar();
                    tracker.markLineColumn();
                } else {
                    // if the character immediately following - is not whiteSpace,
                    // it means that it is not an array item, but a literal
                    // that starts with -
                    if (scanAnymore()) {
                        includeItemSign = true;
                        break;
                    } else {
                        return false;
                    }
                }
            } while (yamlReader.current(ITEM.sign));

            StringBuilder literal = new StringBuilder();
            if (includeItemSign) {
                literal.append(ITEM.sign);
            } else {
                tracker.skipWhiteSpaceContinuously();
            }

            while (true) {
                if (yamlReader.isEOF()) {
                    tracker.saveLiteralToken(literal.toString().trim());
                    tracker.saveEOFToken();
                    return false;
                }

                if (yamlReader.isLineTerminator(true)) {
                    tracker.saveLiteralToken(literal.toString().trim());
                    tracker.skipLine();
                    return false;
                }

                if (yamlReader.current(SPLIT.sign)) {
                    tracker.skipChar();
                    if (yamlReader.isWhiteSpace()) {
                        tracker.saveNamedToken(literal.toString());
                        tracker.saveSplitToken();
                        scanValue();
                        return false;
                    }
                }

                literal.append(yamlReader.current());
                tracker.skipChar();
            }
        }

        return true;
    }

    /**
     * Scans a property, including a key and a value. If one
     * is missing, and will throw runtime exception.
     *
     * @return whether to scan for new tokens including EOF.
     */
    private boolean scanNamed() {
        tracker.markLineColumn();

        StringBuilder str = new StringBuilder();

        // scan forward until it encounter a :, and : followed by
        // a white space. Scans for errors if it was not encountered
        // until the next line or EOF
        do {
            str.append(yamlReader.current());
            tracker.skipChar();

            if (yamlReader.current(SPLIT.sign)) {
                tracker.skipChar();
                if (yamlReader.isWhiteSpace()) {
                    tracker.skipChar();
                    tracker.saveNamedToken(str.toString().trim());
                    tracker.saveSplitToken();
                    return true;
                }
                str.append(SPLIT.sign);
            }
        } while (scanAnymore());

        return false;
    }

    /**
     * Scan the entire line of literal after the split token
     */
    private void scanValue() {
        // skip previous spaces
        tracker.skipWhiteSpaceContinuously();

        StringBuilder literal = new StringBuilder();
        tracker.markLineColumn();
        while (true) {
            // scan the next line but current line-column still at the end of the previous line
            if (yamlReader.isLineTerminator(true)) {
                tracker.saveLiteralToken(literal.toString().trim());
                tracker.skipLine();
                return;
            }

            if (yamlReader.isEOF()) {
                tracker.saveLiteralToken(literal.toString().trim());
                tracker.saveEOFToken();
                return;
            }

            literal.append(yamlReader.current());
            tracker.skipChar();
        }
    }

    private boolean scanAnymore() {
        if (yamlReader.isEOF() || yamlReader.isLineTerminator()) {
            tracker.saveErrorToken();
            return false;
        }
        return true;
    }


    // Utils function

    private void reportTrace(String message) {
        LOGGER.trace("[{}][{}:{}] {}", scanId, currentLine(), currentColumn(), message);
    }

    private void reportTrace(Token token, String message) {
        LineColumn start = token.paragraph.start();
        LineColumn end = token.paragraph.end();
        LOGGER.trace("[{}][{}:{}-{}:{}] {}", scanId, start.line, start.column, end.line, end.column, message);
    }

    private void reportTraceAndContent(Token token, String message) {
        LineColumn start = token.paragraph.start();
        LineColumn end = token.paragraph.end();
        LOGGER.debug("[{}][{}:{}-{}:{}] {}: \"{}\"", scanId, start.line, start.column, end.line, end.column, message, token.content);
    }

    private class ScanYamlTracker {
        /**
         * Number of lines currently scanned to position.
         */
        int line = 0;

        /**
         * Number of columns currently scanned to position.
         */
        int column = 0;

        /**
         * Number of token lines used to record tokens containing continuous content.
         */
        int markedLine;

        /**
         * Number of token columns used to record tokens containing continuous content.
         */
        int markedColumn;

        void markLineColumn() {
            markedLine = line;
            markedColumn = column + 1;
        }

        // save token into the scanned token list

        void saveEOFToken() {
            scanned.push(YamlToken.EOF(Paragraph.identical(line, column)));
            reportTrace("EOF Token");
        }

        void saveCommentToken() {
            LineColumn start = LineColumn.of(markedLine, markedColumn);
            LineColumn end = LineColumn.of(line, column);
            YamlToken commentToken = YamlToken.comment(Paragraph.of(start, end));
            scanned.push(commentToken);
            reportTrace(commentToken, "comment token");
        }

        void saveLiteralToken(String content) {
            if (!content.isEmpty()) {
                LineColumn start = LineColumn.of(markedLine, markedColumn);
                LineColumn end = LineColumn.of(markedLine, markedColumn + content.length() - 1);
                YamlToken literalToken = YamlToken.literal(Paragraph.of(start, end), content);
                scanned.push(literalToken);
                reportTraceAndContent(literalToken, "literal token");
            }
        }

        void saveSplitToken() {
            LineColumn start = LineColumn.of(line, column - 1);
            LineColumn end = LineColumn.of(line, column);
            YamlToken splitToken = YamlToken.split(Paragraph.of(start, end));
            scanned.push(splitToken);
            reportTraceAndContent(splitToken, "split token");
        }

        void saveItemToken() {
            YamlToken itemToken = YamlToken.item(Paragraph.identical(line, column));
            scanned.push(itemToken);
            reportTraceAndContent(itemToken, "item token");
        }

        void saveErrorToken() {
            scanned.push(YamlToken.error(Paragraph.identical(line, column)));
            reportTrace("error token");
        }

        void saveNamedToken(String content) {
            LineColumn start = LineColumn.of(markedLine, markedColumn);
            LineColumn end = LineColumn.of(markedLine, markedColumn + content.length() - 1);
            YamlToken namedToken = YamlToken.named(Paragraph.of(start, end), content);
            scanned.push(namedToken);
            reportTraceAndContent(namedToken, "named token");
        }

        void skipWhiteSpace() {
            column++;
            yamlReader.readChar();
            reportTrace("skip white space");
        }

        void skipWhiteSpaceContinuously() {
            while (yamlReader.isWhiteSpace()) {
                tracker.skipChar();
            }
        }

        void skipLine() {
            line++;
            column = 0;
            yamlReader.readChar();
        }

        void skipChar() {
            column++;
            yamlReader.readChar();
        }
    }
}
