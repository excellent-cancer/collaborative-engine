package collaborative.engine.core.command;

import collaborative.engine.core.Collaboratory;
import collaborative.engine.core.CollaboratoryBuilder;
import collaborative.engine.core.ContentSystem;

import java.io.File;

@SuppressWarnings("unused")
public class InitCommand extends Command<Collaboratory> {

    private final CollaboratoryBuilder builder = new CollaboratoryBuilder();

    @Override
    public Collaboratory exec() throws CollaborativeCommandException {
        try {
            validate();
            return builder.build();
        } catch (Exception e) {
            throw new CollaborativeCommandException(e.getMessage(), e);
        } finally {
            executed();
        }
    }

    // Optional parameters

    public InitCommand setDir(File directory) {
        builder.setDirectory(directory);
        return this;
    }

    public InitCommand setDir(String directory) {
        builder.setDirectory(new File(directory));
        return this;
    }

    public InitCommand metadata(File metadata) {
        builder.setWorksite(metadata);
        return this;
    }

    public InitCommand contentSystem(ContentSystem contentSystem) {
        builder.setContentSystem(contentSystem);
        return this;
    }
}
