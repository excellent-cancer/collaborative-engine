package collaborative.engine.workflow.run;

import collaborative.engine.workflow.Work;

/**
 * Control and organize workflow(like filter).
 * Copy {@link java.util.stream.Stream} to learn how
 * to achieve.
 *
 * @author XyParaCrim
 */
public interface WorkService {

    void fork();

    void join();

    void fail(Throwable e);

    void finish();

    void parallel(Class<? extends Work.WorkSlot<? extends Work>> workSlotClass);

    boolean pending2exit();
}
