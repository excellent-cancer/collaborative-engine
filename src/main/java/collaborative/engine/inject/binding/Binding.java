package collaborative.engine.inject.binding;

public interface Binding<T> {

    Key<T> key();

    T instance();
}
