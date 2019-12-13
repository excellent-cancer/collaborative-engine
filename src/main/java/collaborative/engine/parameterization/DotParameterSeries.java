package collaborative.engine.parameterization;

public class DotParameterSeries implements ParameterSeries {

    private final String prefix;

    public DotParameterSeries(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public <T> Parameter<T> make(String name) {
        return new DotParameter<>();
    }

    private static class DotParameter<T> implements Parameter<T> {

    }
}
