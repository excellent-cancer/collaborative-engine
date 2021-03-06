package collaborative.engine.core;

import collaborative.engine.core.command.CollaborativeCommands;
import collaborative.engine.core.databse.FileDatabase;
import collaborative.engine.core.errors.InitializeCollaboratoryException;
import collaborative.engine.core.errors.WorkSiteNotFoundException;
import collaborative.engine.core.lock.ProgressLock;
import collaborative.engine.parameterize.FileParameterTable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pact.annotation.ClassTag;
import pact.component.AutoCloseComponent;
import pact.component.lifecycle.Lifecycle;
import pact.component.lifecycle.ResourceLifecycle;
import pact.support.ExceptionSupport;
import pact.support.FileSupport;

import java.io.File;
import java.io.IOException;

@SuppressWarnings("unused")
public class Collaboratory extends AutoCloseComponent {

    private static final Logger LOGGER = LogManager.getLogger(Collaboratory.class);

    /**
     * main status control
     */
    private final ResourceLifecycle lifecycle = Lifecycle.resource();

    // Core config

    /**
     * 用于记录上一次工作现场的目录，例如：1.修改历史 2.失败任务队列等等
     */
    private final File workSite;

    /**
     * 文件储存目录
     */
    private final File directory;

    /**
     * 用于提交读写任务， ContentSystem会自动分配线程，用于连接外部程序体系
     */
    @SuppressWarnings("FieldCanBeLocal")
    private final ContentSystem contentSystem;

    // Main export features

    private final CollaborativeStatus status;

    private final CollaborativeCommands commands;

    private final CollaborativeComponents components;

    // Class Tags

    final AnalysisReport analysisReport;

    // Construct methods

    protected Collaboratory(CollaboratoryBuilder options) {
        this.workSite = options.getWorkSite();
        this.directory = options.getDirectory();
        this.contentSystem = options.getContentSystem();

        // 获取分析报告的各项指标
        // apply core parameters and generate analysis report
        boolean success = true;
        try {
            boolean qualifiedWorkSite, qualifiedDirectory = false;
            if ((qualifiedWorkSite = applyWorkSite(options)) &&
                    (qualifiedDirectory = applyDirectory(options))) {
                LOGGER.info("Successful analysis of various indicators.");
            }
            this.analysisReport = new AnalysisReport(qualifiedWorkSite, qualifiedDirectory, true);
        } catch (Exception e) {
            success = false;
            throw new InitializeCollaboratoryException();
        } finally {
            if (!success) {
                ExceptionSupport.closeWithExceptionHanding(this);
            }
        }

        // all feature objects
        this.status = new CollaborativeStatus(this);
        this.commands = new CollaborativeCommands(this);
        this.components = new CollaborativeComponents(this);
    }

    // Load and apply core config

    private boolean applyWorkSite(CollaboratoryBuilder options) {
        boolean isRequireExisted = options.isRequireExisted();
        return applyWorkSiteForDirectory(options) &&
                applyWorkSiteForLock() &&
                applyWorkSiteForParameters(options);
    }

    private boolean applyWorkSiteForDirectory(CollaboratoryBuilder options) {
        if (!workSite.isDirectory()) {
            if (options.isRequireExisted()) {
                LOGGER.error(new WorkSiteNotFoundException(workSite));
                return false;
            }

            if (!workSite.mkdirs()) {
                LOGGER.error("Failed to create work-site directory: {}", workSite);
                return false;
            }

            if (options.isRemoveIfClose()) {
                createdResrouce(() -> FileSupport.deleteFile(workSite));
            }
        }

        LOGGER.debug("Apply work-site location: {}", workSite);
        return true;
    }

    private boolean applyWorkSiteForLock() {
        // check if it's used by other co-processes
        ProgressLock progressLock = new ProgressLock(workSite);
        if (progressLock.invalid()) {
            LOGGER.error("The workSite has been occupied: {}", workSite);
            return false;
        }

        createdComponent(ProgressLock.class, progressLock);
        LOGGER.debug("Apply work-site lock");
        return true;
    }

    private boolean applyWorkSiteForParameters(CollaboratoryBuilder options) {
        // get parameters.yaml file
        FileParameterTable fileParameterTable;
        try {
            fileParameterTable = FileParameterTable.create(workSite, "parameters.yaml", !options.isRequireExisted(), options.isRemoveIfClose());
        } catch (IOException e) {
            LOGGER.error(e);
            return false;
        }

        createdComponent(FileParameterTable.class, fileParameterTable);
        LOGGER.debug("Apply work-site parameters file: {}", fileParameterTable.getLocation());
        return true;
    }

    private boolean applyDirectory(CollaboratoryBuilder options) {
        if (!directory.isDirectory()) {
            LOGGER.error(new WorkSiteNotFoundException(directory));
            return false;
        }

        createdComponent(FileDatabase.class, new FileDatabase(this, directory, contentSystem));

/*        FileParameterTable parameterTable = component(FileParameterTable.class);
        String directoryFromParameter = WORK_SITE_DATA.get(parameterTable);
        if (directoryFromParameter.isBlank()) {
            WORK_SITE_DATA.update(parameterTable, directory.toString());
        } else if (!directory.equals(new File(directoryFromParameter))) {
            LOGGER.error("Inconsistent working directories: {}", directoryFromParameter);
            return false;
        }
        WORK_SITE_DATA_DIRECTORY.set(parameterTable, directory.toPath());*/

        return true;
    }

    // Main export

    public CollaborativeStatus status() {
        return status;
    }

    public CollaborativeCommands commands() {
        return commands;
    }

    public CollaborativeComponents components() {
        return components;
    }

    @Override
    public void close() throws Exception {
        LOGGER.info("Closing a collaboratory... - {}", directory);
        super.close();
        LOGGER.info("Collaboratory closed successfully - {}", directory);
    }

    // Tag class

    @ClassTag(Collaboratory.class)
    static final class AnalysisReport {
        final boolean qualifiedWorkSite;
        final boolean qualifiedDirectory;
        final boolean newlyCreated;

        public AnalysisReport(boolean qualifiedWorkSite, boolean qualifiedDirectory, boolean newlyCreated) {
            this.qualifiedWorkSite = qualifiedWorkSite;
            this.qualifiedDirectory = qualifiedDirectory;
            this.newlyCreated = newlyCreated; // TODO 通过参数表的版本数更新
        }
    }
}
