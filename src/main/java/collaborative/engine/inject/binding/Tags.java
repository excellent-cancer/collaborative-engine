package collaborative.engine.inject.binding;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;
import java.util.function.Supplier;

import static collaborative.engine.inject.binding.Bindings.BindingStrategy;

@SuppressWarnings({"unused", "FieldCanBeLocal"})
public final class Tags {

    // Sign injection point

    public interface PointedTag {
    }

    private static class DefaultPointedTag implements PointedTag {
    }

    private static class NamedPointedTag implements PointedTag {
        private final String name;

        public NamedPointedTag(String name) {
            this.name = name;
        }
    }

    private static class AnnotatedPointedTag implements PointedTag {
        private final Annotation annotation;

        public AnnotatedPointedTag(Annotation annotation) {
            this.annotation = annotation;
        }
    }

    private static final PointedTag DEFAULT_POINTED_TAG = new DefaultPointedTag();

    public static PointedTag defaultTag() {
        return DEFAULT_POINTED_TAG;
    }

    public static PointedTag namedTag(String name) {
        return new NamedPointedTag(Objects.requireNonNull(name));
    }

    public static PointedTag annotatedTag(Annotation annotation) {
        return new AnnotatedPointedTag(Objects.requireNonNull(annotation));
    }

    // Sign injection type

    public interface SourceTag<T> {

        Class<T> sourceType();

        BindingStrategy bindingStrategy();

        Supplier<T> createSupplier();

        default boolean isSingleton() {
            return bindingStrategy().isSingletonMode();
        }
    }

    private static class InstanceTag<T> implements SourceTag<T> {

        private final T instance;
        private final Class<T> type;
        private final BindingStrategy bindingStrategy = BindingStrategy.INSTANCE;

        public InstanceTag(Class<T> type, T instance) {
            this.type = type;
            this.instance = instance;
        }

        @Override
        public Class<T> sourceType() {
            return type;
        }

        @Override
        public BindingStrategy bindingStrategy() {
            return bindingStrategy;
        }

        @Override
        public Supplier<T> createSupplier() {
            return () -> instance;
        }
    }

    @SuppressWarnings("FieldCanBeLocal")
    private static class ConstructorTag<T> implements SourceTag<T> {

        private final Class<T> type;
        private final BindingStrategy bindingStrategy;

        private ConstructorTag(Class<T> type, BindingStrategy bindingStrategy) {
            this.type = type;
            this.bindingStrategy = bindingStrategy;
        }

        @Override
        public Class<T> sourceType() {
            return type;
        }

        @Override
        public BindingStrategy bindingStrategy() {
            return bindingStrategy;
        }

        @Override
        public Supplier<T> createSupplier() {
            // TODO
            return () -> {
                try {
                    return type.getConstructor().newInstance();
                } catch (InstantiationException | InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            };
        }
    }

    @SuppressWarnings("FieldCanBeLocal")
    private static class ImplementationTag<T> implements SourceTag<T> {
        private final Class<T> type;
        private final Class<? extends T> implementation;
        private final BindingStrategy bindingStrategy;

        public ImplementationTag(Class<T> type, Class<? extends T> implementation, BindingStrategy bindingStrategy) {
            this.type = type;
            this.implementation = implementation;
            this.bindingStrategy = bindingStrategy;
        }

        @Override
        public Class<T> sourceType() {
            return type;
        }

        @Override
        public BindingStrategy bindingStrategy() {
            return bindingStrategy;
        }

        @Override
        public Supplier<T> createSupplier() {
            // TODO
            return () -> {
                try {
                    return implementation.getConstructor().newInstance();
                } catch (InstantiationException | InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            };
        }
    }

    public static <T> SourceTag<T> constructorTag(Class<T> type, boolean isSingleton) {
        return new ConstructorTag<>(type,
                isSingleton ? BindingStrategy.CONSTRUCTOR_SINGLETON : BindingStrategy.CONSTRUCTOR_FACTORY);
    }

    public static <T> SourceTag<T> implementationTag(Class<T> type, Class<? extends T> implementation, boolean isSingleton) {
        return new ImplementationTag<>(type, implementation,
                isSingleton ? BindingStrategy.IMPLEMENTATION_SINGLETON : BindingStrategy.IMPLEMENTATION_FACTORY);
    }

    public static <T> SourceTag<T> instanceTag(Class<T> type, T instance) {
        return new InstanceTag<>(type, instance);
    }
}
