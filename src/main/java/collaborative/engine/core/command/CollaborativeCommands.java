package collaborative.engine.core.command;

import collaborative.engine.core.Collaboratory;
import collaborative.engine.core.Defaults;


/**
 * Collaborative的所有命令合集
 *
 * @author XyParaCrim
 */
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
        return new CreateFileCommand(collaboratory, Defaults.DEFAULT_EXTENSION);
    }

    public RemoveFileCommand removeFile() {
        return new RemoveFileCommand(collaboratory);
    }

    public DeleteCommand delete() {
        return new DeleteCommand(collaboratory);
    }

    // Global commands

    public static InitCommand init() {
        return new InitCommand();
    }
}
