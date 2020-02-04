package collaborative.engine.core;

import collaborative.engine.core.databse.FileDatabase;
import collaborative.engine.core.lock.ProgressLock;
import collaborative.engine.parameterize.FileParameterTable;

/**
 * 定义所有{@link Collaboratory}的所有组件及其可用级别。
 *
 * @author XyParaCrim
 */
@SuppressWarnings("unused")
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

    public ProgressLock progressLock() {
        return collaboratory.component(ProgressLock.class);
    }
}
