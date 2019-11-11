package collaborative.engine.vcs;

import java.util.concurrent.Flow;

public abstract class AbstractCommitStream implements CommitStream {

    abstract Flow.Subscriber<CommitInfo> subscriber();

}
