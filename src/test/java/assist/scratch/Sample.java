package assist.scratch;

import collaborative.engine.CollaborativeEngine;
import collaborative.engine.CollaborativeEngineTest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Sample {
    @Test
    @DisplayName("1 + 1 = 2")
    void displayName() {
        assertEquals(2, 2, "1 + 1 should equal 2");
    }

    @ParameterizedTest(name = "{0} + {1} = {2}")
    @CsvSource({
            "0,    1,   1",
            "1,    2,   3",
            "49,  51, 100",
            "1,  100, 101"
    })
    void add(int first, int second, int expectedResult) {
        assertEquals(expectedResult, first + second,
                () -> first + " + " + second + " should equal " + expectedResult);
    }

    @ParameterizedTest(name = "run engine with {0}")
    @CsvSource({"/Users/yanjiaxun/Library/Preferences/IntelliJIdea2019.3/scratches/collaborative-engine/config"})
    void runWithConfigDirectory(String configDirectory) {
        CollaborativeEngine.run(configDirectory);
    }

    @Test
    @DisplayName("log current state")
    void lookupState() {
        final Logger LOGGER = LogManager.getLogger(CollaborativeEngineTest.class);
        LOGGER.info("this's normal");
    }
}
