package collaborative.engine.vcs;

import collaborative.engine.DisposableService;
import collaborative.engine.content.ContentProvider;
import collaborative.engine.content.ContentProviderSupport;
import collaborative.engine.operation.EditOperation;
import collaborative.engine.operation.EditOperationRequest;

import java.nio.file.Path;
import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 代表版本控制的对象
 * @author XyParaCrim
 */
public class EditVersionControl extends DisposableService {

    private final ContentProvider contentProvider;
    private final CommitStreamFactory commitStreamFactory;

    /* 战术乱写 */

    private EditVersionControl(EditVersionControlBuilder builder) {
        this.contentProvider = builder.contentProviderSupplier.get();
        this.commitStreamFactory = builder.commitStreamFactoryProvider.apply(this);
    }

    public CommitStream newCommitStream() {
        return commitStreamFactory.newCommitStream();
    }

    private ConcurrentLinkedQueue<EditOperation> handlerQueue;

    @Override
    public void start() {
        if (lifecycle.tryStarted()) {
            handlerQueue = new ConcurrentLinkedQueue<>();
            Runnable process = new Dispatcher();
            Thread processThread = new Thread(null, process, "edit-process", 0, false);
            processThread.start();
        }
    }

    @Override
    public void close() {

    }

    /* 乱写 */
    public void handle(EditOperationRequest request) {
        handlerQueue.add(EditOperation.from(request));
    }

    public static EditVersionControlBuilder newBuilder() {
        return new EditVersionControlBuilder();
    }

    private class Dispatcher implements Runnable {
        @Override
        public void run() {
            while (!lifecycle.isClose()) {
                EditOperation operation = handlerQueue.poll();
                if (operation != null) {

                }
            }
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
