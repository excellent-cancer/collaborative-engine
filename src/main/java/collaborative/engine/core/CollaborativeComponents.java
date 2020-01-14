package collaborative.engine.core;

import collaborative.engine.parameterize.FileParameterTable;

public final class CollaborativeComponents {
    private final Collaboratory collaboratory;

    public CollaborativeComponents(Collaboratory collaboratory) {
        this.collaboratory = collaboratory;
    }

    public FileStore fileStore() {
        return collaboratory.component(FileStore.class);
    }

    public FileParameterTable parameterTable() {
        return collaboratory.component(FileParameterTable.class);
    }
}
