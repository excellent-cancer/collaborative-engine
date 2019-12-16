package collaborative.engine.parameterize;

public interface ParameterSeries {

    <T> Parameter<T> make(String name);

}
