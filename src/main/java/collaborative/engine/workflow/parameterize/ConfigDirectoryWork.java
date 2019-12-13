package collaborative.engine.workflow.parameterize;

import collaborative.engine.parameterization.Parameter;
import collaborative.engine.workflow.Work;
import collaborative.engine.workflow.WorkLocal;
import collaborative.engine.workflow.Workflow;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static collaborative.engine.ParameterGroup.CONFIG_DIRECTORY;

/**
 * Validate whether the specified config's directory is directory
 * and save path to workLocal.
 * @author XyParaCrim
 */
public class ConfigDirectoryWork implements Work {

    private static final Logger LOGGER = LogManager.getLogger(ConfigDirectoryWork.class);

    private final String configDirectory;

    public ConfigDirectoryWork(String configDirectory) {
        this.configDirectory = configDirectory;
    }

    @Override
    public Workflow proceed(WorkLocal workLocal, Workflow workflow) {
        // validate whether the path is a directory
        Path directoryPath = Paths.get(configDirectory);
        if (!Files.isDirectory(directoryPath)) {
            LOGGER.error("the config path is not a directory");
            return workflow.fail(IllegalConfigException.qualify(CONFIG_DIRECTORY, configDirectory));
        }

        // test whether the parameter has been already set
        if (!workLocal.setParameterIfAbsent(CONFIG_DIRECTORY, directoryPath)) {
            LOGGER.warn("the config path has already been set");
        }

        return workflow.then(new LoadConfigWork());
    }

    private static class IllegalConfigException extends RuntimeException {
        public IllegalConfigException(String message) {
            super(message);
        }

        public static IllegalConfigException qualify(Parameter<?> parameter, String configValue) {
            return new IllegalConfigException("{" + parameter.name() + " = " + configValue + "}");
        }
    }
}
