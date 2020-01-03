package collaborative.engine.service;

import java.util.Objects;
import java.util.function.Consumer;

public final class ServiceSupport {

    public static ServiceBinder bindService(Consumer<ServiceBinder> bind) {
        Objects.requireNonNull(bind);
        ServiceBinder serviceBinder = new ServiceBinderImp();
        bind.accept(serviceBinder);
        return serviceBinder;
    }

    private static class ServiceBinderImp implements ServiceBinder {


        @Override
        public <T> BindOptions<T> bind(Class<T> type) {
            return null;
        }
    }

}
