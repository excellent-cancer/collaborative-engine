package collaborative.engine.parameterize;

public class DotParameterSeries implements ParameterSeries {

    private final String prefix;

    public DotParameterSeries(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public <T> Parameter<T> make(String name) {
        return new DotParameter<>(prefix + "." + name);
    }

    private static class DotParameter<T> implements Parameter<T> {

        private final String keyName;

        public DotParameter(String keyName) {
            this.keyName = keyName;
        }

        @Override
        public String name() {
            return keyName;
        }
    }
}
