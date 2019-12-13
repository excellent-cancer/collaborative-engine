package collaborative.engine;

/**
 *
 * @author XyParaCrim
 */
public final class LifecycleFactory {
    public static ServiceLifecycle service() {
        return new ServiceLifecycle();
    }
}
