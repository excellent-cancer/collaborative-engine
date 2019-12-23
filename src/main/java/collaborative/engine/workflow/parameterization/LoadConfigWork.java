package collaborative.engine.workflow.parameterization;

import collaborative.engine.content.ContentSupport;
import collaborative.engine.workflow.Work;
import collaborative.engine.workflow.WorkProcessing;
import collaborative.engine.workflow.Workflow;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import static collaborative.engine.ParameterGroup.*;

/**
 * @author XyParaCrim
 */
public class LoadConfigWork implements Work.UncheckedWork {

    private static final Logger LOGGER = LogManager.getLogger(LoadConfigWork.class);

    @Override
    public Workflow proceed(WorkProcessing workProcessing, Workflow workflow) {
        Path collaborativeYaml = workProcessing.parameter(COLLABORATIVE_CONFIG_FILE);

        // load config from collaborative.yaml as map
        Map<String, Object> properties;
        try {
            properties = ContentSupport.flatLoadYaml(collaborativeYaml);
        } catch (IOException e) {
            LOGGER.error("failed to load collaborative.yaml from {}", collaborativeYaml);
            return workflow.fail(e);
        }

        // build parameter from properties
        if (!buildParameterFromProperties(properties, workProcessing, workflow)) {
            return workflow.fail();
        }

        return workflow;
    }

    // TO-REMOVE
    private boolean buildParameterFromProperties(Map<String, Object> properties, WorkProcessing workProcessing, Workflow workflow) {

        try {
            // TODO
            String dataDirectoryOption = (String) properties.get("data.path");

            Path dataDirectory = Paths.get(dataDirectoryOption);
            if (!dataDirectory.isAbsolute()) {
                boolean isJoinedConfig = (boolean) properties.getOrDefault("data.joined-config", false);

                dataDirectory = isJoinedConfig ?
                        workProcessing.parameter(CONFIG_DIRECTORY).resolve(dataDirectory) :
                        workProcessing.parameter(USER_DIRECTORY).resolve(dataDirectory);
            }

            if (!Files.exists(dataDirectory)) {
                try {
                    Files.createDirectory(dataDirectory);
                } catch (IOException e) {
                    LOGGER.error("fail to create directory({}).", dataDirectory);
                    workflow.fail(e);
                    return false;
                }
            }

            return true;
        } catch (Exception e) {
            workflow.fail(e);
            return false;
        }
    }
}
