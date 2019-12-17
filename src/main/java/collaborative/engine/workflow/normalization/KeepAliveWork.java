package collaborative.engine.workflow.normalization;

import collaborative.engine.workflow.Work;
import collaborative.engine.workflow.WorkProcessing;
import collaborative.engine.workflow.Workflow;

public class KeepAliveWork implements Work.UncheckedWork {

    @Override
    public Workflow proceed(WorkProcessing workProcessing, Workflow workflow) {
        return null;
    }
}
