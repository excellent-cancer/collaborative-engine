package collaborative.engine.workflow.normalization;

import collaborative.engine.service.ServicePlatform;
import collaborative.engine.workflow.Work;
import collaborative.engine.workflow.WorkProcessing;
import collaborative.engine.workflow.Workflow;

public class AssemblyServicesWork extends Work {

    // sprivate static final Logger LOGGER = LogManager.getLogger(AssemblyServicesWork.class);

    // private final List<ServiceBuilder> assemblyServices;

    public AssemblyServicesWork() {
        super("Assembly-Services");
/*        assemblyServices = Stream.of(services).
                filter(Objects::nonNull).
                collect(Collectors.toUnmodifiableList());*/
    }

    @Override
    public void proceed(WorkProcessing workProcessing, Workflow workflow) {
        ServicePlatform platform = workProcessing.servicePlatform();
        // ServiceBinder serviceBinder = ServiceSupport.bindService(workProcessing::con);

/*        try {
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
        }*/
    }
}
