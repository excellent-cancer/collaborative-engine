package collaborative.engine.core.command;

import collaborative.engine.core.CollaborativeComponents;
import collaborative.engine.core.CollaborativeStatus;
import collaborative.engine.core.Collaboratory;

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
}
