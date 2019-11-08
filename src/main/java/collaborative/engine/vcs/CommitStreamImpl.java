package collaborative.engine.vcs;

import java.util.function.Consumer;

public class CommitStreamImpl implements CommitStream {
    @Override
    public CommitStream onNext(Consumer<Commit> action) {
        return null;
    }

    @Override
    public CommitStream onClose(Runnable closeHandler) {
        return null;
    }

    @Override
    public CommitStream currentCommit(Consumer<Commit> action) {
        return null;
    }

    @Override
    public CommitStream moveTo(Commit commit) {
        return null;
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void close() {

    }
}
