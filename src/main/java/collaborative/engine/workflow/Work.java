package collaborative.engine.workflow;

import pact.support.CharSequenceSupport;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Contract for interception-style, chained processing of workflow.
 *
 * @author XyParaCrim
 * @scope part
 */
@SuppressWarnings("unused")
public abstract class Work {

    private final long wid;
    private final String name;

    public Work() {
        this("UnknownWork-" + nextUnknownWorkNumber());
    }

    public Work(String name) {
        this.wid = nextWorkID();
        this.name = CharSequenceSupport.pascal(name);
    }

    // Main replayed methods

    /**
     * Process the work task and delegate to the next work
     * through the given {@link Workflow}.Must follow the
     * convention：
     * 1.Don't log any exception.
     * 2.Don't throw any exception, if so, indicate it's not
     * qualified.
     *
     * @param workProcessing the current workflow context
     * @param workflow       provides a way to delegate to the next work
     */
    public abstract void proceed(WorkProcessing workProcessing, Workflow workflow);

    // Final public methods

    public final long getId() {
        return wid;
    }

    public final String tagName() {
        return name + "@" + wid;
    }

    // Static fields

    private static final AtomicLong WORK_GENERATED_NUMBER = new AtomicLong();

    private static long nextWorkID() {
        return WORK_GENERATED_NUMBER.addAndGet(1L);
    }

    private static final AtomicInteger UNKNOWN_WORK_NUMBER = new AtomicInteger();

    private static int nextUnknownWorkNumber() {
        return UNKNOWN_WORK_NUMBER.addAndGet(1);
    }

    public interface WorkSlot<T extends Work> {
    }
}
