package collaborative.engine.workflow.parameterization;

import collaborative.engine.parameterize.Parameter;
import collaborative.engine.workflow.Work;
import collaborative.engine.workflow.WorkProcessing;
import collaborative.engine.workflow.Workflow;

import java.nio.file.Files;
import java.nio.file.Path;

import static collaborative.engine.ParameterGroup.COLLABORATIVE_CONFIG_FILE;
import static collaborative.engine.ParameterGroup.CONFIG_DIRECTORY;

/**
 * Validate whether the specified config's directory is directory
 * and save path to workLocal.
 *
 * @author XyParaCrim
 */
public class ConfigDirectoryWork implements Work.OnlyCheckWork {

    @Override
    public boolean check(WorkProcessing processing) {
        Path directoryPath = processing.parameter(CONFIG_DIRECTORY);

        return checkDirectory(processing, directoryPath) &&
                checkCollaborativeYaml(processing, directoryPath);
    }

    /**
     * 检查参数表中的{@code collaborative.engine.ParameterGroup.CONFIG_DIRECTORY}的值：
     * 该路径是否为目录
     *
     * @param processing    工作过程
     * @param directoryPath {@code collaborative.engine.ParameterGroup.CONFIG_DIRECTORY}的值
     * @return 该参数是否正确
     */
    private boolean checkDirectory(WorkProcessing processing, Path directoryPath) {
        if (directoryPath == null) {
            processing.reportConfigureNotFound("missing parameters in the parameter-table", CONFIG_DIRECTORY.name());
            return false;
        }

        // validate whether the path is a directory
        if (!Files.isDirectory(directoryPath)) {
            processing.reportConfigureNotFound("the config path is not a directory");
            return false;
        }

        processing.reportConfigureFound("config-directory", directoryPath);
        return true;
    }

    /**
     * 检查参数表中的{@code collaborative.engine.ParameterGroup.CONFIG_DIRECTORY}的值：
     * 该路径下是否存在collaborative.yaml文件
     *
     * @param processing    工作过程
     * @param directoryPath {@code collaborative.engine.ParameterGroup.CONFIG_DIRECTORY}的值
     * @return 该参数是否正确
     */
    private boolean checkCollaborativeYaml(WorkProcessing processing, Path directoryPath) {
        // validate if collaborative.yaml exists
        Path collaborativeYaml = directoryPath.resolve("collaborative.yaml");
        if (Files.exists(collaborativeYaml)) {
            processing.reportConfigureFound("collaborative.yaml");
            processing.setParameter(COLLABORATIVE_CONFIG_FILE, collaborativeYaml);
            return true;
        }
        processing.reportConfigureNotFound("collaborative.yaml", collaborativeYaml);
        return false;
    }

    @Override
    public Workflow nextWork(WorkProcessing workProcessing, Workflow workflow) {
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
