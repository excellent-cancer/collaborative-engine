package collaborative.engine.core.command;

import collaborative.engine.core.Collaboratory;
import collaborative.engine.core.CollaboratoryBuilder;
import collaborative.engine.core.ContentSystem;

import java.io.File;

/**
 * 初始化一个{@link Collaboratory}命令，需要一个文件系统的目录作为必要参数。
 *
 * @author XyParaCrim
 */
@SuppressWarnings("unused")
public class InitCommand extends Command<Collaboratory> {

    private final CollaboratoryBuilder builder = new CollaboratoryBuilder();

    @Override
    public Collaboratory exec() throws CollaborativeCommandException {
        return once(builder::build);
    }

    // Optional parameters

    /**
     * 设置{@link Collaboratory}的目录，也可以为不设置，但是必须指明work-site目录。
     *
     * @param directory 用于{@link Collaboratory}的工作目录
     * @return 返回自身链式对象
     */
    public InitCommand setDir(File directory) {
        builder.setDirectory(directory);
        return this;
    }

    /**
     * @param directory 用于{@link Collaboratory}的工作目录的字符串
     * @return 返回自身链式对象
     * @see InitCommand#setDir(File)
     */
    public InitCommand setDir(String directory) {
        builder.setDirectory(new File(directory));
        return this;
    }

    /**
     * 设置{@link Collaboratory}的工作现场目录，方便下一次运行能够重现现场。
     * 也可以不设置，但必须设置工作目录{@link InitCommand#setDir(File)}。
     *
     * @param workSite 工作现场目录
     * @return 返回自身链式对象
     */
    public InitCommand setWorkSite(File workSite) {
        builder.setWorkSite(workSite);
        return this;
    }

    /**
     * 自定义内容系统，用于{@link Collaboratory}的线程池之类的东西
     *
     * @param contentSystem 自定义内容系统
     * @return 返回自身链式对象
     */
    public InitCommand setContentSystem(ContentSystem contentSystem) {
        builder.setContentSystem(contentSystem);
        return this;
    }

    /**
     * 不允许新建，即必须是一个已经运行过的
     *
     * @return 返回自身链式对象
     */
    public InitCommand notAllowNewIfNotExist() {
        builder.setRequireExisted(true);
        return this;
    }

    /**
     * 代替已存在的
     *
     * @return 返回自身链式对象
     */
    public InitCommand allowRemoveAnotherIfNew() {
        builder.setRequirePureLocationIfCreate(false);
        return this;
    }

    /**
     * 临时{@link Collaboratory}，即当其关闭是会将其删除
     *
     * @return 返回自身链式对象
     */
    public InitCommand temporary() {
        builder.addShutdownCommand(collaboratory -> collaboratory.commands().delete().exec());
        return this;
    }
}
