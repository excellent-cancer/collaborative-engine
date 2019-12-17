package collaborative.engine;

/**
 * @author XyParaCrim
 */
public final class BufferSupport {

    public static final int BUFFER_SIZE = 1025;

    public static char[] newBuffer() {
        return new char[BUFFER_SIZE];
    }
}
