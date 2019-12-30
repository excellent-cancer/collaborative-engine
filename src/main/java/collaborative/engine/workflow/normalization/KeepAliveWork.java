package collaborative.engine.workflow.normalization;

import collaborative.engine.workflow.Work;
import collaborative.engine.workflow.WorkProcessing;
import collaborative.engine.workflow.Workflow;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pact.support.ExecutorSupport;
import pact.support.IgnoredSupport;

import java.util.concurrent.CountDownLatch;

public class KeepAliveWork implements Work {

    private static final Logger LOGGER = LogManager.getLogger(KeepAliveWork.class);

    private static Runnable keepAliveTask() {
        final CountDownLatch downLatch = new CountDownLatch(1);
        Runtime.getRuntime().addShutdownHook(new Thread(downLatch::countDown));

        return IgnoredSupport.collectIgnored(downLatch::await);
    }

    @Override
    public void proceed(WorkProcessing workProcessing, Workflow workflow) {
        ExecutorSupport.execute(keepAliveTask());
    }
}
