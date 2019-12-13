package collaborative.engine.parameterization;

/**
 * @author XyParaCrim
 */
public interface Parameter<T> {
    default T get(ParameterVariables variables) {
        return null;
    }

    default void set(ParameterVariables variables) {
    }

    default T defaultValue() {
        return null;
    }

    default String name() {
        return null;
    }
}
