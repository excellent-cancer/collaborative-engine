package collaborative.engine;


import collaborative.engine.workflow.Workflow;

import java.util.Objects;

final class CollaborativeWorkflow {

    static Workflow bootstrap(CollaborativeCarcinogen carcinogen) {
        Objects.requireNonNull(carcinogen);

        return new Workflow(CarcinogenFactor.analysis(carcinogen));
    }
}
