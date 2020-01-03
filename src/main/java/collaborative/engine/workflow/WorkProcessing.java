package collaborative.engine.workflow;

import collaborative.engine.inject.Injector;
import collaborative.engine.parameterize.ParameterTable;
import collaborative.engine.service.ServicePlatform;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pact.annotation.NotNull;
import pact.etc.ConfigureCheckProcessing;

/**
 * Provides access to the parameter set and save workflow variables.
 *
 * @author XyParaCrim
 */
public abstract class WorkProcessing implements ConfigureCheckProcessing {

    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public Logger logger() {
        return LOGGER;
    }

    // Abstract methods

    public abstract Injector injector();

    // Report methods

    public void reportStartWork(@NotNull Work work) {
        LOGGER.debug("[{}]: start work", work.tagName());
    }

    public void reportDoneWork(@NotNull Work work) {
        LOGGER.debug("[{}]: done work", work.tagName());
    }

    public void reportFailedWork(@NotNull Work work) {
        LOGGER.debug("[{}]: failed work", work.tagName());
    }

    public void reportContinueWork(@NotNull Work work) {
        LOGGER.debug("[{}]: continue work", work.tagName());
    }

    // Important Object

    public ParameterTable parameterTable() {
        return injector().instance(ParameterTable.class);
    }

    public ServicePlatform servicePlatform() {
        return injector().instance(ServicePlatform.class);
    }
}
