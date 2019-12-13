package collaborative.engine;

import collaborative.engine.parameterization.ParameterSeries;
import collaborative.engine.parameterization.ParameterSupport;

/**
 * Defines the properties of converted by config files.
 * Root parameter store to define important concept.
 * @author XyParaCrim
 * @scope part
 */
public final class ParameterSeriesGroup {

    /**
     * Running config from collaborative-engine.yaml file.
     */
    public static final ParameterSeries COLLABORATIVE_ENGINE_CONFIG =
            ParameterSupport.prefix("collaborative");

    /**
     * Setting jvm option from jvm.option files.
     */
    public static final ParameterSeries JVM_OPTIONS_CONFIG =
            ParameterSupport.prefix("jvm");

    /**
     * Setting how to use log frame.
     */
    public static final ParameterSeries LOG_CONFIG =
            ParameterSupport.prefix("log");

    /**
     * Some global parameters that is not important for this project.
     */
    public static final ParameterSeries BASE_PARAMETER =
            ParameterSupport.prefix("base");

}
