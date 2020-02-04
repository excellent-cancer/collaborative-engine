package collaborative.engine.core.command;

import collaborative.engine.core.CollaborativeComponents;
import collaborative.engine.core.CollaborativeStatus;
import collaborative.engine.core.Collaboratory;
import pact.support.ExceptionSupport;
import pact.support.FunctionSupport;

import java.io.Closeable;

abstract class SpecifiedCommand<T> extends Command<T> {

    protected final Collaboratory collaboratory;

    protected SpecifiedCommand(Collaboratory collaboratory) {
        this.collaboratory = collaboratory;
    }

    protected CollaborativeStatus status() {
        return collaboratory.status();
    }

    protected CollaborativeCommands commands() {
        return collaboratory.commands();
    }

    protected CollaborativeComponents components() {
        return collaboratory.components();
    }

    protected final T onceAndCloseIfException(FunctionSupport.SupplierWithException<T, Exception> exec) throws CollaborativeCommandException {
        boolean success = true;
        try {
            validate();
            return exec.get();
        } catch (Exception e) {
            success = false;
            throw new CollaborativeCommandException(e.getMessage(), e);
        } finally {
            executed();
            if (!success) {
                ExceptionSupport.closeWithExceptionHanding((Closeable) collaboratory);
            }
        }
    }
}
