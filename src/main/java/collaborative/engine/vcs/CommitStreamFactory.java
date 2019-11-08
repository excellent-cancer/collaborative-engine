package collaborative.engine.vcs;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Flow;

public interface CommitStreamFactory extends Flow.Publisher<Commit> {

    CommitStream newCommitStream();

    @Override
    void subscribe(Flow.Subscriber<? super Commit> subscriber);
}
