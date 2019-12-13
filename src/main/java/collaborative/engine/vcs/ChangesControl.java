package collaborative.engine.vcs;

import collaborative.engine.operation.EditOperation;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ChangesControl {

    private static final String EMPTY_VERSION = "*";

    private volatile Change head;

    private volatile Change tail;

    private final ConcurrentLinkedQueue<StringBuffer> linesBuffer = new ConcurrentLinkedQueue<>();

    public ChangesControl() {
        //aliveChanges.put(EMPTY_VERSION, head = tail = Change.make());
    }

    public void commitEditOperation(EditOperation editOperation) {
        Objects.requireNonNull(editOperation);

        Change thisHead = head;
        Change newChange = Change.make(thisHead);

        // check if parent change exits
        String parentChangeVersion = editOperation.getBaseVersion();
        if (thisHead.versionId.equals(parentChangeVersion)) {


        } else if (thisHead.aliveChanges.containsKey(parentChangeVersion)) {

        } else {
            throw new IllegalStateException();
        }
    }

    private static final class Change {
        volatile Change child;
        volatile Change parent;
        volatile Map<String, Change> aliveChanges = Collections.emptyMap();

        volatile String versionId;

        static Change make() {
            return make(null);
        }

        static Change make(Change parent) {
            Change newChange = new Change();
            newChange.parent = parent;
            return newChange;
        }
    }

    private static final class Diff {
        private String[] content;
        private EditOperation.Position start;
        private EditOperation.Position end;
    }
}
