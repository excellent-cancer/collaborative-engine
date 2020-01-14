package collaborative.engine.content.core;

import collaborative.engine.core.Collaboratory;
import collaborative.engine.core.command.CollaborativeCommands;
import org.apache.logging.log4j.LogManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CollaboratoryTest {

    @Test
    @DisplayName("test facade's api")
    public void facade() {
        try (Collaboratory collaboratory = CollaborativeCommands.
                init().
                setDir("/Users/yanjiaxun/Library/Preferences/IntelliJIdea2019.3/scratches/collaborative-engine/data").
                allowRemoveAnotherIfNew().
                exec()) {

            collaboratory.commands().createFile().exec();

        } catch (Exception ignored) {
            LogManager.getLogger().error(ignored);
            assertNotNull(ignored);
        }
    }


}
