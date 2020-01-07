package collaborative.engine.inject.binding;

import java.util.Objects;

public class Key<T> {

    private final int hashCode;
    private final Class<T> type;
    private final Tags.PointedTag tag;

    Key(Class<T> type, Tags.PointedTag tag) {
        this.type = type;
        this.tag = tag;
        this.hashCode = Objects.hash(type, tag);
        ;
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Key<?> key = (Key<?>) o;
        return type.equals(key.type) &&
                tag.equals(key.tag);
    }
}
