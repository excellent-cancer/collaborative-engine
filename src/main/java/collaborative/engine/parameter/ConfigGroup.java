package collaborative.engine.parameter;

import collaborative.engine.parameterization.Parameter;
import collaborative.engine.parameterization.ParameterVariables;

/**
 * Defines the properties of converted by config files.
 * @author XyParaCrim
 * @scope part
 */
public enum ConfigGroup implements Parameter<ParameterVariables> {
    /**
     * Running config from collaborative-engine.yaml file.
     */
    COLLABORATIVE_ENGINE_CONFIG,

    /**
     * Setting jvm option from jvm.option files.
     */
    JVM_OPTIONS_CONFIG,

    /**
     * Setting how to use log frame.
     */
    LOG_CONFIG
}
