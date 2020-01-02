package collaborative.engine.workflow.parameterization;

import collaborative.engine.parameterize.Parameter;
import collaborative.engine.workflow.WorkProcessing;
import collaborative.engine.workflow.Workflow;

import java.nio.file.Path;
import java.util.Map;

import static collaborative.engine.ParameterGroup.WORKFLOW_CONFIG_FILE;

/**
 * @author XyParaCrim
 */
public class WorkflowConfigWork extends AbstractLoadYamlFileWork {

    public WorkflowConfigWork() {
        super("Resolve-Workflow.yaml");
    }

    @Override
    protected Parameter<Path> fileParameter() {
        return WORKFLOW_CONFIG_FILE;
    }

    @Override
    protected void buildParameterTable(Map<String, Object> properties, WorkProcessing workProcessing, Workflow workflow) {

    }
}
