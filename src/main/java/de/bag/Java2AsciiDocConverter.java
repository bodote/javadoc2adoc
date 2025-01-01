package de.bag;

import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NullMarked;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@NullMarked
public class Java2AsciiDocConverter {

    public static void convert(String[] args) throws IOException {
        log.info("args: {}", args);
        if (args.length >= 1) {
            throw new IllegalArgumentException("Please provide a directory name as argument");
        }
        Path dir = Paths.get(args[0]);
        if (!Files.isDirectory(dir)) {
            throw new IllegalArgumentException("Directory does not exist: " + dir);
        }

        Files.walk(dir)
                .filter(Files::isRegularFile)
                .filter(path -> path.toString().endsWith(".java"))
                .forEach(path -> {
                    try {
                        Java2Adoc.processFile(path);
                    } catch (IOException e) {
                        log.error("Error processing file: " + path, e);
                    }
                });
    }
}