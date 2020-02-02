package collaborative.engine.core.identify;

abstract class Cache<T extends ObjectId> {

    abstract void add(T objectId);

    abstract void remove(T objectId);

    abstract boolean contains(T objectId);
}
