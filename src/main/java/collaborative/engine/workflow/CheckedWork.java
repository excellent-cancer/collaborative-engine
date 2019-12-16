package collaborative.engine.workflow;

public abstract class CheckedWork implements Work {

    private final boolean isChecked;

    protected CheckedWork(boolean isChecked) {
        this.isChecked = isChecked;
    }

}
