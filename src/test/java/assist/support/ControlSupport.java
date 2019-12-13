package assist.support;

import java.io.IOException;
import java.util.Objects;
import java.util.function.Supplier;

public final class ControlSupport {

    @FunctionalInterface
    public interface SupplierWithException<T, V extends Throwable> {
        T get() throws V;
    }

    public static void blockIt() throws IOException {
        System.in.read();
    }

    public static <T, V extends Throwable> T tryUntil(SupplierWithException<T, V> supplier) throws V {
        Objects.requireNonNull(supplier);
        while (true) {
            T t = supplier.get();
            if (t != null) {
                return t;
            }
        }
    }
}
