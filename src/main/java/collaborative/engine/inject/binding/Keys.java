package collaborative.engine.inject.binding;

import java.lang.annotation.Annotation;
import java.util.Objects;

public final class Keys {
    public static <T> Key<T> get(Class<T> type, Tags.PointedTag pointedTag) {
        Objects.requireNonNull(type);
        Objects.requireNonNull(pointedTag);
        return new Key<>(type, pointedTag);
    }

    public static <T> Key<T> get(Class<T> type) {
        Objects.requireNonNull(type);
        return new Key<>(type, Tags.defaultTag());
    }

    public static <T> Key<T> get(Class<T> type, String name) {
        Objects.requireNonNull(type);
        Objects.requireNonNull(name);
        return new Key<>(type, Tags.namedTag(name));
    }

    public static <T> Key<T> get(Class<T> type, Annotation annotation) {
        Objects.requireNonNull(type);
        Objects.requireNonNull(annotation);
        return new Key<>(type, Tags.annotatedTag(annotation));
    }
}
