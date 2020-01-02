package collaborative.engine.workflow.run;

import collaborative.engine.workflow.DispatcherWork;
import collaborative.engine.workflow.Work;
import collaborative.engine.workflow.WorkProcessing;
import collaborative.engine.workflow.Workflow;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;
import java.util.stream.Collectors;

public class WorkflowExecutor extends ForkJoinPool {

    public void submit(@NotNull Work work, Workflow workflow, WorkProcessing processing) {
        submit(new RecursiveWork(work, workflow, processing));
    }

    public void submit(@NotNull DispatcherWork work, Workflow workflow, WorkProcessing processing) {
        submit(new DispatcherAction(work, workflow, processing));
    }

    public void invoke(@NotNull Work work, Workflow workflow, WorkProcessing processing) {
        invoke(new RecursiveWork(work, workflow, processing));
    }

    public void invokeAll(@NotNull List<Work> works, Workflow workflow, WorkProcessing processing) {
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
                processing.reportStartWork(work);
                work.proceed(processing, workflow);
            } catch (Exception e) {
                processing.reportFailedWork(work);
                workflow.fail(e);
            }

            processing.reportDoneWork(work);
            workflow.fork(work.getClass());
        }
    }

    private class DispatcherAction extends RecursiveAction {
        final DispatcherWork work;
        final Workflow workflow;
        final WorkProcessing processing;

        public DispatcherAction(DispatcherWork work, Workflow workflow, WorkProcessing processing) {
            this.work = work;
            this.workflow = workflow;
            this.processing = processing;
        }

        @Override
        protected void compute() {
            if (work.finished()) {
                processing.reportDoneWork(work);
            } else {
                try {
                    processing.reportStartWork(work);
                    work.proceed(processing, workflow);
                } catch (Exception e) {
                    processing.reportFailedWork(work);
                    workflow.fail(e);
                }

                processing.reportContinueWork(work);
                WorkflowExecutor.this.submit(work, workflow, processing);
            }
        }
    }
}
