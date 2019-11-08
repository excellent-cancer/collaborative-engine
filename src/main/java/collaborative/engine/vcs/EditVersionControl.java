package collaborative.engine.vcs;

import collaborative.engine.content.ContentProvider;
import collaborative.engine.content.ContentProviderSupport;
import collaborative.engine.operation.EditOperationRequest;

import java.nio.file.Path;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author XyParaCrim
 */
public class EditVersionControl {

    private final ContentProvider contentProvider;
    private final CommitStreamFactory commitStreamFactory;

    private EditVersionControl(EditVersionControlBuilder builder) {
        this.contentProvider = builder.contentProviderSupplier.get();
        this.commitStreamFactory = builder.commitStreamFactoryProvider.apply(this);
    }

    public CommitStream newCommitStream() {
        return commitStreamFactory.newCommitStream();
    }

    public void handle(EditOperationRequest request) {
    }

    public static EditVersionControlBuilder newBuilder() {
        return new EditVersionControlBuilder();
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
