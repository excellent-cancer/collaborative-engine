package collaborative.engine.parameterization;

public interface ParameterSeries {

    <T> Parameter<T> make(String name);

}
