package collaborative.engine.content.yaml;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class FlatParseYamlTest {

    @Nested
    @DisplayName("parse test-yaml-scanner#2.yaml")
    class TestFlatParse extends YamlScannerTest.AbstractYamlScannedDelegator {

        @Override
        public String yaml() {
            return "test-yaml-scanner#2.yaml";
        }

        @Test
        @DisplayName("pares key-value")
        void parse() {
            Map<String, Object> properties = new FlatParseYaml().resolve(yamlScanner);

            assertTrue(properties.containsKey("key"));
            assertTrue(properties.containsKey("valueWithWhiteSpcace"));

            assertEquals(properties.get("key"), "value");
            assertEquals(properties.get("valueWithWhiteSpcace"), "aaa aaaa aaaa");
        }
    }
}
