package collaborative.engine.content.core;

public interface ContentReader {

    char current();

    void readChar();

    default boolean isCommentSign() {
        return current() == '#';
    }

    default boolean isSplitCharater() {
        return current() == ':';
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
        if (!skip) {
            return isLineTerminator();
        }

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

}
