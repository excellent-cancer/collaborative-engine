package collaborative.engine.core.command;

import collaborative.engine.core.Collaboratory;
import collaborative.engine.core.databse.InsertOption;
import collaborative.engine.core.databse.InsertResult;
import collaborative.engine.core.identify.ObjectId;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pact.support.FileSupport;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import static collaborative.engine.core.databse.PromissoryInsertOption.TEMP;
import static pact.support.CharSequenceSupport.requireNonBlank;


/**
 * 创建文件命令
 *
 * @author XyParaCrim
 */
public class CreateFileCommand extends SpecifiedCommand<ObjectId> {

    private static final Logger LOGGER = LogManager.getLogger(CreateFileCommand.class);

    private String extension;

    private Set<InsertOption> options;

    public CreateFileCommand(Collaboratory collaboratory, String defaultExtension) {
        super(collaboratory);
        setExtension(defaultExtension);
        this.options = new HashSet<>();
        this.options.add(TEMP);
    }

    @Override
    public ObjectId exec() throws CollaborativeCommandException {
        return once(() -> {
            // 执行验证条件
            requireNonBlank(extension);
            status().requireQualified();

            // 创建一个临时文件，并插入文件数据库
            File source = FileSupport.createTempFile(extension);
            InsertResult insertResult = components().fileStore().insert(source, options);

            switch (insertResult.getTag()) {
                case INSERTED:
                    ObjectId id = insertResult.getObjectId();
                    LOGGER.info("Create file: {}", id.location().getName());
                    return id;
                case FAILURE:
                case UNSUPPORTED:
                default:
                    LOGGER.error("Failed to create file: {}", insertResult.getObjectId(), insertResult.getError());
                    throw insertResult.getError();
            }
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
