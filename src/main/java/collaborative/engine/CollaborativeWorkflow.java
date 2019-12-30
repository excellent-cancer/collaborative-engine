package collaborative.engine;


import collaborative.engine.parameterize.ParameterTable;
import collaborative.engine.workflow.Work;
import collaborative.engine.workflow.WorkProcessing;
import collaborative.engine.workflow.Workflow;
import collaborative.engine.workflow.config.Proceed;
import collaborative.engine.workflow.config.ProceedAllAfter;
import collaborative.engine.workflow.config.ProceedEachAfter;
import collaborative.engine.workflow.run.WorkflowExecutor;
import pact.support.ReflectSupport;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

final class CollaborativeWorkflow {


    static Workflow bootstrap(CollaborativeCarcinogen carcinogen) {
        Objects.requireNonNull(carcinogen);

        return new WorkflowImp(CarcinogenFactor.analysis(carcinogen));
    }

    private static class CarcinogenFactor {

        private final CollaborativeCarcinogen carcinogen;

        private final List<Method> startWork = new LinkedList<>();

        private final Map<Class<? extends Work>, List<Method>> defaultSlotsMap = new ConcurrentHashMap<>();

        private final Map<Class<? extends Work.WorkSlot<? extends Work>>, List<Method>> nameSlotsMap = new ConcurrentHashMap<>();

        public CarcinogenFactor(CollaborativeCarcinogen carcinogen) {
            this.carcinogen = carcinogen;
            ReflectSupport.walkMethod(carcinogen, CarcinogenFactor::legalMethod, method -> {
                ReflectSupport.ifAnnotated(method, Proceed.class, this::resolveProceed);
                ReflectSupport.ifAnnotated(method, ProceedAllAfter.class, this::resolveProceedAllAfter);
                ReflectSupport.ifAnnotated(method, ProceedEachAfter.class, this::resolveProceedEachAfter);
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

        private Work newWork(Method method) {
            Work work = null;
            if (method != null) {
                try {
                    work = (Work) method.invoke(carcinogen);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }

            return work;
        }

        private static CarcinogenFactor analysis(CollaborativeCarcinogen carcinogen) {
            return new CarcinogenFactor(carcinogen);
        }

        private static boolean legalMethod(Method method) {
            return method.getReturnType().isAssignableFrom(Work.class);
        }

    }

    private static class WorkflowImp implements Workflow {

        private final WorkflowExecutor workflowExecutor;
        private final CarcinogenFactor carcinogenFactor;
        private final WorkProcessing workProcessing;

        public WorkflowImp(CarcinogenFactor carcinogenFactor) {
            this.carcinogenFactor = carcinogenFactor;
            this.workflowExecutor = new WorkflowExecutor();
            this.workProcessing = new WorkProcessingImp(carcinogenFactor.carcinogen.parameterTable());

            carcinogenFactor.startWork.forEach(method -> {
                Work work = carcinogenFactor.newWork(method);
                if (work != null) {
                    workflowExecutor.submit(work);
                }
            });
        }

        @Override
        public void fork() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void join() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void fail(Throwable e) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void finish() {
            throw new UnsupportedOperationException();
        }

        @Override
        public <T extends Work> void parallel(Class<Work.WorkSlot<T>> workSlotClass) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void close() throws Exception {
            throw new UnsupportedOperationException();
        }
    }

    private static class WorkProcessingImp extends WorkProcessing {
        WorkProcessingImp(ParameterTable parameterTable) {
            super(parameterTable);
        }
    }
}
