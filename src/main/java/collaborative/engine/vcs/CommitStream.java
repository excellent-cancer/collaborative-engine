package collaborative.engine.vcs;

import java.util.function.Consumer;

public interface CommitStream {
    void pause();

    void resume();

    void close();

    CommitStream onNewVersion(Consumer<Commit> action);

    CommitStream onClose(Runnable closeHandler);

    CommitStream currentCommit(Consumer<Commit> action);

    CommitStream moveTo(Commit commit);
}
