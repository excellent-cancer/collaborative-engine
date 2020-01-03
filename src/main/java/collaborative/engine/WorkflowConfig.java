package collaborative.engine;

import collaborative.engine.parameterize.ParameterTable;
import collaborative.engine.service.ServiceBinder;
import pact.annotation.NotNull;

@SuppressWarnings("unused")
public class WorkflowConfig implements CollaborativeCarcinogen {

/*    @Proceed
    public Work configDirectoryWork() {
        return new ConfigDirectoryWork();
    }

    @ProceedEachAfter(ConfigDirectoryWork.class)
    public Work loadConfigWorkSlot() {
        return null;
    }

    @Override
    public ParameterTable parameterTable() {
        ParameterTable parameterTable = new ParameterTable();
        CONFIG_DIRECTORY.set(parameterTable, Paths.get(""));
        return parameterTable;
    }*/

    @Override
    public void configurateParameterTable(@NotNull ParameterTable parameterTable) {

    }

    @Override
    public void configurateServices(@NotNull ServiceBinder serviceBinder) {

    }
}
