package collaborative.engine.inject.binding;

public class InstanceBinding extends Binding {

    private final Object instance;

    public InstanceBinding(Object instance) {
        this.instance = instance;
    }

}
