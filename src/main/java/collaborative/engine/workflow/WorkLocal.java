package collaborative.engine.workflow;

import collaborative.engine.parameterization.Parameter;
import collaborative.engine.parameterization.ParameterVariables;

import java.util.Objects;

public class WorkLocal {

    private final ParameterVariables parameterStore;

    public WorkLocal(ParameterVariables parameterStore) {
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
}
