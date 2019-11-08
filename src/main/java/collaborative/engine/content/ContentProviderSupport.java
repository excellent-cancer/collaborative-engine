package collaborative.engine.content;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Supplier;

public final class ContentProviderSupport {

    public static Supplier<ContentProvider> cachedProvider(File file) {
        return () -> CachedContentProvider.newProvider(file);
    }

    public static Supplier<ContentProvider> cachedProvider(Path path) {
        return () -> CachedContentProvider.newProvider(path);
    }

    public static Supplier<ContentProvider> cachedProvider(String path) {
        return () -> CachedContentProvider.newProvider(Paths.get(path));
    }
}
