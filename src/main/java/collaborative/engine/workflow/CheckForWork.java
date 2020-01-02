package collaborative.engine.workflow;

import pact.etc.CheckFor;

public abstract class CheckForWork extends Work implements CheckFor<WorkProcessing> {

    public CheckForWork() {
    }

    public CheckForWork(String name) {
        super(name);
    }

    @Override
    public void proceed(WorkProcessing workProcessing, Workflow workflow) {
        if (!check(workProcessing)) {
            workflow.fail(new RuntimeException());
        }
    }

    @Override
    public abstract boolean check(WorkProcessing processing);

    public static abstract class SpecificCheckForWork extends CheckForWork {

        public SpecificCheckForWork(String name) {
            super(name);
        }

        @Override
        public void proceed(WorkProcessing workProcessing, Workflow workflow) {
            if (check(workProcessing)) {
                // TODO
                // workflow.parallel(wortSlot());
            } else {
                workflow.fail(new RuntimeException());
            }
        }

        protected abstract <T extends SpecificCheckForWork> Class<? extends WorkSlot<T>> workSlot();
    }

    public static abstract class ParallelCheckWork extends CheckForWork {

        public ParallelCheckWork(String name) {
            super(name);
        }

        @Override
        public void proceed(WorkProcessing workProcessing, Workflow workflow) {
            if (check(workProcessing)) {
                workflow.parallel(workSlot());
            } else {
                workflow.fail(new RuntimeException());
            }
        }

        protected abstract Class<? extends WorkSlot<? extends ParallelCheckWork>> workSlot();
    }
}
