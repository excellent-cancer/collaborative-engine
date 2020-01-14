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
        return once(builder::build);
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

    public InitCommand setWorkSite(File metadata) {
        builder.setWorkSite(metadata);
        return this;
    }

    public InitCommand setContentSystem(ContentSystem contentSystem) {
        builder.setContentSystem(contentSystem);
        return this;
    }

    public InitCommand notAllowNewIfNotExist() {
        builder.setRequireExisted(true);
        return this;
    }

    public InitCommand allowRemoveAnotherIfNew() {
        builder.setRequirePureLocationIfCreate(false);
        return this;
    }
}
