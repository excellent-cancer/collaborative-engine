package collaborative.engine.inject.binding;

@SuppressWarnings({"unused", "FieldCanBeLocal"})
class InstanceBinding<T> implements Binding<T> {

    private final Key<T> key;
    private final T instance;
    private final Tags.PointedTag pointedTag;
    private final Tags.SourceTag<T> sourceTag;

    InstanceBinding(Tags.PointedTag pointedTag, Tags.SourceTag<T> sourceTag) {
        this.pointedTag = pointedTag;
        this.sourceTag = sourceTag;
        this.key = Keys.get(sourceTag.sourceType(), pointedTag);
        this.instance = sourceTag.createSupplier().get();
    }

    @Override
    public Key<T> key() {
        return key;
    }

    @Override
    public T instance() {
        return instance;
    }
}
