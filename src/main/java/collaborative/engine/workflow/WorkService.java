package collaborative.engine.workflow;

/**
 * Control and organize workflow(like filter).
 * Copy {@link java.util.stream.Stream} to learn how
 * to achieve.
 *
 * @author XyParaCrim
 */
public interface WorkService {

    default void join() {
        throw new UnsupportedOperationException();
    }

    default void finish(boolean immediately) {
        throw new UnsupportedOperationException();
    }

    void fork(Class<? extends Work> workClass);

    void fail(Throwable e);

    void parallel(Class<? extends Work.WorkSlot<? extends Work>> workSlotClass);

    void dispatcher(DispatcherWork dispatcherWork);
}
