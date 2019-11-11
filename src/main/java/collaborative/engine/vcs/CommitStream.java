package collaborative.engine.vcs;

import java.util.function.Consumer;

public interface CommitStream {
    void pause();

    void resume();

    void close();

    CommitStream onNewVersion(Consumer<CommitInfo> action);

    CommitStream onClose(Runnable closeHandler);

    CommitStream currentCommit(Consumer<CommitInfo> action);

    CommitStream moveTo(CommitInfo commitInfo);
}
