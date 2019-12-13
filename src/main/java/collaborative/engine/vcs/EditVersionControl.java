package collaborative.engine.vcs;

import collaborative.engine.DisposableService;
import collaborative.engine.content.ContentProvider;
import collaborative.engine.content.ContentProviderSupport;
import collaborative.engine.operation.EditOperation;
import collaborative.engine.operation.EditOperationRequest;

import java.nio.file.Path;
import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.function.Function;
import java.util.function.Supplier;

import static java.util.concurrent.Flow.Publisher;
import static java.util.concurrent.Flow.Subscriber;

/**
 * 代表版本控制的对象
 * @author XyParaCrim
 */
public class EditVersionControl extends DisposableService implements Publisher<CommitInfo> {

    private final ContentProvider contentProvider;
    private final CommitStreamFactory commitStreamFactory;
    private final ChangesControl changesControl = new ChangesControl();

    private ConcurrentLinkedQueue<EditOperation> editOperations;

    private Dispatcher dispatcher;
    private Thread mainThread;

    private EditVersionControl(EditVersionControlBuilder builder) {
        this.contentProvider = builder.contentProviderSupplier.get();
        this.commitStreamFactory = builder.commitStreamFactoryProvider.apply(this);
    }

    public CommitStream newCommitStream() {
        return commitStreamFactory.newCommitStream();
    }

    @Override
    public void subscribe(Subscriber<? super CommitInfo> subscriber) {

    }

    @Override
    public void start() {
        if (lifecycle.tryStarted()) {
            dispatcher = new Dispatcher();
            mainThread = new Thread(null, dispatcher, "edit-process", 0, false);
            mainThread.start();
        }
    }

    @Override
    public void close() {
        if (lifecycle.tryClosed()) {
            mainThread.isInterrupted();
        }
    }

    public void commit(EditOperationRequest request) {
        if (!lifecycle.isStarted()) {
            throw new IllegalStateException();
        }
        Objects.requireNonNull(request);
        dispatcher.operationRequests.add(request);

    }

    public static EditVersionControlBuilder newBuilder() {
        return new EditVersionControlBuilder();
    }

    private class Dispatcher implements Runnable {

        PriorityBlockingQueue<EditOperationRequest> operationRequests = new PriorityBlockingQueue<>(128, (t1, t2) -> t1.getModifyTime() > t2.getModifyTime() ? 1 : 0);

        @Override
        public void run() {
/*            while (!lifecycle.isClose()) {
                EditOperation operation = operationRequests.poll();
                if (operation != null) {

                }
            }*/
        }
    }

    @SuppressWarnings("unused")
    public static class EditVersionControlBuilder {
        private Function<EditVersionControl, CommitStreamFactory> commitStreamFactoryProvider = DefaultsSupport.DEFAULT_COMMIT_STREAM_FACTORY_PROVIDER;
        private Supplier<ContentProvider> contentProviderSupplier;

        public EditVersionControlBuilder path(Path contentPath) {
            this.contentProviderSupplier = ContentProviderSupport.cachedProvider(contentPath);
            return this;
        }

        public EditVersionControlBuilder path(String contentPathStr) {
            this.contentProviderSupplier = ContentProviderSupport.cachedProvider(contentPathStr);
            return this;
        }

        public EditVersionControlBuilder cachedCommitStream() {
            this.commitStreamFactoryProvider = DefaultsSupport.DEFAULT_COMMIT_STREAM_FACTORY_PROVIDER;
            return this;
        }

        public EditVersionControl build() {
            Objects.requireNonNull(contentProviderSupplier);
            Objects.requireNonNull(commitStreamFactoryProvider);

            return new EditVersionControl(this);
        }
    }
}
