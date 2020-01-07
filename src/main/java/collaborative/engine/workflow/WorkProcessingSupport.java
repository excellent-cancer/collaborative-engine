package collaborative.engine.workflow;

import collaborative.engine.CollaborativeCarcinogen;
import collaborative.engine.inject.InjectSupport;
import collaborative.engine.inject.Injector;
import collaborative.engine.parameterize.ParameterTable;

import java.util.Objects;

public final class WorkProcessingSupport {

    public static WorkProcessing processing(CollaborativeCarcinogen carcinogen) {
        return new WorkProcessingImp(carcinogen);
    }

    private static final class WorkProcessingImp extends WorkProcessing {

        private final Injector moduleInjector;

        public WorkProcessingImp(CollaborativeCarcinogen carcinogen) {
            this.moduleInjector = InjectSupport.createModuleInjector(carcinogen::configurateModules);
            ParameterTable parameterTable = Objects.requireNonNull(parameterTable());
            carcinogen.configurateParameterTable(parameterTable);
        }

        @Override
        public Injector injector() {
            return moduleInjector;
        }
    }
}
