package collaborative.engine.inject.binding;

import java.lang.invoke.VarHandle;
import java.util.function.Supplier;

@SuppressWarnings({"unused", "FieldCanBeLocal"})
class ConstructorBinding<T> implements Binding<T> {

    private final Key<T> key;
    private final Tags.PointedTag pointedTag;
    private final Tags.SourceTag<T> sourceTag;

    protected volatile Supplier<T> instanceSupplier;

    private ConstructorBinding(Tags.PointedTag pointedTag, Tags.SourceTag<T> sourceTag) {
        this.pointedTag = pointedTag;
        this.sourceTag = sourceTag;
        this.key = Keys.get(sourceTag.sourceType(), pointedTag);
    }

    protected Supplier<T> supplier() {
        if (instanceSupplier == null) {
            instanceSupplier = sourceTag.createSupplier();
            VarHandle.releaseFence();
        }
        return instanceSupplier;
    }

    @Override
    public Key<T> key() {
        return this.key;
    }

    @Override
    public T instance() {
        return supplier().get();
    }

    private static class SingletonConstructorBinding<T> extends ConstructorBinding<T> {

        private volatile T instance;

        SingletonConstructorBinding(Tags.PointedTag pointedTag, Tags.SourceTag<T> sourceTag) {
            super(pointedTag, sourceTag);
        }

        @Override
        public T instance() {
            if (instance == null) {
                instance = supplier().get();
                VarHandle.releaseFence();
            }
            return instance;
        }
    }

    static <T> Binding<T> singleton(Tags.PointedTag pointedTag, Tags.SourceTag<T> sourceTags) {
        return new SingletonConstructorBinding<>(pointedTag, sourceTags);
    }

    static <T> Binding<T> factory(Tags.PointedTag pointedTag, Tags.SourceTag<T> sourceTags) {
        return new ConstructorBinding<>(pointedTag, sourceTags);
    }
}
