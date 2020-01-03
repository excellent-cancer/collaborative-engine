package collaborative.engine.service;

public class ServiceBinder {

    public <T> BindOptions<T> bind(Class<T> type) {
        return null;
    }

    public interface BindOptions<T> {
        void as();

        void as(T instance);

        void asSingleton();

        void asSingleton(T instance);
    }

}
