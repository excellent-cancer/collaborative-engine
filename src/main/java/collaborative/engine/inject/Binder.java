package collaborative.engine.inject;

public interface Binder {

    <V> BindingOptional<V> bind(Class<V> type);
}
