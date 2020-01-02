package collaborative.engine.workflow;

import collaborative.engine.parameterize.ParameterTable;

import java.util.Objects;

public final class WorkProcessingSupport {

    public static WorkProcessing processing(ParameterTable parameterTable) {
        return new WorkProcessingImp(Objects.requireNonNull(parameterTable));
    }

    private static final class WorkProcessingImp extends WorkProcessing {
        WorkProcessingImp(ParameterTable parameterTable) {
            super(parameterTable);
        }
    }
}
