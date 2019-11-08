package collaborative.engine;

import collaborative.engine.vcs.EditVersionControl;
import org.jetbrains.annotations.Contract;

import java.nio.file.Path;

/**
 * Collaborative engine facade
 */
@SuppressWarnings("unused")
public final class CollaborativeEngine {

    @Contract(pure = true)
    private CollaborativeEngine() {
    }

    public static EditVersionControl newEditVersionControl(Path path) {
        return EditVersionControl.newBuilder()
                .path(path)
                .build();
    }

    public static EditVersionControl newEditVersionControl(String path) {
        return EditVersionControl.newBuilder()
                .path(path)
                .build();
    }
}
