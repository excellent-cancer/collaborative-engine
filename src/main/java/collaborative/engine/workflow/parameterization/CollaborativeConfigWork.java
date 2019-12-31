package collaborative.engine.workflow.parameterization;

import collaborative.engine.parameterize.Parameter;
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
public class CollaborativeConfigWork extends AbstractLoadYamlFileWork {

    private static final Logger LOGGER = LogManager.getLogger(CollaborativeConfigWork.class);

    @Override
    protected Parameter<Path> fileParameter() {
        return COLLABORATIVE_CONFIG_FILE;
    }

    @Override
    protected void buildParameterTable(Map<String, Object> properties, WorkProcessing workProcessing, Workflow workflow) {
        try {
            String dataDirectoryOption = (String) properties.get("data.path");

            Path dataDirectory = Paths.get(dataDirectoryOption);
            if (!dataDirectory.isAbsolute()) {
                boolean isJoinedConfig = Boolean.parseBoolean((String) properties.get("data.joined-config"));

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
                }
            }
        } catch (Exception e) {
            workflow.fail(e);
        }
    }
}
