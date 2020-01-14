package collaborative.engine.core;

import collaborative.engine.core.databse.FileDatabase;
import collaborative.engine.parameterize.FileParameterTable;

public final class CollaborativeComponents {
    private final Collaboratory collaboratory;

    public CollaborativeComponents(Collaboratory collaboratory) {
        this.collaboratory = collaboratory;
    }

    public FileDatabase fileStore() {
        return collaboratory.component(FileDatabase.class);
    }

    public FileParameterTable parameterTable() {
        return collaboratory.component(FileParameterTable.class);
    }
}
