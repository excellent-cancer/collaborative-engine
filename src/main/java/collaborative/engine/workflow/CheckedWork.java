package collaborative.engine.workflow;

public abstract class CheckedWork extends Work {

    private final boolean isChecked;

    protected CheckedWork(boolean isChecked) {
        this.isChecked = isChecked;
    }

}
