package collaborative.engine.core.command;

import collaborative.engine.core.Collaboratory;

public class RemoveCollabortativeCommand extends SpecifiedCommand<Void> {

    protected RemoveCollabortativeCommand(Collaboratory collaboratory) {
        super(collaboratory);
    }

    @Override
    public Void exec() throws CollaborativeCommandException {
        return null;
    }
}
