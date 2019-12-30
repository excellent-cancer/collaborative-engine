package collaborative.engine;

import collaborative.engine.parameterize.ParameterTable;
import collaborative.engine.workflow.Work;
import collaborative.engine.workflow.WorkFactory;
import collaborative.engine.workflow.config.Proceed;
import collaborative.engine.workflow.config.ProceedEachAfter;
import collaborative.engine.workflow.parameterization.CollaborativeConfigWork;
import collaborative.engine.workflow.parameterization.ConfigDirectoryWork;
import collaborative.engine.workflow.parameterization.LogConfigWork;
import collaborative.engine.workflow.parameterization.WorkflowConfigWork;

import java.nio.file.Paths;

import static collaborative.engine.ParameterGroup.CONFIG_DIRECTORY;

@SuppressWarnings("unused")
public class WorkflowConfig implements CollaborativeCarcinogen {

    @Proceed
    public Work configDirectoryWork() {
        return new ConfigDirectoryWork();
    }

    @ProceedEachAfter(ConfigDirectoryWork.class)
    public Work loadConfigWorkSlot() {
        return WorkFactory.parallel(
                new LogConfigWork(),
                new WorkflowConfigWork(),
                new CollaborativeConfigWork()
        );
    }

    @Override
    public ParameterTable parameterTable() {
        ParameterTable parameterTable = new ParameterTable();
        CONFIG_DIRECTORY.set(parameterTable, Paths.get(""));
        return parameterTable;
    }
}
