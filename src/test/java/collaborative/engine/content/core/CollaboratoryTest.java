package collaborative.engine.content.core;

import collaborative.engine.core.CollaborativeCommands;
import collaborative.engine.core.Collaboratory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CollaboratoryTest {

    @Test
    @DisplayName("test facade's api")
    public void facade() {
        try (Collaboratory collaboratory = CollaborativeCommands.init().call()) {

            collaboratory.commands().status().call();
            collaboratory.commands().history().call();

        } catch (Exception ignored) {

        }
    }


}
