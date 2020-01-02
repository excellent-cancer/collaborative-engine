package collaborative.engine.workflow;

public abstract class DispatcherWork extends Work {
    public DispatcherWork(String name) {
        super(name);
    }

    public abstract boolean finished();
}
