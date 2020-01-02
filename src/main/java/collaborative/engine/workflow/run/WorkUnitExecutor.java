package collaborative.engine.workflow.run;

import collaborative.engine.workflow.Work;
import collaborative.engine.workflow.WorkProcessing;
import collaborative.engine.workflow.Workflow;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;
import java.util.stream.Collectors;

public class WorkUnitExecutor extends ForkJoinPool {

    private static final Logger LOGGER = LogManager.getLogger(WorkUnitExecutor.class);

    public void submit(Work work, Workflow workflow, WorkProcessing processing) {
        LOGGER.debug("submit new work: \"{}\"", work.getClass().getSimpleName());
        submit(new RecursiveWork(work, workflow, processing));
    }

    public void invoke(Work work, Workflow workflow, WorkProcessing processing) {
        LOGGER.debug("invoke new work: \"{}\"", work.getClass().getSimpleName());
        invoke(new RecursiveWork(work, workflow, processing));
    }

    public void invokeAll(List<Work> works, Workflow workflow, WorkProcessing processing) {
        ForkJoinTask.invokeAll(
                works.stream().
                        map(work -> new RecursiveWork(work, workflow, processing)).
                        collect(Collectors.toUnmodifiableList())
        );
    }

    private static class RecursiveWork extends RecursiveAction {
        final Work work;
        final Workflow workflow;
        final WorkProcessing processing;

        public RecursiveWork(Work work, Workflow workflow, WorkProcessing processing) {
            this.work = work;
            this.workflow = workflow;
            this.processing = processing;
        }

        @Override
        protected void compute() {
            try {
                work.proceed(processing, workflow);
            } catch (Exception e) {
                workflow.fail(e);
            }

            LOGGER.debug("done work: \"{}\"", work.getClass().getSimpleName());

            workflow.fork(work.getClass());
        }
    }
}
