package collaborative.engine.workflow.parameterization;

import collaborative.engine.content.ContentSupport;
import collaborative.engine.parameterize.Parameter;
import collaborative.engine.workflow.Work;
import collaborative.engine.workflow.WorkProcessing;
import collaborative.engine.workflow.Workflow;
import pact.annotation.NotNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

abstract class AbstractLoadYamlFileWork extends Work {

    public AbstractLoadYamlFileWork(String name) {
        super(name);
    }

    @Override
    public void proceed(WorkProcessing workProcessing, Workflow workflow) {
        Path yamlFile = fileParameter().get(workProcessing.parameterTable());
        Map<String, Object> properties = null;

        try {
            properties = ContentSupport.flatLoadYaml(yamlFile);
        } catch (IOException e) {
            workflow.fail(new RuntimeException());
        }

        buildParameterTable(properties, workProcessing, workflow);
    }

    @NotNull
    protected abstract Parameter<Path> fileParameter();

    protected abstract void buildParameterTable(Map<String, Object> properties, WorkProcessing workProcessing, Workflow workflow);
}
