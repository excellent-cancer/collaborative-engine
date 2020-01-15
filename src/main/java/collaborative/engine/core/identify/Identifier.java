package collaborative.engine.core.identify;

public interface Identifier {

    ObjectId newId();

    default boolean contains(ObjectId objectId) {
        return false;
    }

    default void add(ObjectId objectId) {

    }
}
