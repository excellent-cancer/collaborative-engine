package collaborative.engine.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;

public abstract class Collaboratory implements AutoCloseable {

    private static final Logger LOGGER = LogManager.getLogger(Collaboratory.class);

    // critical of collaboratory

    private final File collaborativeFile;

    protected Collaboratory(CollaboratoryBuilder options) {
        this.collaborativeFile = options.getCollaborativeFile();
    }

    public abstract void create() throws IOException;

    public abstract CollaborativeCommands commands();
}
