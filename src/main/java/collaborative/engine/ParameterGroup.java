package collaborative.engine;

import collaborative.engine.parameterize.Parameter;

import java.nio.file.Path;

import static collaborative.engine.ParameterSeriesGroup.*;

/**
 * @author XyParaCrim
 */
public final class ParameterGroup {

    public static final Parameter<Path> CONFIG_DIRECTORY = COLLABORATIVE_ENGINE_CONFIG.make("configDirectory");

    public static final Parameter<Path> DTAT_DIRECTORY = COLLABORATIVE_ENGINE_CONFIG.make("dataDirectory");

    public static final Parameter<Path> COLLABORATIVE_CONFIG_FILE = COLLABORATIVE_ENGINE_CONFIG.make("file");

    public static final Parameter<Path> WORKFLOW_CONFIG_FILE = WORKFLOW_CONFIG.make("file");

    public static final Parameter<Path> USER_DIRECTORY = BASE_PARAMETER.make("usrDirectory");

    public static final Parameter<Path> LOG_CONFIG_FILE = LOG_CONFIG.make("file");

    public static final Parameter<String> WORK_SITE_DATA = WORK_SITE_PARAMETER.make("data");

    public static final Parameter<Path> WORK_SITE_DATA_DIRECTORY = WORK_SITE_PARAMETER.make("directory");
}
