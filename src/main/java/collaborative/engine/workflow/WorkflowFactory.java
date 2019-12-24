package collaborative.engine.workflow;

import collaborative.engine.parameterize.ParameterTable;
import collaborative.engine.workflow.config.Proceed;
import collaborative.engine.workflow.config.ProceedAllAfter;
import collaborative.engine.workflow.config.ProceedEachAfter;
import org.apache.logging.log4j.LogManager;
import pact.support.ExecutorSupport;
import pact.support.ReflectSupport;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;

import static collaborative.engine.ParameterGroup.CONFIG_DIRECTORY;

/**
 * @author XyParaCrim
 */
public final class WorkflowFactory {

    public static Workflow bootstrap(String configDirectory, Object workflowConfig) {
        return new WorkflowImp(WorkPool.load(workflowConfig), WorkProcessingImp.make(configDirectory));
    }

    private static class WorkPool {

        private static boolean legalMethod(Method method) {
            return method.getReturnType().isAssignableFrom(Work.class);
        }

        private final List<Method> startWork = new LinkedList<>();

        private final Map<Class<? extends Work>, List<Method>> defaultSlotsMap = new ConcurrentHashMap<>();

        private final Map<Class<? extends Work.WorkSlot<? extends Work>>, List<Method>> nameSlotsMap = new ConcurrentHashMap<>();

        private final Object configContext;

        public WorkPool(Object workflowConfig) {
            this.configContext = workflowConfig;
            ReflectSupport.walkMethod(workflowConfig, WorkPool::legalMethod, method -> {
                ReflectSupport.ifAnnotated(method, Proceed.class, this::resolveProceed);
                ReflectSupport.ifAnnotated(method, ProceedAllAfter.class, this::resolveProceedAllAfter);
                ReflectSupport.ifAnnotated(method, ProceedEachAfter.class, this::resolveProceedEachAfter);
            });
        }

        public <T extends Work> void doneWork(Class<T> workClass) {
            defaultSlotsMap.getOrDefault(workClass, Collections.emptyList())
                    .forEach(method -> {
                        try {
                            method.invoke(configContext);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    });
        }

        private void resolveProceed(Proceed proceed, Method method) {
            startWork.add(method);
        }

        private void resolveProceedEachAfter(ProceedEachAfter proceedEachAfter, Method method) {
            for (Class<? extends Work> defaultSlot : proceedEachAfter.value()) {
                List<Method> methods = defaultSlotsMap.get(defaultSlot);
                if (methods == null) {
                    methods = new LinkedList<>();
                }
                methods.add(method);
            }

            for (Class<? extends Work.WorkSlot<? extends Work>> nameSlot : proceedEachAfter.slots()) {
                List<Method> methods = nameSlotsMap.get(nameSlot);
                if (methods == null) {
                    methods = new LinkedList<>();
                }
                methods.add(method);
            }
        }

        private void resolveProceedAllAfter(ProceedAllAfter proceedAllAfter, Method method) {
            // throw new UnsupportedOperationException();
        }

        private static WorkPool load(Object workflowConfig) {
            return new WorkPool(workflowConfig);
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

        private static WorkProcessing make(String configDirectory) {
            ParameterTable parameterTable = new ParameterTable();
            CONFIG_DIRECTORY.set(parameterTable, Path.of(configDirectory));
            return new WorkProcessingImp(parameterTable);
        }
    }

    private static class WorkflowImp implements Workflow {

        private final ConcurrentLinkedQueue<Work> queue = new ConcurrentLinkedQueue<>();

        private final WorkPool workPool;

        private final WorkProcessing workProcessing;

        private final ExecutorService executorService = ExecutorSupport.singleOnlyThreadExecutor();

        public WorkflowImp(WorkPool workPool, WorkProcessing workProcessing) {
            this.workPool = workPool;
            this.workProcessing = workProcessing;

            workPool.startWork.forEach(method -> {
                try {
                    method.invoke(workPool.configContext);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            });
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
