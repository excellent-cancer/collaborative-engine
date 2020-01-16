package collaborative.engine.core.command;

import collaborative.engine.core.Collaboratory;
import collaborative.engine.core.identify.ObjectId;
import org.apache.logging.log4j.LogManager;
import pact.support.FileSupport;

import java.io.File;

import static collaborative.engine.core.databse.PromissoryInsertOption.TEMP;
import static pact.support.CharSequenceSupport.requireNonBlank;


/**
 * 创建文件命令
 *
 * @author XyParaCrim
 */
public class CreateFileCommand extends SpecifiedCommand<ObjectId> {

    private String extension;

    public CreateFileCommand(Collaboratory collaboratory, String defaultExtension) {
        super(collaboratory);
        setExtension(defaultExtension);
    }

    @Override
    public ObjectId exec() throws CollaborativeCommandException {
        return once(() -> {
            // 执行验证条件
            requireNonBlank(extension);
            status().requireQualified();

            // 创建一个临时文件，并插入文件数据库
            File source = FileSupport.createTempFile(extension);
            ObjectId objectId = components().fileStore().insert(source, TEMP);

            LogManager.getLogger().info("Create file: {}", objectId.location().getName());
            return objectId;
        });
    }

    /**
     * 设置文件后缀
     *
     * @param extension 文件后缀
     * @return 返回自身链式对象
     */
    public CreateFileCommand setExtension(String extension) {
        this.extension = extension;
        return this;
    }
}
