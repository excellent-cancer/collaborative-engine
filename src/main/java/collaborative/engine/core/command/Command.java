package collaborative.engine.core.command;

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class Command<T> {

    private final AtomicBoolean executed = new AtomicBoolean();

    protected void validate() {
        if (executed.get()) {
            throw new IllegalStateException("The command was executed in wrong state");
        }
    }

    protected void executed() {
        executed.set(true);
    }

    public abstract T exec() throws CollaborativeCommandException;
}
