package collaborative.engine.workflow.parameterize;

import collaborative.engine.workflow.Work;
import collaborative.engine.workflow.WorkLocal;
import collaborative.engine.workflow.Workflow;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Files;
import java.nio.file.Path;

import static collaborative.engine.ParameterGroup.CONFIG_DIRECTORY;

/**
 * @author XyParaCrim
 */
class LoadConfigWork implements Work {

    private static final Logger LOGGER = LogManager.getLogger(LoadConfigWork.class);

    @Override
    public Workflow proceed(WorkLocal workLocal, Workflow workflow) {
        Path directoryPath = workLocal.parameter(CONFIG_DIRECTORY);

        // load config from collaborative.yaml
        Path collaborativeYaml = directoryPath.resolve("collaborative.yaml");
        if (!Files.exists(collaborativeYaml)) {
            LOGGER.error("collaborative.yaml file does not exist");
            return workflow.fail();
        }

        return workflow;
    }
}
