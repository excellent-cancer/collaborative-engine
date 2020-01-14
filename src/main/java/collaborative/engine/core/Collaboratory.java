package collaborative.engine.core;

import collaborative.engine.core.command.CollaborativeCommands;
import collaborative.engine.core.errors.WorkSiteNotFoundException;
import collaborative.engine.parameterize.FileParameterTable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pact.annotation.ClassTag;
import pact.component.lifecycle.AutoCloseComponent;
import pact.component.lifecycle.Lifecycle;
import pact.component.lifecycle.ResourceLifecycle;
import pact.support.FileSupport;

import java.io.File;
import java.io.IOException;
import java.nio.channels.FileLock;

@SuppressWarnings("unused")
public class Collaboratory extends AutoCloseComponent {

    private static final Logger LOGGER = LogManager.getLogger(Collaboratory.class);

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

    // Class Tags

    final AnalysisReport analysisReport;

    // Features object

    private final CollaborativeStatus status;

    private final CollaborativeCommands commands;

    private final CollaborativeComponents components;

    protected Collaboratory(CollaboratoryBuilder options) {
        this.workSite = options.getWorkSite();
        this.directory = options.getDirectory();
        this.contentSystem = options.getContentSystem();

        // 获取分析报告的各项指标
        // apply core parameters and generate analysis report
        boolean qualifiedWorkSite, qualifiedDirectory = false;
        if ((qualifiedWorkSite = applyWorkSite(options)) &&
                (qualifiedDirectory = applyDirectory(options))) {
            LOGGER.info("Successful analysis of various indicators.");
        }
        this.analysisReport = new AnalysisReport(qualifiedWorkSite, qualifiedDirectory, true);

        // all feature objects
        this.status = new CollaborativeStatus(this);
        this.commands = new CollaborativeCommands(this);
        this.components = new CollaborativeComponents(this);
    }

    // Load and apply core config

    private boolean applyWorkSite(CollaboratoryBuilder options) {
        if (!workSite.isDirectory()) {
            if (options.isRequireExisted()) {
                LOGGER.error(new WorkSiteNotFoundException(workSite));
                return false;
            }

            if (!workSite.mkdirs()) {
                LOGGER.error("Failed to create wor-site directory: {}", workSite);
                return false;
            }
        }

        // check if it's used by other co-processes
        FileLock workSiteLock = FileSupport.getFileLock(new File(workSite, ".lock"));
        if (workSiteLock == null) {
            LOGGER.error("The workSite has been occupied: {}", workSite);
            return false;
        }
        createdComponent(FileLock.class, workSiteLock);

        // get parameters.yaml file
        try {
            createdComponent(FileParameterTable
                    .class, FileParameterTable.create(workSite, "parameters.yaml", !options.isRequireExisted()));
        } catch (IOException e) {
            LOGGER.error(e);
            return false;
        }

        return true;
    }

    private boolean applyDirectory(CollaboratoryBuilder options) {
        if (!directory.isDirectory()) {
            LOGGER.error(new WorkSiteNotFoundException(directory));
            return false;
        }

        createdComponent(FileStore.class, new FileStore(this, directory));

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

    // Tag class

    @ClassTag(Collaboratory.class)
    static final class AnalysisReport {
        final boolean qualifiedWorkSite;
        final boolean qualifiedDirectory;
        final boolean newlyCreated;

        public AnalysisReport(boolean qualifiedWorkSite, boolean qualifiedDirectory, boolean newlyCreated) {
            this.qualifiedWorkSite = qualifiedWorkSite;
            this.qualifiedDirectory = qualifiedDirectory;
            // TODO 通过参数表的版本数更新
            this.newlyCreated = newlyCreated;
        }
    }
}
