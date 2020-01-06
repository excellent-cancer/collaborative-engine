package collaborative.engine.inject.binding;

public final class Bindings {

    public static <T> Binding of(Class<T> type, Tags.PointedTag pointedTag, Tags.SourceTag sourceTag) {
        Key<T> key = Key.get(type, pointedTag);
    }
}
