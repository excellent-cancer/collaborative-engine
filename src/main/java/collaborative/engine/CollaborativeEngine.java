package collaborative.engine;

import collaborative.engine.workflow.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pact.cmp.lifecycle.LifecycleFactory;
import pact.cmp.lifecycle.ServiceLifecycle;

/**
 * Collaborative engine facade
 * @author XyParaCrim
 * @scope export
 */
@SuppressWarnings("unused")
public final class CollaborativeEngine {

    private static final Logger LOGGER = LogManager.getLogger(CollaborativeEngine.class);

    private volatile static Workflow ROOT_WORKFLOW = WorkflowFactory.empty();

    private volatile static ServiceLifecycle LIFECYCLE = LifecycleFactory.service();

    /**
     * Static helper that can be used to run a engine from
     * the specified config files.
     * @param configDirectory config directory string
     */
    public static void run(String configDirectory) {
        if (LIFECYCLE.tryStarted()) {
            LOGGER.trace("collaborative-engine is starting...");
            ROOT_WORKFLOW = WorkflowFactory.bootstrap(configDirectory, new WorkflowConfig());
        } else {
            LOGGER.warn("try to run collaborative-engine, but is's running.");
        }
    }

}
