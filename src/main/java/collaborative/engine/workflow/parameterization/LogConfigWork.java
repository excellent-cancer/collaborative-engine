package collaborative.engine.workflow.parameterization;

import collaborative.engine.parameterize.Parameter;
import collaborative.engine.workflow.WorkProcessing;
import collaborative.engine.workflow.Workflow;

import java.nio.file.Path;
import java.util.Map;

import static collaborative.engine.ParameterGroup.LOG_CONFIG_FILE;

public class LogConfigWork extends AbstractLoadYamlFileWork {

    public LogConfigWork() {
        super("Resolve-Log.yaml");
    }

    @Override
    protected Parameter<Path> fileParameter() {
        return LOG_CONFIG_FILE;
    }

    @Override
    protected void buildParameterTable(Map<String, Object> properties, WorkProcessing workProcessing, Workflow workflow) {

    }
}
