package collaborative.engine;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

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
        CollaborativeEngine.run(configDirectory);
    }
}
