package collaborative.engine.workflow;

/**
 * Control and organize workflow(like filter).
 * Copy {@link java.util.stream.Stream} to learn how
 * to achieve.
 * @author XyParaCrim
 */
public interface Workflow extends AutoCloseable {

    /**
     * Delegate to the next {@link Work} in the workflow.
     * @param next next work by order
     * @return return a equivalent workflow
     */
    Workflow then(Work next);

    /**
     * Work task failed to complete.
     * @param e the exception to commit, including its stack trace.
     * @return 战术返回
     */
    Workflow fail(Throwable e);

    Workflow fail();

    /**
     * Return a
     * @param target
     * @param first
     * @param second
     * @param more
     * @return
     */
    Workflow parallel(Work target, Work first, Work second, Work... more);

    Workflow end();
}
