package collaborative.engine.core;

import collaborative.engine.core.errors.WorkSiteNotFoundException;
import collaborative.engine.parameterize.FileParameterTable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pact.annotation.ClassTag;
import pact.component.lifecycle.Lifecycle;
import pact.component.lifecycle.ResourceLifecycle;
import pact.support.FileSupport;

import java.io.File;
import java.io.IOException;
import java.nio.channels.FileLock;

import static collaborative.engine.ParameterGroup.WORK_SITE_DATA;
import static collaborative.engine.ParameterGroup.WORK_SITE_DATA_DIRECTORY;

@SuppressWarnings("unused")
public abstract class Collaboratory implements AutoCloseable {

    private static final Logger LOGGER = LogManager.getLogger(Collaboratory.class);

    private final ResourceLifecycle lifecycle = Lifecycle.resource();

    // Core config

    private final File workSite;

    private final File directory;

    private final ContentSystem contentSystem;

    // Class Tags

    private final CloseResource closeResource;

    private final AnalysisReport analysisReport;


    protected Collaboratory(CollaboratoryBuilder options) {
        this.workSite = options.getWorkSite();
        this.directory = options.getDirectory();
        this.contentSystem = options.getContentSystem();
        this.closeResource = new CloseResource();

        // apply core parameters and generate analysis report
        this.analysisReport = new AnalysisReport(
                applyWorkSite(),
                applyDirectory()
        );
    }

    // Load and apply core config

    private boolean applyWorkSite() {
        if (!workSite.isDirectory()) {
            LOGGER.error(new WorkSiteNotFoundException(workSite));
            return false;
        }

        // check if it's used by other co-processes
        FileLock workSiteLock = FileSupport.getFileLock(workSite);
        if (workSiteLock == null) {
            LOGGER.error("The workSite has been occupied: {}", workSite);
            return false;
        }
        closeResource.workSiteLock = workSiteLock;

        // get parameters.yaml file
        try (FileParameterTable parameterTable =
                     FileParameterTable.create(workSite, "parameters.yaml")) {
            closeResource.parameterTable = parameterTable;
        } catch (IOException e) {
            LOGGER.error(e);
            return false;
        }

        return true;
    }

    private boolean applyDirectory() {
        if (!directory.isDirectory()) {
            LOGGER.error(new WorkSiteNotFoundException(directory));
            return false;
        }

        String directoryFromParameter = WORK_SITE_DATA.get(closeResource.parameterTable);
        if (directoryFromParameter.isBlank()) {
            WORK_SITE_DATA.update(closeResource.parameterTable, directory.toString());
        } else if (!directory.equals(new File(directoryFromParameter))) {
            LOGGER.error("Inconsistent working directories: {}", directoryFromParameter);
            return false;
        }
        WORK_SITE_DATA_DIRECTORY.set(closeResource.parameterTable, directory.toPath());

        // TODO

        closeResource.fileDatabase = null;

        return true;
    }


    public abstract void create() throws IOException;

    public abstract CollaborativeCommands commands();

    static Collaboratory create(CollaboratoryBuilder options) {
        throw new UnsupportedOperationException();
    }

    // Status query

    public boolean qualified() {
        return analysisReport.qualifiedWorkSite;
    }

    // Tag class

    @ClassTag(Collaboratory.class)
    private static class AnalysisReport {

        final boolean qualifiedWorkSite;
        final boolean qualifiedDirectory;

        AnalysisReport(boolean qualifiedWorkSite, boolean qualifiedDirectory) {
            this.qualifiedWorkSite = qualifiedWorkSite;
            this.qualifiedDirectory = qualifiedDirectory;
        }
    }

    @ClassTag(Collaboratory.class)
    private static class CloseResource {

        private FileLock workSiteLock;

        private FileParameterTable parameterTable;

        private FileDatabase fileDatabase;
    }

    // Close resource

    @Override
    public void close() throws Exception {
        closeResource.workSiteLock.close();
        closeResource.parameterTable.close();
    }

    private static class FileDatabase implements AutoCloseable {

        @Override
        public void close() throws Exception {

        }
    }
}
