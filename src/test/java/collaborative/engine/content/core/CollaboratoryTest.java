package collaborative.engine.content.core;

import collaborative.engine.core.CollaborativeCommands;
import collaborative.engine.core.Collaboratory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CollaboratoryTest {

    @Test
    @DisplayName("test facade's api")
    public void facade() {
        try (Collaboratory collaboratory = CollaborativeCommands.
                init().
                setDir("/Users/yanjiaxun/Library/Preferences/IntelliJIdea2019.3/scratches/collaborative-engine/data").
                exec()) {

            collaboratory.
                    commands().
                    status().
                    exec();

            collaboratory.
                    commands().
                    history().
                    exec();

        } catch (Exception ignored) {

        }
    }


}
