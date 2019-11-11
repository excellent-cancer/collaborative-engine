package collaborative.engine.vcs;

import collaborative.engine.content.ContentProvider;
import collaborative.engine.operation.EditOperation;

// TEMP-CODE
public class CommitEngine {
    private final ContentProvider contentProvider;
    private volatile Commit updatedCommit;
    private volatile Commit headCommit;

    public CommitEngine(ContentProvider contentProvider) {
        this.contentProvider = contentProvider;
        this.headCommit = new Commit();
        ContentProvider.ContentInfo info = contentProvider.contentInfo();
        this.headCommit.prevDiff = EditOperation.from(info);
    }

    public void merge(EditOperation editOperation) {

    }

    public CommitHandle newCurrentHandle() {
        return null;
    }

    static class Commit {
        volatile Commit next;
        volatile EditOperation prevDiff;
    }

    class CommitHandle {}
}
