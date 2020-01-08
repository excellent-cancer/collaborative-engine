package collaborative.engine.content;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

class CachedContentProvider implements ContentProvider {

    private final File file;
    private final ConcurrentLinkedQueue<StringBuffer> linesBuffer;

    private CachedContentProvider(File file) {
        List<String> lines = null;
        try {
            lines = Files.readAllLines(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert lines != null;
        this.linesBuffer = lines.
                stream().
                map(StringBuffer::new).
                collect(Collectors.toCollection(ConcurrentLinkedQueue::new));
        this.file = file;
    }

    @NotNull
    static CachedContentProvider newProvider(@NotNull File file) {
        Objects.requireNonNull(file);
        Files.isWritable(file.toPath());

        return new CachedContentProvider(file);
    }

    @NotNull
    static CachedContentProvider newProvider(@NotNull Path path) {
        Objects.requireNonNull(path);
        Files.isWritable(path);

        return new CachedContentProvider(path.toFile());
    }

    @Override
    public ContentInfo contentInfo() {
        return null;
    }
}
