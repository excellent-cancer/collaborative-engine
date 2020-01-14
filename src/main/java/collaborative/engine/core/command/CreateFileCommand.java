package collaborative.engine.core.command;

import collaborative.engine.core.Collaboratory;
import collaborative.engine.core.Defaults;
import collaborative.engine.core.FileStore;
import org.apache.logging.log4j.LogManager;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import static pact.support.CharSequenceSupport.requireNonBlank;

public class CreateFileCommand extends SpecifiedCommand<Void> {

    private String suffix = Defaults.DEFAULT_SUFFIX;

    public CreateFileCommand(Collaboratory collaboratory) {
        super(collaboratory);
    }

    @Override
    public Void exec() throws CollaborativeCommandException {
        return once(() -> {
            // 执行验证条件
            requireNonBlank(suffix);
            status().requireQualified();
            FileStore fileStore = components().fileStore();
            UUID uuid = UUID.randomUUID();
            String uuidStr = uuid.toString();
            String directory = uuidStr.substring(0, 2);
            String name = uuidStr.substring(2);
            File target = Paths.get(fileStore.location().getPath(), directory, name).toFile();
            File source = File.createTempFile("temp", suffix);
            target.getParentFile().mkdir();
            Files.move(source.toPath(), target.toPath(), StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
            LogManager.getLogger().info(uuidStr);
            return null;
        });
    }

    public CreateFileCommand setSuffix(String suffix) {
        this.suffix = suffix;
        return this;
    }
}
