package collaborative.engine.workflow;

import collaborative.engine.parameterize.ParameterTable;

import java.util.Objects;
import java.util.function.Consumer;

public final class WorkProcessingSupport {

    public static WorkProcessing processing(ParameterTable parameterTable) {
        return new WorkProcessingImp(Objects.requireNonNull(parameterTable));
    }

    public static WorkProcessing processing(Consumer<ParameterTable> initialize) {
        ParameterTable parameterTable = new ParameterTable();
        Objects.requireNonNull(initialize).accept(parameterTable);
        return new WorkProcessingImp(parameterTable);
    }

    private static final class WorkProcessingImp extends WorkProcessing {
        WorkProcessingImp(ParameterTable parameterTable) {
            super(parameterTable);
        }
    }
}
