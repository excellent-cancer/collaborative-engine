package collaborative.engine.core.command;

import collaborative.engine.core.Collaboratory;

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

    public CreateFileCommand createFile() {
        return new CreateFileCommand(collaboratory);
    }

    // Global commands

    public static InitCommand init() {
        return new InitCommand();
    }
}
