package collaborative.engine.workflow.parameterization;

import collaborative.engine.content.ContentSupport;
import collaborative.engine.parameterize.Parameter;
import collaborative.engine.workflow.Work;
import collaborative.engine.workflow.WorkProcessing;
import collaborative.engine.workflow.Workflow;
import org.apache.logging.log4j.LogManager;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

abstract class AbstractLoadYamlFileWork implements Work {

    @Override
    public void proceed(WorkProcessing workProcessing, Workflow workflow) {
        Path yamlFile = workProcessing.parameter(fileParameter());
        Map<String, Object> properties = null;
        try {
            properties = ContentSupport.flatLoadYaml(yamlFile);
        } catch (IOException e) {
            LogManager.getLogger(this.getClass()).
                    error("failed to load collaborative.yaml from {}", yamlFile);
            workflow.fail(new RuntimeException());
        }

        buildParameterTable(properties, workProcessing, workflow);
    }

    protected abstract Parameter<Path> fileParameter();

    protected abstract void buildParameterTable(Map<String, Object> properties, WorkProcessing workProcessing, Workflow workflow);
}
