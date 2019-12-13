package collaborative.engine;

import collaborative.engine.parameterization.Parameter;

import java.nio.file.Path;

import static collaborative.engine.ParameterSeriesGroup.COLLABORATIVE_ENGINE_CONFIG;

public final class ParameterGroup {

    public static final Parameter<Path> CONFIG_DIRECTORY = COLLABORATIVE_ENGINE_CONFIG.make("configDirectory");
}
