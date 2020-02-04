package collaborative.engine.core;

import collaborative.engine.core.command.CollaborativeCommandException;
import collaborative.engine.core.command.CollaborativeCommands;
import collaborative.engine.core.identify.Identifier;
import collaborative.engine.core.identify.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 关于Collaborative一些可执行的命令测试
 *
 * @author XyParaCrim
 */
public class CollaborativeCommandsTest {

    // 初始化命令测试

    @Test
    @DisplayName("测试无效初始化命令")
    public void testInvalidInitCommand() {
        assertThrows(CollaborativeCommandException.class, () -> CollaborativeCommands.init()
                .exec());
    }

    @Test
    @DisplayName("测试临时目录初始化命令")
    public void testTempInitCommand() throws IOException {
        File tempDirectory = Files.createTempDirectory(null).toFile();

        // 首先验证是否正确创建临时目录
        assertTrue(tempDirectory.exists());
        assertTrue(tempDirectory.isDirectory());

        assertDoesNotThrow(() -> CollaborativeCommands.
                init().
                setDir(tempDirectory).
                temporary().
                exec().
                close());

        // 当错误初始化collaborative时，希望其目录下所有创建的文件删除
        File[] files = tempDirectory.listFiles();
        assertNotNull(files);
        assertEquals(0, files.length);
        assertTrue(tempDirectory.delete());
    }

    @Test
    @DisplayName("测试临时目录初始化命令 - 发生错误是否可以删除目录")
    public void testTempInitCommandWithException() throws IOException {
        File tempDirectory = Files.createTempDirectory(null).toFile();

        // 首先验证是否正确创建临时目录
        assertTrue(tempDirectory.exists());
        assertTrue(tempDirectory.isDirectory());

        assertThrows(CollaborativeCommandException.class, () -> CollaborativeCommands.
                init().
                setDir(tempDirectory).
                // 设置一个错误的ContentSystem
                        setContentSystem(new ContentSystem() {
                    @Override
                    public Identifier newIdentifier() {
                        throw new UnsupportedOperationException();
                    }
                }).
                        temporary().
                        exec().
                        close());

        // 当错误初始化collaborative时，希望其目录下所有创建的文件删除
        File[] files = tempDirectory.listFiles();
        assertNotNull(files);
        assertEquals(0, files.length);
        assertTrue(tempDirectory.delete());
    }

    // 文件创建命令测试

    @Test
    @DisplayName("测试创建文件命令")
    public void testCreateFileCommand() {
        assertDoesNotThrow(() -> {
            try (Collaboratory collaboratory = CollaborativeCommands.
                    init().
                    setDir(Files.createTempDirectory(null).toFile()).
                    temporary().
                    exec()) {
                ObjectId objectId = collaboratory.
                        commands().
                        createFile().
                        exec();

                assertTrue(objectId.location().exists());
            }
        });
    }
}
