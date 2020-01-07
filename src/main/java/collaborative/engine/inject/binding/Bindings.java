package collaborative.engine.inject.binding;

import java.util.Objects;

public final class Bindings {

    // private static final Binding<?> NULL_BINDING = BindingStrategy.EMPTY.createBinding(null, null);
    private static final Binding<?> NULL_BINDING = new Binding<>() {
        @Override
        public Key<Object> key() {
            return null;
        }

        @Override
        public Object instance() {
            return null;
        }
    };

    @FunctionalInterface
    interface BindingProvider {
        <T> Binding<T> provide(Tags.PointedTag pointedTag, Tags.SourceTag<T> sourceTag);
    }

    enum BindingStrategy {
        EMPTY(null),
        INSTANCE(InstanceBinding::new),
        CONSTRUCTOR_SINGLETON(ConstructorBinding::singleton),
        CONSTRUCTOR_FACTORY(ConstructorBinding::factory, false),
        IMPLEMENTATION_SINGLETON(ConstructorBinding::singleton),
        IMPLEMENTATION_FACTORY(ConstructorBinding::factory, false);

        private final BindingProvider bindingProvider;
        private final boolean isSingletonMode;

        BindingStrategy(BindingProvider bindingProvider) {
            this(bindingProvider, true);
        }

        BindingStrategy(BindingProvider bindingProvider, boolean isSingletonMode) {
            this.isSingletonMode = isSingletonMode;
            this.bindingProvider = bindingProvider;
        }

        <T> Binding<T> createBinding(Tags.PointedTag pointedTag, Tags.SourceTag<T> sourceTag) {
            return bindingProvider.provide(pointedTag, sourceTag);
        }

        boolean isSingletonMode() {
            return isSingletonMode;
        }
    }

    public static <T> Binding<T> of(Tags.PointedTag pointedTag, Tags.SourceTag<T> sourceTag) {
        Objects.requireNonNull(sourceTag);
        return sourceTag.bindingStrategy().createBinding(pointedTag, sourceTag);
    }

    @SuppressWarnings("unchecked")
    public static <T> Binding<T> nullable() {
        return (Binding<T>) NULL_BINDING;
    }
}
