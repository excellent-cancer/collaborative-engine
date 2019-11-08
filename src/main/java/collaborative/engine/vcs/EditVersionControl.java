package collaborative.engine.vcs;

import collaborative.engine.content.ContentProvider;
import collaborative.engine.operation.EditOperationRequest;

import java.nio.file.Path;
import java.util.function.Supplier;

/**
 * @author XyParaCrim
 */
public class EditVersionControl {

    private EditVersionControl(EditVersionControlBuilder builder) {
    }

    public static EditVersionControlBuilder newBuilder() {
        return new EditVersionControlBuilder();
    }

    public CommitStream newCommitStream() {
        return null;
    }

    public void handle(EditOperationRequest request) {
    }

    public static class EditVersionControlBuilder {
        private Supplier<ContentProvider> contentProviderSupplier;

        public EditVersionControlBuilder path(Path contentPath) {
            return this;
        }

        public EditVersionControlBuilder path(String contentPathStr) {
            return this;
        }

        public EditVersionControl build() {
            return new EditVersionControl(this);
        }
    }
}
