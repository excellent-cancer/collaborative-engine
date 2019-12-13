package collaborative.engine.workflow.parameterize;

import collaborative.engine.content.ContentSupport;
import collaborative.engine.workflow.Work;
import collaborative.engine.workflow.WorkLocal;
import collaborative.engine.workflow.Workflow;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import static collaborative.engine.ParameterGroup.CONFIG_DIRECTORY;
import static collaborative.engine.ParameterGroup.USER_DIRECTORY;

/**
 * @author XyParaCrim
 */
class LoadConfigWork implements Work {

    private static final Logger LOGGER = LogManager.getLogger(LoadConfigWork.class);

    @Override
    public Workflow proceed(WorkLocal workLocal, Workflow workflow) {
        Path directoryPath = workLocal.parameter(CONFIG_DIRECTORY);

        // validate if collaborative.yaml exists
        Path collaborativeYaml = directoryPath.resolve("collaborative.yaml");
        if (!Files.exists(collaborativeYaml)) {
            LOGGER.error("collaborative.yaml file does not exist");
            return workflow.fail();
        }

        // load config from collaborative.yaml as map
        Map<String, Object> properties;
        try {
            properties = ContentSupport.loadYaml(collaborativeYaml);
        } catch (IOException e) {
            LOGGER.error("failed to load collaborative.yaml from {}", collaborativeYaml.toAbsolutePath());
            return workflow.fail(e);
        }

        // build parameter from properties
        if (!buildParameterFromProperties(properties, workLocal, workflow)) {
            return workflow.fail();
        }

        return workflow;
    }

    // TO-REMOVE
    private boolean buildParameterFromProperties(Map<String, Object> properties, WorkLocal workLocal, Workflow workflow) {
        // TODO
        String dataDirectoryOption = (String) properties.get("data.path");

        Path dataDirectory = Paths.get(dataDirectoryOption);
        if (!dataDirectory.isAbsolute()) {
            boolean isJoinedConfig = (boolean) properties.getOrDefault("data.joined-config", false);

            dataDirectory = isJoinedConfig ?
                    workLocal.parameter(CONFIG_DIRECTORY).resolve(dataDirectory) :
                    workLocal.parameter(USER_DIRECTORY).resolve(dataDirectory);
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
    }
}
