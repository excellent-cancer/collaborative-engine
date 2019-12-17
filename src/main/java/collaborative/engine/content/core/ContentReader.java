package collaborative.engine.content.core;

/**
 * @author XyParaCrim
 */
public interface ContentReader {

    char current();

    void readChar();

    boolean isEOF();

    default boolean current(char a) {
        return current() == a;
    }

    default String readUntil(char a, boolean include) {
        StringBuilder readStr = new StringBuilder();

        while (!isEOF()) {
            if (current(a)) {
                if (include) {
                    readStr.append(current());
                    break;
                }
            }

            readStr.append(current());
            readChar();
        }

        return readStr.toString();

    }

    default void skipUntil(char a, boolean include) {
        while (!isEOF()) {
            if (current(a)) {
                if (include) {
                    readChar();
                    break;
                }
            }
            readChar();
        }
    }

    default boolean isWhiteSpace() {
        return Character.isWhitespace(current());
    }

    default boolean isLineTerminator() {
        return current() == 0xA || current() == 0xD;
    }

    default boolean isLetterOrDigit() {
        return Character.isLetterOrDigit(current());
    }

    default boolean isLineTerminator(boolean skip) {
        if (skip) {
            if (current() == 0xA) {
                return true;
            }

            if (current() == 0xD) {
                if (current() == 0xA) {
                    readChar();
                }
                return true;
            }

            return false;
        }

        return isLineTerminator();
    }

}
