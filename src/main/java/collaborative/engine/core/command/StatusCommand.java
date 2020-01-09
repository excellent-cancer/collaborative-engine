package collaborative.engine.core.command;

import collaborative.engine.core.Collaboratory;

public class StatusCommand extends Command<Void> {

    public StatusCommand(Collaboratory collaboratory) {
        super();
    }

    @Override
    public Void exec() throws CollaborativeCommandException {
        return null;
    }
}
