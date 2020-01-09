package collaborative.engine.core;

import collaborative.engine.core.errors.CollaboratoryDisqualifiedException;

import java.io.File;

@SuppressWarnings("unused")
public class CollaboratoryBuilder {

    // Getters and setters of parameters

    private File directory;

    private File workSite;

    private ContentSystem contentSystem;

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

    public CollaboratoryBuilder setWorkSite(File workSite) {
        this.workSite = worksite;
        return this;
    }

    public CollaboratoryBuilder setContentSystem(ContentSystem contentSystem) {
        this.contentSystem = contentSystem;
        return this;
    }

    // Check required parameters

    private void requireWorkSite() {
        if (workSite == null) {
            throw new IllegalArgumentException("WorkSite is not locked");
        }
    }

    private void requireQualifiedCollaboratory(Collaboratory collaboratory) {
        if (!collaboratory.qualified()) {
            throw new CollaboratoryDisqualifiedException();
        }
    }

    private void requireMutex(Collaboratory collaboratory) {
        throw new UnsupportedOperationException();
    }

    // Check parameters and fill missing

    private CollaboratoryBuilder setup() {
        requireWorkSite();
        setupContentSystem();
        setupDirectory();
        setupMetadataDirectory();
        return this;
    }

    private void setupDirectory() {
        if (directory == null) {
            this.directory = workSite.getParentFile();
        }
    }

    private void setupMetadataDirectory() {
        throw new UnsupportedOperationException();
    }

    private void setupContentSystem() {
        throw new UnsupportedOperationException();
    }

    // Build method

    public Collaboratory build() {
        Collaboratory collaboratory = Collaboratory.create(setup());

        requireQualifiedCollaboratory(collaboratory);
        requireMutex(collaboratory);

        return collaboratory;
    }
}
