package collaborative.engine.parameterize;

/**
 * @author XyParaCrim
 */
public interface Parameter<T> {
    @SuppressWarnings("unchecked")
    default T get(ParameterTable variables) {
        return (T) variables.get(name());
    }

    default void set(ParameterTable variables, T value) {
        variables.put(name(), value);
    }

    default T defaultValue() {
        return null;
    }

    default String name() {
        return null;
    }

    default void update(ParameterTable variables, T value) {
        throw new UnsupportedOperationException();
    }
}
