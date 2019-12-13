package collaborative.engine.workflow.phase;

import collaborative.engine.parameter.ParameterVariablesGroup;
import collaborative.engine.parameterization.ParameterVariables;
import collaborative.engine.workflow.Work;
import collaborative.engine.workflow.WorkLocal;
import collaborative.engine.workflow.Workflow;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author XyParaCrim
 */
public class ConfigFileWork implements Work {

    private static final Logger LOGGER = LogManager.getLogger(ConfigFileWork.class);

    private static final String CONFIG_DIRECTORY = "config-directory";

    private final String configDirectory;

    public ConfigFileWork(String configDirectory) {
        this.configDirectory = configDirectory;
    }

    @Override
    public void proceed(WorkLocal workLocal, Workflow workflow) {
        ParameterVariables constants = workLocal.parameterOrDefault(ParameterVariablesGroup.CONSTANTS);

        constants.setProperty(CONFIG_DIRECTORY, configDirectory);

        // validate path
        Path directoryPath = Paths.get(configDirectory);
        if (!Files.isDirectory(directoryPath)) {
            LOGGER.error("the path is not a directory");
            throw new IllegalConfigException(qualify(CONFIG_DIRECTORY, configDirectory));
        }

        // fin

    }

    private static String qualify(String configName, String configValue) {
        return "{" + configName + " = " + configValue + "}";
    }

    static class IllegalConfigException extends RuntimeException {
        public IllegalConfigException(String message) {
            super(message);
        }
    }
}
