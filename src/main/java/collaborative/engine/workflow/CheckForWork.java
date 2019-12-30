package collaborative.engine.workflow;

import pact.etc.CheckFor;

public abstract class CheckForWork implements Work, CheckFor<WorkProcessing> {

    @Override
    public void proceed(WorkProcessing workProcessing, Workflow workflow) {
        if (!check(workProcessing)) {
            workflow.fail(new RuntimeException());
        }
    }

    @Override
    public abstract boolean check(WorkProcessing processing);

    public static abstract class SpecificCheckForWork extends CheckForWork {

        @Override
        public void proceed(WorkProcessing workProcessing, Workflow workflow) {
            if (check(workProcessing)) {
                // TODO
                // workflow.parallel(wortSlot());
            } else {
                workflow.fail(new RuntimeException());
            }
        }

        protected abstract <T extends SpecificCheckForWork> Class<? extends WorkSlot<T>> wortSlot();
    }
}
