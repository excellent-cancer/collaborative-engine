package collaborative.engine.workflow;

import collaborative.engine.parameterize.Parameter;
import collaborative.engine.parameterize.ParameterTable;
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

    @Override
    public Logger logger() {
        return LOGGER;
    }

    private final ParameterTable parameterTable;

    public WorkProcessing(ParameterTable parameterTable) {
        this.parameterTable = Objects.requireNonNull(parameterTable, "parameterStore is required");
    }

    public <T> T parameter(Parameter<T> parameter) {
        return parameter != null ? parameter.get(parameterTable) : null;
    }

    public <T> T parameterOrDefault(Parameter<T> parameter) {
        T value;
        return parameter != null ?
                (value = parameter.get(parameterTable)) == null ?
                        parameter.defaultValue() :
                        value :
                null;

    }

    public <T> boolean setParameterIfAbsent(Parameter<T> parameter, T value) {
        parameter.set(parameterTable, value);
        return true;
    }

    public <T> void setParameter(Parameter<T> parameter, T value) {
        parameter.set(parameterTable, value);
    }
}
