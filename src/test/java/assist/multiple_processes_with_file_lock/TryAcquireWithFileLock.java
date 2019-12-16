package assist.multiple_processes_with_file_lock;

import assist.support.ConsoleSupport;
import assist.support.ControlSupport;
import assist.support.ResourcesSupport;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Path;

import static assist.multiple_processes_with_file_lock.FileLockConfig.EXCLUSIVE_NAME;
import static java.nio.file.StandardOpenOption.WRITE;

/**
 * 测试多进程下的文件锁独占效果
 * @author XyParaCrim
 * @scope test
 */
public final class TryAcquireWithFileLock {

    public static void main(String[] args) throws IOException {
        ResourcesSupport.ifPathExists(TryAcquireWithFileLock.class, EXCLUSIVE_NAME, TryAcquireWithFileLock::tryAcquireFileLock);
        ControlSupport.blockIt();
    }

    private static void tryAcquireFileLock(Path targetPath) {
        try {
            // obtain file lock with fileChannel
            FileChannel channel = FileChannel.open(targetPath, WRITE);

            ConsoleSupport.println("try acquire file lock...");
            FileLock lock = ControlSupport.tryUntil(channel::tryLock);
            ConsoleSupport.rollback("acquire done");
            ConsoleSupport.println("vail lock: " + lock.isValid());
            ConsoleSupport.println("shared lock: " + lock.isShared());
        } catch (IOException e) {
            ConsoleSupport.printExceptionRoutinely(e, "failed to acquire file lock");
        }
    }
}
