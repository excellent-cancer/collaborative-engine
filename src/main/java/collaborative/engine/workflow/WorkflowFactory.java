package collaborative.engine.workflow;

import collaborative.engine.parameterize.ParameterTable;
import collaborative.engine.workflow.config.Proceed;
import collaborative.engine.workflow.config.ProceedEachAfter;
import org.apache.logging.log4j.LogManager;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author XyParaCrim
 */
public final class WorkflowFactory {

    public static Workflow bootstrap(String configDirectory, Object workflowConfig) {
        WorkPool workPool = new WorkPool(workflowConfig);

        return new WorkflowImp(workPool);
    }


    private static class WorkPool {

        private final List<Method> startWork;

        private final Map<Class<? extends Work>, WorkInfo> workMap = new ConcurrentHashMap<>();

        private final Object configContext;

        public WorkPool(Object workflowConfig) {
            Objects.requireNonNull(workflowConfig);
            configContext = workflowConfig;
            startWork = new LinkedList<>();
            for (Method method : workflowConfig.getClass().getMethods()) {

                Proceed parallelStart = method.getAnnotation(Proceed.class);
                if (parallelStart != null && method.getReturnType().isAssignableFrom(Work.class)) {
                    startWork.add(method);
                }

                ProceedEachAfter proceedEachAfter = method.getAnnotation(ProceedEachAfter.class);
                if (proceedEachAfter != null && method.getReturnType().isAssignableFrom(Work.class)) {
                    Class<? extends Work>[] defaults = proceedEachAfter.value();
                    for (Class<? extends Work> workClass : defaults) {
                        WorkInfo workInfo = workMap.get(workClass);
                        if (workInfo == null) {
                            workInfo = new WorkInfo();
                        }

                        workInfo.addDefault(method);
                    }

                    Class<? extends Work.WorkSlot<? extends Work>>[] slots = proceedEachAfter.slots();


                }

            }

        }
    }

    private static class WorkInfo {


        public void addDefault(Method method) {
        }
    }

/*    public static Workflow bootstrap(Work work) {
        WorkflowImp workflow = new WorkflowImp();
        WorkProcessingImp workProcessing = new WorkProcessingImp(new ParameterTable());

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
    }*/


    public static Workflow empty() {
        return null;
    }

    private static class WorkProcessingImp extends WorkProcessing {

        public WorkProcessingImp(ParameterTable parameterStore) {
            super(parameterStore);
        }
    }

    private static class WorkflowImp implements Workflow {

        private final ConcurrentLinkedQueue<Work> queue = new ConcurrentLinkedQueue<>();

        public WorkflowImp(WorkPool workPool) {
        }

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
