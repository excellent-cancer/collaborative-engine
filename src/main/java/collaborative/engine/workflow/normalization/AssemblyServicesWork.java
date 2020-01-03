package collaborative.engine.workflow.normalization;

import collaborative.engine.service.ServiceBuilder;
import collaborative.engine.service.ServicePlatform;
import collaborative.engine.workflow.Work;
import collaborative.engine.workflow.WorkProcessing;
import collaborative.engine.workflow.Workflow;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.List;

public class AssemblyServicesWork extends Work {

    private static final Logger LOGGER = LogManager.getLogger(AssemblyServicesWork.class);

    private final List<ServiceBuilder> assemblyServices;

    public AssemblyServicesWork(ServiceBuilder... services) {
        super("Assembly-Services");
        assemblyServices = Arrays.asList(services);
    }

    @Override
    public void proceed(WorkProcessing workProcessing, Workflow workflow) {
        ServicePlatform platform = workProcessing.getInstance(ServicePlatform.class);

        try {
            for (ServiceBuilder serviceBuilder : assemblyServices) {
                if (serviceBuilder != null) {
                    platform.install(serviceBuilder);
                } else {
                    reportLostServiceBuilder();
                }
            }
        } catch (Exception e) {
            platform.closeResources();
            workflow.fail(e);
        }
    }

    private static void reportLostServiceBuilder() {

    }
}
