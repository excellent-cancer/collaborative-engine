package collaborative.engine.core.identify;

abstract class AbstractIdentifier<T extends ObjectId> implements Identifier {

    protected final Cache<T> cache;

    protected AbstractIdentifier() {
        this.cache = newCache();
    }

    protected abstract Cache<T> newCache();
}
