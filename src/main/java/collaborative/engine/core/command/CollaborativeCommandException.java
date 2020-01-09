package collaborative.engine.core.command;

public class CollaborativeCommandException extends Exception {


    public CollaborativeCommandException(String message) {
        super(message);
    }

    public CollaborativeCommandException(String message, Throwable cause) {
        super(message, cause);
    }
}
