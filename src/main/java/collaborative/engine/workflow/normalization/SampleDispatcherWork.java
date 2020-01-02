package collaborative.engine.workflow.normalization;

import collaborative.engine.workflow.DispatcherWork;
import collaborative.engine.workflow.WorkProcessing;
import collaborative.engine.workflow.Workflow;
import pact.cmp.lifecycle.LifecycleFactory;
import pact.cmp.lifecycle.ServiceLifecycle;

public class SampleDispatcherWork extends DispatcherWork {

    private ServiceLifecycle lifecycle = LifecycleFactory.service();

    public SampleDispatcherWork() {
        super("Sample-Dispatcher");
    }

    @Override
    public void proceed(WorkProcessing workProcessing, Workflow workflow) {
        for (int i = 0; i < Integer.MAX_VALUE; i++) {

        }
    }

    @Override
    public boolean finished() {
        return true;
    }
}
