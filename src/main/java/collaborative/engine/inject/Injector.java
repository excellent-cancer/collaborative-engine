package collaborative.engine.inject;

import collaborative.engine.inject.binding.Key;

import java.lang.annotation.Annotation;

public interface Injector {

    <T> T instance(Class<T> type);

    <T> T instance(Class<T> type, String name);

    <T> T instance(Class<T> type, Annotation annotation);

    <T> T instance(Key<T> key);
}
