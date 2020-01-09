package collaborative.engine.core.command;

import collaborative.engine.core.Collaboratory;

public class HistoryCommand extends Command<Void> {

    public HistoryCommand(Collaboratory collaboratory) {
        super();
    }

    @Override
    public Void exec() throws CollaborativeCommandException {
        return null;
    }
}
