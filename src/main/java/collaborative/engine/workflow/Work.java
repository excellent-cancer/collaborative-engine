package collaborative.engine.workflow;

/**
 * Contract for interception-style, chained processing of workflow.
 * @author XyParaCrim
 * @scope part
 */
public interface Work {
    void proceed(WorkLocal workLocal, Workflow workflow);
}
