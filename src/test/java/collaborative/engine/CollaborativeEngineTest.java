package collaborative.engine;

import collaborative.engine.parameterize.ParameterTable;
import collaborative.engine.workflow.Work;
import collaborative.engine.workflow.WorkFactory;
import collaborative.engine.workflow.config.Proceed;
import collaborative.engine.workflow.config.ProceedEachAfter;
import collaborative.engine.workflow.parameterization.CollaborativeConfigWork;
import collaborative.engine.workflow.parameterization.ConfigDirectoryWork;
import collaborative.engine.workflow.parameterization.LogConfigWork;
import collaborative.engine.workflow.parameterization.WorkflowConfigWork;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.nio.file.Paths;

import static collaborative.engine.ParameterGroup.CONFIG_DIRECTORY;

public class CollaborativeEngineTest {

    @Test
    @DisplayName("use relative to run it")
    void invalidConfigDirectory() {
        CollaborativeEngine.run("");
    }

    @ParameterizedTest(name = "{0}")
    @CsvSource({
            "/Users/yanjiaxun/Library/Preferences/IntelliJIdea2019.3/scratches/collaborative-engine/config"
    })
    void sampleConfigDirectory(String configDirectory) {
        CollaborativeEngine.run(() -> new WorkflowConfig(configDirectory));
    }

    static class WorkflowConfig implements CollaborativeCarcinogen {

        final String path;

        public WorkflowConfig(String path) {
            this.path = path;
        }

        @Proceed
        public Work configDirectoryWork() {
            return new ConfigDirectoryWork();
        }

        @ProceedEachAfter(ConfigDirectoryWork.class)
        public Work loadConfigWorkSlot() {
            return WorkFactory.parallel(
                    new LogConfigWork(),
                    new WorkflowConfigWork(),
                    new CollaborativeConfigWork()
            );
        }

        @Override
        public ParameterTable parameterTable() {
            ParameterTable parameterTable = new ParameterTable();
            CONFIG_DIRECTORY.set(parameterTable, Paths.get(path));
            return parameterTable;
        }
    }
}
