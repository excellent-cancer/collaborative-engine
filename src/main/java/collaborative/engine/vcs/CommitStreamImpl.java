package collaborative.engine.vcs;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Flow;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.util.function.Consumer;

import static java.util.Objects.requireNonNull;

public class CommitStreamImpl extends AbstractCommitStream {

    private static final AtomicReferenceFieldUpdater<CommitStreamImpl, CommitInfo> COMMIT_UPDATER =
            AtomicReferenceFieldUpdater.newUpdater(CommitStreamImpl.class, CommitInfo.class, "referenceCommitInfo");

    private final UUID uuid = UUID.randomUUID();

    private final CommitSubscriber commitSubscriber = new CommitSubscriber();
    private volatile CommitInfo referenceCommitInfo = null;
    private ConcurrentLinkedQueue<Runnable> closeActions = new ConcurrentLinkedQueue<>();
    private ConcurrentLinkedQueue<Consumer<CommitInfo>> newVersionActions = new ConcurrentLinkedQueue<>();

    private volatile boolean closed = false;

    @Override
    Flow.Subscriber<CommitInfo> subscriber() {
        return commitSubscriber;
    }

    @Override
    public CommitStream onNewVersion(@NotNull Consumer<CommitInfo> action) {
        predicateClosed();
        requireNonNull(action);
        newVersionActions.add(action);
        return this;
    }

    @Override
    public CommitStream onClose(@NotNull Runnable action) {
        predicateClosed();
        requireNonNull(action);
        closeActions.add(action);
        return this;
    }

    @Override
    public CommitStream currentCommit(@NotNull Consumer<CommitInfo> action) {
        predicateClosed();
        Objects.requireNonNull(action);
        action.accept(commitSubscriber.latestCommitInfo);
        return this;
    }

    @Override
    public CommitStream moveTo(@NotNull CommitInfo commitInfo) {
        COMMIT_UPDATER.set(this, commitInfo);
        return this;
    }

    @Override
    public void pause() {
        predicateClosed();
        commitSubscriber.pauseRequest();
    }

    @Override
    public void resume() {
        predicateClosed();
        commitSubscriber.request();
    }

    @Override
    public synchronized void close() {
        closed = true;
        closeActions.forEach(Runnable::run);
    }

    private void predicateClosed() {
        if (closed) {
            throw new IllegalStateException("The commit stream has already been closed");
        }
    }

    class CommitSubscriber implements Flow.Subscriber<CommitInfo> {
        private volatile Flow.Subscription subscription;
        private volatile CommitInfo latestCommitInfo = null;
        private final AtomicBoolean subscribed = new AtomicBoolean();
        private final AtomicBoolean currentSubscribed = new AtomicBoolean();

        private void request() {
            if (subscribed.get() && currentSubscribed.compareAndSet(false, true)) {
                subscription.request(1);
            }
        }

        private void pauseRequest() {
            currentSubscribed.set(false);
        }

        @Override
        public void onSubscribe(Flow.Subscription subscription) {
            Objects.requireNonNull(subscription);
            if (subscribed.compareAndSet(false, true)) {
                this.subscription = subscription;
                onClose(subscription::cancel);
                request();
            } else {
                subscription.cancel();
            }
        }

        @Override
        public void onNext(CommitInfo item) {
            // TODO 这里绝对不正确
            this.latestCommitInfo = item;
            if (currentSubscribed.compareAndSet(true, false)) {
                newVersionActions.forEach(action -> action.accept(item));
            }
        }

        @Override
        public void onError(Throwable throwable) {

        }

        @Override
        public void onComplete() {

        }
    }
}
