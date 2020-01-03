package collaborative.engine;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinWorkerThread;

public class CollaborativeEngineTest {

    public static void main(String[] args) {
        new CollaborativeEngineTest().sampleConfigDirectory("/Users/yanjiaxun/Library/Preferences/IntelliJIdea2019.3/scratches/collaborative-engine/config");
    }

    @Test
    @DisplayName("use relative path to run it")
    void invalidConfigDirectory() {
        CollaborativeEngine.run("");
    }

    @ParameterizedTest(name = "{0}")
    @CsvSource({
            "/Users/yanjiaxun/Library/Preferences/IntelliJIdea2019.3/scratches/collaborative-engine/config"
    })
    void sampleConfigDirectory(String configDirectory) {
        CollaborativeEngine.run(() -> new TestWorkflowConfig(configDirectory));
    }

    public static class ForkJoinWorker extends ForkJoinWorkerThread {
        public ForkJoinWorker(ForkJoinPool pool) {
            super(pool);
            this.setDaemon(false);
        }
    }
}
