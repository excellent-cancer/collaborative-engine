package collaborative.engine.core.command;

import collaborative.engine.core.Collaboratory;
import collaborative.engine.core.Defaults;
import collaborative.engine.core.identify.ObjectId;
import org.apache.logging.log4j.LogManager;
import pact.support.FileSupport;

import java.io.File;

import static collaborative.engine.core.databse.PromissoryInsertOption.TEMP;
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

            File source = FileSupport.createTempFile(suffix);
            ObjectId objectId = components().fileStore().insert(source, TEMP);

            LogManager.getLogger().info("Create file: {}", objectId.location().getName());
            return null;
        });
    }

    public CreateFileCommand setSuffix(String suffix) {
        this.suffix = suffix;
        return this;
    }
}
