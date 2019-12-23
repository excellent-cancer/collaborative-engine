package collaborative.engine.workflow;

import pact.etc.CheckFor;

/**
 * Contract for interception-style, chained processing of workflow.
 * @author XyParaCrim
 * @scope part
 */
public interface Work extends CheckFor<WorkProcessing> {

    /**
     * Process the work task and delegate to the next work
     * through the given {@link Workflow}.Must follow the
     * convention：
     *  1.Don't log any exception.
     *  2.Don't throw any exception, if so, indicate it's not
     *  qualified.
     * @param workProcessing the current workflow context
     * @param workflow provides a way to delegate to the next work
     */
    Workflow proceed(WorkProcessing workProcessing, Workflow workflow);

    interface UncheckedWork extends Work {
        @Override
        default boolean check(WorkProcessing processing) {
            return true;
        }
    }

    interface OnlyCheckWork extends Work {
        @Override
        default Workflow proceed(WorkProcessing workProcessing, Workflow workflow) {
            return nextWork(workProcessing, workflow);
        }

        Workflow nextWork(WorkProcessing workProcessing, Workflow workflow);
    }

    interface WorkSlot<T extends Work> {
        Class<T> from();
    }

    static <T extends Work> String qualified(Class<T> tClass, String name) {
        return tClass.getSimpleName() + "." + name;
    }
}
