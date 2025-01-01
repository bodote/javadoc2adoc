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
        String[] args = {"-i", "src/test/java/de/bag/testfiles", "-o", "target/adocs/de/bag/testfiles"};
        String outputPath = "target/adocs/de/bag/testfiles";
        Path file1Path = Paths.get(outputPath + "/MyTest.adoc");
        Path file2Path = Paths.get(outputPath + "/MyTestTwo.adoc");
        Path verzeichnis = Paths.get(outputPath + "/Verzeichnis.adoc");
        Files.deleteIfExists(file1Path);
        Files.deleteIfExists(file2Path);
        Files.deleteIfExists(verzeichnis);
        Java2AsciiDocConverter java2AsciiDocConverter = new Java2AsciiDocConverter(args);
        java2AsciiDocConverter.convert();
        assertThat(Files.exists(file1Path)).as("File should exist").isTrue();
        assertThat(Files.exists(verzeichnis))
                .as("Verzeichnis.adoc should exist")
                .isTrue();
        String actualContent = Files.readString(file1Path).trim();
        String expectedContent =
                Files.readString(Paths.get("src/test/resources/MyTest.adoc")).trim();
        assertThat(actualContent).isEqualTo(expectedContent);

        actualContent = Files.readString(file2Path).trim();
        expectedContent =
                Files.readString(Paths.get("src/test/resources/MyTestTwo.adoc")).trim();
        assertThat(actualContent).isEqualTo(expectedContent);

        actualContent = Files.readString(verzeichnis).trim();
        expectedContent = Files.readString(Paths.get("src/test/resources/Verzeichnis.adoc"))
                .trim();
        assertThat(actualContent).isEqualTo(expectedContent);
    }
}
