package collaborative.engine.inject;

import collaborative.engine.inject.binding.Keys;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class KeysTest {

    @Test
    @DisplayName("identical hashCode")
    public void hashCodeTest() {
        assertEquals(Keys.get(KeysTest.class).hashCode(), Keys.get(KeysTest.class).hashCode());
    }

}
