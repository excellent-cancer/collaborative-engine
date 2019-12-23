package collaborative.engine.workflow.parameterization;

import collaborative.engine.workflow.Work;
import collaborative.engine.workflow.WorkProcessing;
import collaborative.engine.workflow.Workflow;

import java.nio.file.Path;
import java.nio.file.Paths;

import static collaborative.engine.ParameterGroup.USER_DIRECTORY;

/**
 * Preparation for parameterization.
 * @author XyParaCrim
 */
public class PrepareWork implements Work.UncheckedWork {

    @Override
    public Workflow proceed(WorkProcessing workProcessing, Workflow workflow) {
        Path usrDir = Paths.get(System.getProperty("user.dir"));

        workProcessing.setParameter(USER_DIRECTORY, usrDir);

        return null;
    }

    public static final class Mysteriously implements WorkSlot<PrepareWork> {
        @Override
        public Class<PrepareWork> from() {
            return PrepareWork.class;
        }
    }
}
