package de.bag;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class Java2AsciiDocConverterTest {
    @Test
    void shouldConvertJavaDoc2asciiDocFile() throws IOException {
        String[] args = {"src/test/java/de/bag/testfiles"};
        String outputPath = "target/adocs/de/bag/testfiles";
        Path path = Paths.get(outputPath + "/MyTest.adoc");
        Path verzeichnis = Paths.get(outputPath + "/Verzeichnis.adoc");
        Files.deleteIfExists(path);
        Files.deleteIfExists(verzeichnis);
        Java2AsciiDocConverter.convert(args);
        assertThat(Files.exists(path)).as("File should exist").isTrue();
        assertThat(Files.exists(verzeichnis))
                .as("Verzeichnis.adoc should exist")
                .isTrue();
        String actualContent = Files.readString(path);
        String expectedContent = Files.readString(Paths.get("src/test/resources/MyTest.adoc"));
        assertThat(actualContent).isEqualTo(expectedContent);
    }
}
