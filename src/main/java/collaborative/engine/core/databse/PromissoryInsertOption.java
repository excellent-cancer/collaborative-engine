package collaborative.engine.core.databse;

public enum PromissoryInsertOption implements InsertOption {
    /**
     * 完全信任传入的文件，用于命令之类的，已知输入条件可信的情况
     */
    UNSUSPECTING,

    /**
     * 声明传入的为非空临时文件，正确插入后会将其删除
     */
    TEMP
}
