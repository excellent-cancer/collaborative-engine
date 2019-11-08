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

    private static final AtomicReferenceFieldUpdater<CommitStreamImpl, Commit> COMMIT_UPDATER =
            AtomicReferenceFieldUpdater.newUpdater(CommitStreamImpl.class, Commit.class, "referenceCommit");

    private final UUID uuid = UUID.randomUUID();

    private final CommitSubscriber commitSubscriber = new CommitSubscriber();
    private volatile Commit referenceCommit = null;
    private ConcurrentLinkedQueue<Runnable> closeActions = new ConcurrentLinkedQueue<>();
    private ConcurrentLinkedQueue<Consumer<Commit>> newVersionActions = new ConcurrentLinkedQueue<>();

    private volatile boolean closed = false;

    @Override
    Flow.Subscriber<Commit> subscriber() {
        return commitSubscriber;
    }

    @Override
    public CommitStream onNewVersion(@NotNull Consumer<Commit> action) {
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
    public CommitStream currentCommit(@NotNull Consumer<Commit> action) {
        predicateClosed();
        Objects.requireNonNull(action);
        action.accept(commitSubscriber.latestCommit);
        return this;
    }

    @Override
    public CommitStream moveTo(@NotNull Commit commit) {
        COMMIT_UPDATER.set(this, commit);
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

    class CommitSubscriber implements Flow.Subscriber<Commit> {
        private volatile Flow.Subscription subscription;
        private volatile Commit latestCommit = null;
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
        public void onNext(Commit item) {
            // TODO 这里绝对不正确
            this.latestCommit = item;
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
