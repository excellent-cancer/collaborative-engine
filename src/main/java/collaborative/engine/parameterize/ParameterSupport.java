package collaborative.engine.parameterize;

public final class ParameterSupport {

    public static ParameterSeries prefix(String prefix) {
        return new DotParameterSeries(prefix);
    }

}
