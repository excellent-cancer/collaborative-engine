package collaborative.engine.vcs;

import java.util.concurrent.Flow;
import java.util.function.Function;

final class DefaultsSupport {

    final static Function<EditVersionControl, CommitStreamFactory> DEFAULT_COMMIT_STREAM_FACTORY_PROVIDER = DefaultCommitFactory::new;


    static class DefaultCommitFactory implements CommitStreamFactory {

        private final EditVersionControl versionControl;
        // private final ConcurrentLinkedQueue<DefaultSubscription> subscriptions = new ConcurrentLinkedQueue<>();

        DefaultCommitFactory(EditVersionControl versionControl) {
            this.versionControl = versionControl;
        }

        @Override
        public CommitStream newCommitStream() {
            AbstractCommitStream commitStream = new CommitStreamImpl();
            subscribe(commitStream.subscriber());
            return commitStream;
        }

        @Override
        public void subscribe(Flow.Subscriber<? super CommitInfo> subscriber) {
            subscriber.onSubscribe(new DefaultSubscription(subscriber));
        }

        class DefaultSubscription implements Flow.Subscription {
            private final Flow.Subscriber<? super CommitInfo> commitSubscriber;
            private volatile boolean completed;
            private volatile boolean cancelled;

            DefaultSubscription(Flow.Subscriber<? super CommitInfo> commitSubscriber) {
                this.commitSubscriber = commitSubscriber;
            }

            @Override
            public void request(long n) {
                if (cancelled) return;
                versionControl.newCommitStream();
                // 获取最新版本，然后commitSubscriber.onNext
            }

            @Override
            public void cancel() {

            }
        }
    }

}
