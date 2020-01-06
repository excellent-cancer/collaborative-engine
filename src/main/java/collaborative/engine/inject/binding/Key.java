package collaborative.engine.inject.binding;

public class Key<T> {

    private final Class<T> type;
    private final Tags.PointedTag tag;
    private final int hashCode;

    public Key(Class<T> type) {
        this(type, Tags.defaultTag());
    }

    private Key(Class<T> type, Tags.PointedTag tag) {
        this.type = type;
        this.tag = tag;
        this.hashCode = type.hashCode() * 31 + tag.hashCode();
    }

    public static <T> Key<T> get(Class<T> type, Tags.PointedTag pointedTag) {
        return new Key<>(type, pointedTag);
    }

    @Override
    public int hashCode() {
        return hashCode;
    }
}
