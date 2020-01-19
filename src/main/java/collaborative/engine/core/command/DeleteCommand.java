package collaborative.engine.core.command;

import collaborative.engine.core.Collaboratory;

public class DeleteCommand extends SpecifiedCommand<Void> {

    protected DeleteCommand(Collaboratory collaboratory) {
        super(collaboratory);
    }

    @Override
    public Void exec() throws CollaborativeCommandException {
        return null;
    }
}
