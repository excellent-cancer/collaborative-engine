package collaborative.engine.core;

/**
 * @author XyParaCrim
 */
public class CollaborativeStatus {
    private final Collaboratory collaboratory;

    public CollaborativeStatus(Collaboratory collaboratory) {
        this.collaboratory = collaboratory;
    }

    public boolean qualified() {
        return collaboratory.analysisReport.qualifiedWorkSite &&
                collaboratory.analysisReport.qualifiedDirectory;
    }

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
