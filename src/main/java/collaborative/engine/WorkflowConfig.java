package collaborative.engine;

import collaborative.engine.workflow.Work;
import collaborative.engine.workflow.config.Proceed;
import collaborative.engine.workflow.config.ProceedEachAfter;
import collaborative.engine.workflow.parameterization.ConfigDirectoryWork;
import collaborative.engine.workflow.parameterization.LoadConfigWork;
import collaborative.engine.workflow.parameterization.PrepareWork;

import static collaborative.engine.workflow.parameterization.PrepareWork.Mysteriously;

@SuppressWarnings("unused")
class WorkflowConfig {

    @Proceed
    public Work configDirectoryWork() {
        return new ConfigDirectoryWork();
    }

    @ProceedEachAfter(
            value = {
                    ConfigDirectoryWork.class,
                    PrepareWork.class
            },
            slots = Mysteriously.class
    )
    public Work loadConfigWork() {
        return new LoadConfigWork();
    }
}
