package collaborative.engine.workflow;

import collaborative.engine.parameterization.Parameter;
import collaborative.engine.parameterization.ParameterVariables;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pact.etc.ConfigureCheckProcessing;

import java.util.Objects;

/**
 *  Provides access to the parameter set and save workflow variables.
 * @author XyParaCrim
 */
public class WorkProcessing implements ConfigureCheckProcessing {

    private static final Logger LOGGER = LogManager.getLogger(WorkProcessing.class);

    private final ParameterVariables parameterStore;

    @Override
    public Logger logger() {
        return LOGGER;
    }

    public WorkProcessing(ParameterVariables parameterStore) {
        this.parameterStore = Objects.requireNonNull(parameterStore, "parameterStore is required");
    }

    public <T> T parameter(Parameter<T> parameter) {
        return parameter != null ? parameter.get(parameterStore) : null;
    }

    public <T> T parameterOrDefault(Parameter<T> parameter) {
        T value;
        return parameter != null ?
                (value = parameter.get(parameterStore)) == null ?
                        parameter.defaultValue() :
                        value :
                null;

    }

    public <T> boolean setParameterIfAbsent(Parameter<T> parameter, T value) {
        return false;
    }

    public <T> void setParameter(Parameter<T> parameter, T value) {

    }
}
