package collaborative.engine.core.command;

import collaborative.engine.core.Collaboratory;
import collaborative.engine.core.databse.FileDatabase;
import collaborative.engine.core.databse.RemoveOption;
import collaborative.engine.core.databse.RemoveResult;
import collaborative.engine.core.identify.ObjectId;

import java.util.HashSet;
import java.util.Set;

import static collaborative.engine.core.databse.StandardRemoveOption.FORCED;
import static collaborative.engine.core.databse.StandardRemoveOption.IMMEDIATE;

/**
 * 删除文件命令
 *
 * @author XyParaCrim
 */
public class RemoveFileCommand extends SpecifiedCommand<Boolean> {

    public RemoveFileCommand(Collaboratory collaboratory) {
        super(collaboratory);
    }

    @Override
    public Boolean exec() throws CollaborativeCommandException {
        FileDatabase database = components().fileStore();

        assert objectIdString == null || objectId == null;

        if (objectIdString != null) {
            objectId = database.identifier().toObjectId(objectIdString);
        }
        if (objectId != null) {
            Set<RemoveOption> options = new HashSet<>();
            if (immediate) {
                options.add(IMMEDIATE);
            }
            if (forced) {
                options.add(FORCED);
            }

            RemoveResult result = database.remove(objectId, options);
            switch (result.getTag()) {
                case UNSUPPORTED:
                default:
                    throw new CollaborativeCommandException(result.getError());
            }

        } else {
            throw new CollaborativeCommandException("Failed to execute remove file command due to miss object-id");
        }
    }

    private String objectIdString;

    private ObjectId objectId;

    private boolean immediate;

    private boolean forced;

    // 删除文件对外提供的设置

    /**
     * 标识需要删除的文件id，也可以通过{@link RemoveFileCommand#objectId(ObjectId)}设置，
     * 但是以最后一次的输入为准
     *
     * @param objectId id的字符串形式
     * @return 返回自身链式对象
     */
    public RemoveFileCommand objectId(String objectId) {
        this.objectIdString = objectId;
        this.objectId = null;
        return this;
    }

    /**
     * 标识需要删除的文件id，也可以通过{@link RemoveFileCommand#objectId(String)}设置，
     * 但是以最后一次的输入为准
     *
     * @param objectId 直接传入id对象
     * @return 返回自身链式对象
     */
    public RemoveFileCommand objectId(ObjectId objectId) {
        this.objectId = objectId;
        this.objectIdString = null;
        return this;
    }

    /**
     * 有可能的话是否立即删除文件
     *
     * @return 返回自身链式对象
     */
    public RemoveFileCommand immediate() {
        this.immediate = true;
        return this;
    }

    /**
     * 是否一定要删除文件
     *
     * @return 返回自身链式对象
     */
    public RemoveFileCommand forced() {
        this.forced = true;
        return this;
    }
}
