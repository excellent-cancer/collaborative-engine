package collaborative.engine.workflow.run;

import collaborative.engine.workflow.Work;

import java.util.Random;

public class WorkflowWorkerThread extends Thread {

    public static boolean submitIfInWorkflow(Work work) {
        Thread thread = currentThread();
        return new Random().nextBoolean();
    }
}
