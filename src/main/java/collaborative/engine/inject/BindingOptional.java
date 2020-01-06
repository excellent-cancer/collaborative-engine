package collaborative.engine.inject;

import pact.annotation.NotNull;

import java.lang.annotation.Annotation;

@SuppressWarnings("unused")
public interface BindingOptional<T> {

    PointedOptional asSingleton();

    PointedOptional asSingleton(@NotNull T instance);

    PointedOptional asSingleton(@NotNull Class<? extends T> implementation);

    PointedOptional asFactory();

    PointedOptional asFactory(@NotNull Class<? extends T> implementation);

    interface PointedOptional {

        void defaulted();

        void named(@NotNull String name);

        void annotated(Annotation annotation);
    }
}
