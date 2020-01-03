package collaborative.engine.inject;

public interface Injector {

    <T> T instance(Class<T> type);

}
