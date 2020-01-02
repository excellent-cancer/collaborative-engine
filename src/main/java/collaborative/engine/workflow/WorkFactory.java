package collaborative.engine.workflow;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;

public final class WorkFactory {

/*    public static Work parallel(Work... works) {
        return (workProcessing, workflow) -> {
            List<RecursiveAction> actions = new LinkedList<>();
            for (Work work : works) {
                actions.add(workflow.newAction(work));
            }

            ForkJoinTask.invokeAll(actions);
        };
    }*/

}
