package collaborative.engine.service;

public interface ServiceBinder {

    <T> BindOptions<T> bind(Class<T> type);

    interface BindOptions<T> {
        void as();

        void as(T instance);

        void asSingleton();

        void asSingleton(T instance);
    }
}
