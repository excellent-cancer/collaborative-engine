package collaborative.engine.workflow;

import collaborative.engine.parameterize.ParameterVariables;
import org.apache.logging.log4j.LogManager;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author XyParaCrim
 */
public final class WorkflowFactory {

    public static Workflow bootstrap(Work work) {
        WorkflowImp workflow = new WorkflowImp();
        WorkProcessingImp workProcessing = new WorkProcessingImp(new ParameterVariables());

        workflow.queue.add(work);

        while (!workflow.queue.isEmpty()) {
            Work work1 = workflow.queue.poll();
            if (work1.check(workProcessing)) {
                work1.proceed(workProcessing, workflow);
            } else {
                break;
            }
        }

        workflow.close();

        return workflow;
    }


    public static Workflow empty() {
        return null;
    }

    private static class WorkProcessingImp extends WorkProcessing {

        public WorkProcessingImp(ParameterVariables parameterStore) {
            super(parameterStore);
        }
    }

    private static class WorkflowImp implements Workflow {

        private final ConcurrentLinkedQueue<Work> queue = new ConcurrentLinkedQueue<>();

        @Override
        public Workflow then(Work next) {
            queue.add(next);
            return this;
        }

        @Override
        public Workflow fail(Throwable e) {
            LogManager.getLogger().error(e);
            return this;
            // throw new UnsupportedOperationException();
        }

        @Override
        public Workflow fail() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Workflow parallel(Work target, Work first, Work second, Work... more) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Workflow end() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void close() {
            LogManager.getLogger().info("close");
        }
    }
}
