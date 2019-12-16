package collaborative.engine;

import collaborative.engine.parameterization.Parameter;

import java.nio.file.Path;

import static collaborative.engine.ParameterSeriesGroup.BASE_PARAMETER;
import static collaborative.engine.ParameterSeriesGroup.COLLABORATIVE_ENGINE_CONFIG;

/**
 * @author XyParaCrim
 */
public final class ParameterGroup {

    public static final Parameter<Path> CONFIG_DIRECTORY = COLLABORATIVE_ENGINE_CONFIG.make("configDirectory");

    public static final Parameter<Path> DTAT_DIRECTORY = COLLABORATIVE_ENGINE_CONFIG.make("dataDirectory");

    public static final Parameter<Path> COLLABORATIVE_CONFIG_FILE = COLLABORATIVE_ENGINE_CONFIG.make("configFile");

    public static final Parameter<Path> USER_DIRECTORY = BASE_PARAMETER.make("usrDirectory");
}
