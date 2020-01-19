package collaborative.engine.core.command;

public class CollaborativeCommandException extends Exception {


    public CollaborativeCommandException(String message) {
        super(message);
    }

    public CollaborativeCommandException(Throwable cause) {
        super(cause);
    }

    public CollaborativeCommandException(String message, Throwable cause) {
        super(message, cause);
    }
}
