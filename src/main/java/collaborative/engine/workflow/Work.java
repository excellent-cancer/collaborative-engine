package collaborative.engine.workflow;

/**
 * Contract for interception-style, chained processing of workflow.
 * @author XyParaCrim
 * @scope part
 */
public interface Work {

    /**
     * Process the work task and delegate to the next work
     * through the given {@link Workflow}.Must follow the
     * convention：
     *  1.Don't log any exception.
     *  2.Don't throw any exception, if so, indicate it's not
     *  qualified.
     * @param workLocal the current workflow context
     * @param workflow provides a way to delegate to the next work
     */
    Workflow proceed(WorkLocal workLocal, Workflow workflow);
}
