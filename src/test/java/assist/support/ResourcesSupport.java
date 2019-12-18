package assist.support;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.function.Consumer;

public final class ResourcesSupport {

    public static void ifPathExists(Class<?> targetClass, String name, Consumer<Path> consumer) {
        try {
            URL dir = targetClass.getResource("/" + name);
            Objects.requireNonNull(consumer).accept(Paths.get(dir.toURI()));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
