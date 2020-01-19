package collaborative.engine.core.databse;

public enum StandardRemoveOption implements RemoveOption {
    /**
     * 是否需要强制把此文件删除
     */
    FORCED,
    /**
     * 是否需要立即删除，即如果此时资源被占用则在它完成后删除，否则就仅仅为尝试而已
     */
    IMMEDIATE
}
