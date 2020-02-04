package collaborative.engine.core.lock;

import collaborative.engine.core.Defaults;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pact.support.FileSupport;

import java.io.File;
import java.nio.channels.FileLock;

@SuppressWarnings("unuse")
public class ProgressLock implements AutoCloseable {

    private static final Logger LOGGER = LogManager.getLogger(ProgressLock.class);

    private final FileLock workSiteLock;

    private final File lockFile;

    public ProgressLock(File workSite) {
        this.lockFile = new File(workSite, Defaults.LOCK_FILE);
        this.workSiteLock = FileSupport.acquireFileLockWithCreate(this.lockFile);
    }

    public boolean invalid() {
        return workSiteLock == null;
    }

    @Override
    public void close() throws Exception {
        if (!invalid()) {
            workSiteLock.close();
        }
        FileSupport.deleteFile(lockFile);
    }
}
