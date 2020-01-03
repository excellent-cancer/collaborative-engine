package collaborative.engine.workflow.parameterization;

import collaborative.engine.parameterize.Parameter;
import collaborative.engine.workflow.CheckForWork;
import collaborative.engine.workflow.WorkProcessing;

import java.nio.file.Files;
import java.nio.file.Path;

import static collaborative.engine.ParameterGroup.*;

/**
 * Validate whether the specified config's directory is directory
 * and save path to workLocal.
 *
 * @author XyParaCrim
 */
public class ConfigDirectoryWork extends CheckForWork.ParallelCheckWork {

    public ConfigDirectoryWork() {
        super("CheckFor-Configuration-Location");
    }

    @Override
    public boolean check(WorkProcessing processing) {
        Path directoryPath = CONFIG_DIRECTORY.get(processing.parameterTable());
        return checkDirectory(processing, directoryPath) &&
                checkConfigFileExists(processing, directoryPath.resolve("log.yaml"), LOG_CONFIG_FILE) &&
                checkConfigFileExists(processing, directoryPath.resolve("workflow.yaml"), WORKFLOW_CONFIG_FILE) &&
                checkConfigFileExists(processing, directoryPath.resolve("collaborative.yaml"), COLLABORATIVE_CONFIG_FILE);
    }

    @Override
    protected Class<? extends WorkSlot<? extends ParallelCheckWork>> workSlot() {
        return LoadConfigWorkSlot.class;
    }

    /**
     * 检查参数表中的{@code collaborative.engine.ParameterGroup#CONFIG_DIRECTORY}的值：
     * 该路径是否为目录
     *
     * @param processing    工作过程
     * @param directoryPath {@code collaborative.engine.ParameterGroup#CONFIG_DIRECTORY}的值
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
     * 该路径下是否存在配置文件
     *
     * @param processing 工作过程
     * @param filePath   {@code collaborative.engine.ParameterGroup.CONFIG_DIRECTORY}的值
     * @param parameter  参数表中的参数
     * @return 该参数是否正确
     */
    private boolean checkConfigFileExists(WorkProcessing processing, Path filePath, Parameter<Path> parameter) {
        if (Files.exists(filePath)) {
            processing.reportConfigureFound(filePath.getFileName().toString());
            parameter.set(processing.parameterTable(), filePath);
            return true;
        }
        processing.reportConfigureNotFound(filePath.getFileName().toString(), filePath);
        return false;
    }

    public static final class LoadConfigWorkSlot implements WorkSlot<ConfigDirectoryWork> {
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
