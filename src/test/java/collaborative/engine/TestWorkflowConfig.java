package collaborative.engine;

import collaborative.engine.inject.module.ModuleBinder;
import collaborative.engine.parameterize.ParameterTable;
import collaborative.engine.service.ServiceBinder;
import collaborative.engine.service.ServicePlatform;
import collaborative.engine.workflow.Work;
import collaborative.engine.workflow.config.Proceed;
import collaborative.engine.workflow.config.ProceedEachAfter;
import collaborative.engine.workflow.normalization.AssemblyServicesWork;
import collaborative.engine.workflow.normalization.KeepAliveWork;
import collaborative.engine.workflow.parameterization.CollaborativeConfigWork;
import collaborative.engine.workflow.parameterization.ConfigDirectoryWork;
import collaborative.engine.workflow.parameterization.LogConfigWork;
import collaborative.engine.workflow.parameterization.WorkflowConfigWork;
import pact.annotation.NotNull;

import java.nio.file.Paths;

import static collaborative.engine.ParameterGroup.CONFIG_DIRECTORY;

@SuppressWarnings("unused")
class TestWorkflowConfig implements CollaborativeCarcinogen {

    final String path;

    public TestWorkflowConfig(String path) {
        this.path = path;
    }

    // How to run workflow

    @Proceed
    public Work keepAliveWork() {
        return new KeepAliveWork();
    }

    @ProceedEachAfter(KeepAliveWork.class)
    public Work configDirectoryWork() {
        return new ConfigDirectoryWork();
    }

    @ProceedEachAfter(slots = ConfigDirectoryWork.LoadConfigWorkSlot.class)
    public Work logConfigWork() {
        return new LogConfigWork();
    }

    @ProceedEachAfter(slots = ConfigDirectoryWork.LoadConfigWorkSlot.class)
    public Work workConfigWork() {
        return new WorkflowConfigWork();
    }

    @ProceedEachAfter(slots = ConfigDirectoryWork.LoadConfigWorkSlot.class)
    public Work collaborativeConfigWork() {
        return new CollaborativeConfigWork();
    }

    @ProceedEachAfter(ConfigDirectoryWork.class)
    public Work assemblyServicesWork() {
        return new AssemblyServicesWork();
    }

    // Configure others

    @Override
    public void configurateModules(@NotNull ModuleBinder moduleBinder) {
        moduleBinder.bind(ParameterTable.class);
        moduleBinder.bind(ServicePlatform.class);
    }

    @Override
    public void configurateParameterTable(@NotNull ParameterTable parameterTable) {
        CONFIG_DIRECTORY.set(parameterTable, Paths.get(path));
    }

    @Override
    public void configurateServices(@NotNull ServiceBinder serviceBinder) {
        serviceBinder.bind(CollaborativeEngineTest.class).asSingleton();
        serviceBinder.bind(CollaborativeEngineTest.ForkJoinWorker.class).as(new CollaborativeEngineTest.ForkJoinWorker(null));
        serviceBinder.bind(CollaborativeEngineTest.class).asSingleton();
        serviceBinder.bind(CollaborativeEngineTest.ForkJoinWorker.class).as(new CollaborativeEngineTest.ForkJoinWorker(null));
        serviceBinder.bind(CollaborativeEngineTest.class).asSingleton();
        serviceBinder.bind(CollaborativeEngineTest.ForkJoinWorker.class).as(new CollaborativeEngineTest.ForkJoinWorker(null));
        serviceBinder.bind(CollaborativeEngineTest.class).asSingleton();
        serviceBinder.bind(CollaborativeEngineTest.ForkJoinWorker.class).as(new CollaborativeEngineTest.ForkJoinWorker(null));
        serviceBinder.bind(CollaborativeEngineTest.class).asSingleton();
        serviceBinder.bind(CollaborativeEngineTest.ForkJoinWorker.class).as(new CollaborativeEngineTest.ForkJoinWorker(null));
        serviceBinder.bind(CollaborativeEngineTest.class).asSingleton();
        serviceBinder.bind(CollaborativeEngineTest.ForkJoinWorker.class).as(new CollaborativeEngineTest.ForkJoinWorker(null));
    }
}
