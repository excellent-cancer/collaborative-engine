package collaborative.engine.workflow.normalization;

import collaborative.engine.workflow.Work;
import collaborative.engine.workflow.WorkProcessing;
import collaborative.engine.workflow.Workflow;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pact.support.ExecutorSupport;

import java.util.concurrent.CountDownLatch;

public class KeepAliveWork implements Work.UncheckedWork {

    private static final Logger LOGGER = LogManager.getLogger(KeepAliveWork.class);

    private static Runnable keepAliveTask() {
        final CountDownLatch downLatch = new CountDownLatch(1);
        Runtime.getRuntime().addShutdownHook(new Thread(downLatch::countDown));

        return () -> {
            try {
                downLatch.await();
            } catch (InterruptedException e) {

            }
        };
    }


    @Override
    public Workflow proceed(WorkProcessing workProcessing, Workflow workflow) {
        ExecutorSupport.singleOnlyThreadExecutor().
                submit(keepAliveTask());

        return null;
    }
}
