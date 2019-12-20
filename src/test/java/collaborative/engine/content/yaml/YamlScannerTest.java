package collaborative.engine.content.yaml;

import assist.support.ResourcesSupport;
import org.junit.jupiter.api.*;

import java.io.Reader;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;

public class YamlScannerTest {

    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    interface YamlScannedDelegator {

        void yamlScanner(YamlScanner yamlScanner);

        String yaml();

        @Test
        @BeforeAll
        @DisplayName("generate yaml-scanner")
        default void testGenerateYamlScanner() {
            ResourcesSupport.ifPathExists(YamlScannerTest.class, yaml(), path -> {
                Reader reader = assertDoesNotThrow(() -> Files.newBufferedReader(path));
                YamlScanner yamlScanner = new YamlScanner(reader);
                yamlScanner(yamlScanner);
            });
        }
    }

    static abstract class AbstractYamlScannedDelegator implements YamlScannedDelegator {

        YamlScanner yamlScanner;

        @Override
        public void yamlScanner(YamlScanner yamlScanner) {
            this.yamlScanner = yamlScanner;
        }

        protected void testNextTokenKind(YamlTokenKind kind) {
            yamlScanner.scan();
            assertEquals(yamlScanner.currentToken().kind, kind);
        }

        protected void testScanKeySplitValue() {
            testNextTokenKind(YamlTokenKind.NAMED);
            testNextTokenKind(YamlTokenKind.SPLIT);
            testNextTokenKind(YamlTokenKind.LITERAL);
        }

        protected void testComments(int lines) {
            while (lines-- > 0) {
                testNextTokenKind(YamlTokenKind.COMMENT);
            }
        }

        protected void testCurrentContent(String content) {
            assertEquals(content, yamlScanner.currentToken().content);
        }
    }

    // Test cases

    @Test
    @DisplayName("null reader")
    void initializeWithNullReader() {
        assertThrows(NullPointerException.class, () -> new YamlScanner(null));
    }

    @Nested
    @DisplayName("scan test-yaml-scanner#1.yaml")
    class TestYamlScanner$1 extends AbstractYamlScannedDelegator {

        @Override
        public String yaml() {
            return "test-yaml-scanner#1.yaml";
        }

        @Test
        void scanYamlFileNullContent() {
            testNextTokenKind(YamlTokenKind.EOF);
        }
    }

    @Nested
    @DisplayName("scan test-yaml-scanner#2.yaml")
    class TestYamlScanner$2 extends AbstractYamlScannedDelegator {

        @Override
        public String yaml() {
            return "test-yaml-scanner#2.yaml";
        }

        @Test
        @DisplayName("normal lexical")
        void scanYamlFileNormalLexical() {
            // normal key-value
            testScanKeySplitValue();

            // comment
            testComments(3);

            // value with whiteSpace
            testScanKeySplitValue();

            // test content
            testCurrentContent("aaa aaaa aaaa");
        }
    }
}
