package collaborative.engine.core;

/**
 * @author XyParaCrim
 */
@SuppressWarnings("MethodCanBeVoid")
public class CollaborativeStatus {
    private final Collaboratory collaboratory;

    public CollaborativeStatus(Collaboratory collaboratory) {
        this.collaboratory = collaboratory;
    }

    // Status query method

    public boolean qualified() {
        return collaboratory.analysisReport.qualifiedWorkSite &&
                collaboratory.analysisReport.qualifiedDirectory;
    }

    // Checks that the specified status

    public CollaborativeStatus requireQualified() {
        if (!qualified()) {
            throw new IllegalStateException();
        }
        return this;
    }

    public CollaborativeStatus requireMutex() {
        return this;
    }
}
