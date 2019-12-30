package collaborative.engine.workflow;

import collaborative.engine.CarcinogenFactor;
import collaborative.engine.parameterize.ParameterTable;
import collaborative.engine.workflow.run.WorkService;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class Workflow implements WorkService {

    private final CarcinogenFactor carcinogenFactor;
    public final ForkJoinPool forkJoinPool;
    private final WorkProcessing processing;

    public Workflow(CarcinogenFactor carcinogenFactor) {
        this.carcinogenFactor = carcinogenFactor;
        this.forkJoinPool = new ForkJoinPool();
        this.processing = new WorkProcessingImp(carcinogenFactor.carcinogen.parameterTable());
        this.carcinogenFactor.startWork().forEach(this::submit);
    }

    public void submit(Work work) {
        forkJoinPool.submit(new RecursiveWork(work));
    }

    public RecursiveAction newAction(Work work) {
        return new RecursiveWork(work);
    }

    private class RecursiveWork extends RecursiveAction {

        final Work work;

        RecursiveWork(Work work) {
            this.work = work;
        }

        @Override
        protected void compute() {
            work.proceed(processing, Workflow.this);

            carcinogenFactor.defaultWork(work.getClass()).
                    forEach(work1 -> forkJoinPool.submit(new RecursiveWork(work1)));
        }
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

    private static class WorkProcessingImp extends WorkProcessing {
        WorkProcessingImp(ParameterTable parameterTable) {
            super(parameterTable);
        }
    }
}

/*
public class Workflow implements WorkService {

    private final Status status;
    private final Parameter parameter;
    private final CarcinogenFactor carcinogenFactor;

    private WorkQueue[] queues;

    public Workflow(CarcinogenFactor carcinogenFactor) {
        this.parameter = new Parameter();
        this.queues = new WorkQueue[ComputionSupport.expansionUp(parameter.parallelism)];
        this.status = new Status();
        this.carcinogenFactor = carcinogenFactor;
    }

    public void submit(Work work) {
        Objects.requireNonNull(work);
        if (!WorkflowWorkerThread.submitIfInWorkflow(work)) {

        }
    }

    @ClassTag(Workflow.class)
    private static class DefaultClassParameters {

        static final long KEEP_ALIVE = 20L;

        static final int PARALLELISM = Runtime.getRuntime().availableProcessors() - 1;
    }

    @ClassTag(Workflow.class)
    private static class Masks {

        static final int SC_SHIFT = 16;
        static final int TC_SHIFT = 32;
        static final int RC_SHIFT = 48;

        static final long TC_UNIT = 0x0001L << TC_SHIFT;
        static final long RC_UNIT = 0x0001L << RC_SHIFT;

        static final int AC_MASK = 0xffff;
        static final int SC_MASK = 0xffff << SC_SHIFT;
        static final long TC_MASK = 0xffffL << TC_SHIFT;
        static final long RC_MASK = 0xffffL << RC_SHIFT;

        static long total(int i) {
            return ((long) (-i) << TC_SHIFT) & TC_MASK;
        }

        static long released(int i) {
            return ((long) (-i) << RC_SHIFT) & RC_MASK;
        }

        static int avail(int parallel) {
            return (1 - parallel) & AC_MASK;
        }

        static int spare(int parallel) {
            return ((-parallel) << SC_SHIFT) & SC_MASK;
        }

    }

    @ClassTag(Workflow.class)
    private class Status {
        volatile long control;

        Status() {
            this.control = Masks.released(parameter.parallelism) | Masks.total(parameter.parallelism);
        }
    }

    @ClassTag(ForkJoinExecutor.class)
    private static class Parameter {

        final int bounds;
        final int parallelism;
        final long keepAlive;

        Parameter() {
            this.parallelism = DefaultClassParameters.PARALLELISM;
            this.bounds = Masks.avail(parallelism) | Masks.spare(parallelism);
            this.keepAlive = DefaultClassParameters.KEEP_ALIVE;
        }
    }

    private static class WorkQueue {}
}
*/
