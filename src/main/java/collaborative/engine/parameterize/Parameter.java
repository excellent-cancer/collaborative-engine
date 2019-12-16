package collaborative.engine.parameterize;

/**
 * @author XyParaCrim
 */
public interface Parameter<T> {
    @SuppressWarnings("unchecked")
    default T get(ParameterVariables variables) {
        return (T) variables.get(name());
    }

    default void set(ParameterVariables variables, T value) {
        variables.put(name(), value);
    }

    default T defaultValue() {
        return null;
    }

    default String name() {
        return null;
    }
}
