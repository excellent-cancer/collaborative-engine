package collaborative.engine.workflow.parameterize;

import collaborative.engine.parameterization.Parameter;
import collaborative.engine.workflow.Work;
import collaborative.engine.workflow.WorkProcessing;
import collaborative.engine.workflow.Workflow;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static collaborative.engine.ParameterGroup.COLLABORATIVE_CONFIG_FILE;
import static collaborative.engine.ParameterGroup.CONFIG_DIRECTORY;

/**
 * Validate whether the specified config's directory is directory
 * and save path to workLocal.
 *
 * @author XyParaCrim
 */
public class ConfigDirectoryWork implements Work.OnlyCheckWork {

    private final Path directoryPath;

    public ConfigDirectoryWork(String configDirectory) {
        this.directoryPath = Paths.get(configDirectory);
    }

    @Override
    public boolean check(WorkProcessing processing) {
        return checkDirectory(processing)
                && checkParameterPresent(processing)
                && checkCollaborativeYaml(processing);
    }

    @Override
    public Workflow nextWork(WorkProcessing workProcessing, Workflow workflow) {
        return workflow.then(new LoadConfigWork());
    }

    private boolean checkDirectory(WorkProcessing processing) {
        // validate whether the path is a directory
        if (!Files.isDirectory(directoryPath)) {
            processing.reportConfigureNotFound("the config path is not a directory");
            return false;
        }
        processing.reportConfigureFound("config directory");
        return true;
    }

    private boolean checkParameterPresent(WorkProcessing processing) {
        // test whether the parameter has been already set
        if (!processing.setParameterIfAbsent(CONFIG_DIRECTORY, directoryPath)) {
            processing.reportConfigureError("the config path has already been set");
            return false;
        }
        return true;
    }

    private boolean checkCollaborativeYaml(WorkProcessing processing) {
        // validate if collaborative.yaml exists
        Path collaborativeYaml = directoryPath.resolve("collaborative.yaml");
        if (Files.exists(collaborativeYaml)) {
            processing.reportConfigureFound("collaborative.yaml");
            processing.setParameter(COLLABORATIVE_CONFIG_FILE, collaborativeYaml);
            return true;
        }
        processing.reportConfigureNotFound("collaborative.yaml file does not exist");
        return false;
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
