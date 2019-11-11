package collaborative.engine.vcs;

import java.util.concurrent.Flow;

public interface CommitStreamFactory extends Flow.Publisher<CommitInfo> {

    CommitStream newCommitStream();

    @Override
    void subscribe(Flow.Subscriber<? super CommitInfo> subscriber);
}
