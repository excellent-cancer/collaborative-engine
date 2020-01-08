package collaborative.engine.core;

import collaborative.engine.core.command.HistoryCommand;
import collaborative.engine.core.command.InitCommand;
import collaborative.engine.core.command.StatusCommand;

public class CollaborativeCommands {

    private final Collaboratory collaboratory;

    public CollaborativeCommands(Collaboratory collaboratory) {
        this.collaboratory = collaboratory;
    }

    // Collaboratory commands

    public HistoryCommand history() {
        return new HistoryCommand(collaboratory);
    }

    public StatusCommand status() {
        return new StatusCommand(collaboratory);
    }

    // Global commands

    public static InitCommand init() {
        return new InitCommand();
    }
}
