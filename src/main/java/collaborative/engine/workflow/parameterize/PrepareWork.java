package collaborative.engine.workflow.parameterize;

import collaborative.engine.workflow.Work;
import collaborative.engine.workflow.WorkLocal;
import collaborative.engine.workflow.Workflow;

import java.nio.file.Path;
import java.nio.file.Paths;

import static collaborative.engine.ParameterGroup.USER_DIRECTORY;

/**
 * Preparation for parameterization.
 * @author XyParaCrim
 */
public class PrepareWork implements Work {

    @Override
    public Workflow proceed(WorkLocal workLocal, Workflow workflow) {
        Path usrDir = Paths.get(System.getProperty("user.dir"));

        workLocal.setParameter(USER_DIRECTORY, usrDir);

        return null;
    }
}
