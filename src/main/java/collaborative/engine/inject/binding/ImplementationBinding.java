package collaborative.engine.inject.binding;

public class ImplementationBinding extends Binding {

    private final Class<?> implementation;

    public ImplementationBinding(Class<?> implementation) {
        this.implementation = implementation;
    }
}