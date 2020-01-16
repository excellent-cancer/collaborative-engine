package collaborative.engine.core;

import collaborative.engine.core.errors.CollaboratoryDisqualifiedException;
import pact.support.FunctionSupport;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("unused")
public class CollaboratoryBuilder {

    // Getters and setters of parameters

    private File directory;

    private File workSite;

    private ContentSystem contentSystem;

    private boolean requirePureLocationIfCreate;

    private boolean requireExisted;

    private final List<FunctionSupport.ConsumerWithException<Collaboratory, Exception>> shutdownHooks = new LinkedList<>();

    public File getDirectory() {
        return directory;
    }

    public File getWorkSite() {
        return workSite;
    }

    public ContentSystem getContentSystem() {
        return contentSystem;
    }

    public CollaboratoryBuilder setDirectory(File directory) {
        this.directory = directory;
        return this;
    }

    public boolean isRequirePureLocationIfCreate() {
        return requirePureLocationIfCreate;
    }

    public boolean isRequireExisted() {
        return requireExisted;
    }

    public CollaboratoryBuilder setWorkSite(File workSite) {
        this.workSite = workSite;
        return this;
    }

    public CollaboratoryBuilder setContentSystem(ContentSystem contentSystem) {
        this.contentSystem = contentSystem;
        return this;
    }

    public CollaboratoryBuilder setRequirePureLocationIfCreate(boolean requirePureLocationIfCreate) {
        this.requirePureLocationIfCreate = requirePureLocationIfCreate;
        return this;
    }

    public CollaboratoryBuilder setRequireExisted(boolean requireExisted) {
        this.requireExisted = requireExisted;
        return this;
    }

    public CollaboratoryBuilder addShutdownCommand(FunctionSupport.ConsumerWithException<Collaboratory, Exception> hook) {
        shutdownHooks.add(hook);
        return this;
    }

    // Check required parameters

    private void requireWorkSiteOrDirectory() {
        if (workSite == null && directory == null) {
            throw new IllegalArgumentException("One of workSite or directory must be set.");
        }
    }

    private void requireQualifiedCollaboratory(Collaboratory collaboratory) {
        if (!collaboratory.
                status().
                qualified()) {
            throw new CollaboratoryDisqualifiedException();
        }
    }

    private void requireMutex(Collaboratory collaboratory) {
        throw new UnsupportedOperationException();
    }

    // Check parameters and fill missing

    private CollaboratoryBuilder setup() {
        requireWorkSiteOrDirectory();
        setupContentSystem();
        setupDirectory();
        setupWorkSite();
        return this;
    }

    private void setupDirectory() {
        if (directory == null) {
            this.directory = workSite.getParentFile();
        }
    }

    private void setupWorkSite() {
        if (workSite == null) {
            workSite = new File(directory, ".workSite");
        }
    }

    private void setupContentSystem() {
        this.contentSystem = null;
        // throw new UnsupportedOperationException();
    }

    // Build method

    public Collaboratory build() {
        Collaboratory collaboratory = new Collaboratory(setup());

        collaboratory.
                status().
                requireQualified().
                requireMutex();

        return collaboratory;
    }
}
