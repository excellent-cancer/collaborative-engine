package collaborative.engine.content.yaml;

import assist.support.ResourcesSupport;
import collaborative.engine.content.core.Token;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.Reader;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;

import static org.junit.jupiter.api.Assertions.*;

public class YamlScannerTest {

    @Test
    @DisplayName("null reader")
    void initializeWithNullReader() {
        assertThrows(NullPointerException.class, () -> new YamlScanner(null));
    }

    @Test
    @DisplayName("scan test-yaml-scanner#1.yaml - null content")
    void scanYamlFileNullContent() {
        ResourcesSupport.ifPathExists(YamlScannerTest.class, "test-yaml-scanner#1.yaml", path -> {
            Reader reader = assertDoesNotThrow(() -> Files.newBufferedReader(path));
            YamlScanner yamlScanner = new YamlScanner(reader);

            yamlScanner.scan();
            assertEquals(yamlScanner.currentToken().kind, YamlToken.YamlTokenKind.EOF);
        });
    }

    @Test
    @DisplayName("scan test-yaml-scanner#2.yaml - normal lexical")
    void scanYamlFileNormalLexical() {
        ResourcesSupport.ifPathExists(YamlScannerTest.class, "test-yaml-scanner#2.yaml", path -> {
            Reader reader = assertDoesNotThrow(() -> Files.newBufferedReader(path));
            YamlScanner yamlScanner = new YamlScanner(reader);

            BiConsumer<YamlScanner, YamlToken.YamlTokenKind> scanAndAssertKind = (scanner, kind) -> {
                scanner.scan();
                assertEquals(scanner.currentToken().kind, kind);
            };

            // normal key-value
            scanAndAssertKind.accept(yamlScanner, YamlToken.YamlTokenKind.LITERAL);
            scanAndAssertKind.accept(yamlScanner, YamlToken.YamlTokenKind.SPLIT);
            scanAndAssertKind.accept(yamlScanner, YamlToken.YamlTokenKind.LITERAL);

            // comment
            scanAndAssertKind.accept(yamlScanner, YamlToken.YamlTokenKind.COMMENT);
            scanAndAssertKind.accept(yamlScanner, YamlToken.YamlTokenKind.COMMENT);
            scanAndAssertKind.accept(yamlScanner, YamlToken.YamlTokenKind.COMMENT);
        });
    }
}
