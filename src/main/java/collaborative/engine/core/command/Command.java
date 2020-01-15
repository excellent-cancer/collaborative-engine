package collaborative.engine.core.command;

import pact.support.FunctionSupport;

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

    protected final T once(FunctionSupport.SupplierWithException<T, Exception> exec) throws CollaborativeCommandException {
        try {
            validate();
            return exec.get();
        } catch (Exception e) {
            throw new CollaborativeCommandException(e.getMessage(), e);
        } finally {
            executed();
        }
    }
}
