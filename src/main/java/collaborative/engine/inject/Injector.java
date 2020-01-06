package collaborative.engine.inject;

import java.lang.annotation.Annotation;

public interface Injector {

    <T> T instance(Class<T> type);

    <T> T instance(Class<T> type, String name);

    <T> T instance(Class<T> type, Annotation annotation);
}
