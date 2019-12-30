package collaborative.engine.workflow;

/**
 * Control and organize workflow(like filter).
 * Copy {@link java.util.stream.Stream} to learn how
 * to achieve.
 * @author XyParaCrim
 */
public interface Workflow extends AutoCloseable {

    void fork();

    void join();

    void fail(Throwable e);

    void finish();

    <T extends Work> void parallel(Class<Work.WorkSlot<T>> workSlotClass);
}
