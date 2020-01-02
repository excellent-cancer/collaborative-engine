package collaborative.engine.workflow;

import collaborative.engine.parameterize.Parameter;
import collaborative.engine.parameterize.ParameterTable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pact.annotation.NotNull;
import pact.etc.ConfigureCheckProcessing;

import java.util.Objects;

/**
 * Provides access to the parameter set and save workflow variables.
 *
 * @author XyParaCrim
 */
public class WorkProcessing implements ConfigureCheckProcessing {

    private static final Logger LOGGER = LogManager.getLogger();

    public void reportStartWork(@NotNull Work work) {
        LOGGER.debug("[{}]: start work", work.tagName());
    }

    public void reportDoneWork(@NotNull Work work) {
        LOGGER.debug("[{}]: done work", work.tagName());
    }

    public void reportFailedWork(@NotNull Work work) {
        LOGGER.debug("[{}]: failed work", work.tagName());
    }

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
