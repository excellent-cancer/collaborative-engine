package collaborative.engine.parameterization;

public final class ParameterSupport {

    public static ParameterSeries prefix(String prefix) {
        return new DotParameterSeries(prefix);
    }

}
