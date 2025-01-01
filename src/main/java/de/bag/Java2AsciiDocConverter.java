package de.bag;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.visitor.VoidVisitor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NullMarked;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static com.github.javaparser.ParserConfiguration.LanguageLevel.JAVA_21;

@Slf4j
@NullMarked
public class Java2AsciiDocConverter {
    private final Path inputDir;
    private final Path outputDir;

    public Java2AsciiDocConverter(String[] args) {
        log.info("args: {}", args);
        if (args.length < 2) {
            throw new IllegalArgumentException("Please provide a directory name as argument");
        }
        int inputDirIndex = -1;
        int outputDirIndex = -1;
        for (int i = 0; i < args.length - 1; i++) {
            if ("-i".equals(args[i])) {
                inputDirIndex = i;
            }
            if ("-o".equals(args[i])) {
                outputDirIndex = i;
            }
        }
        if (inputDirIndex == -1 || inputDirIndex + 1 >= args.length) {
            throw new IllegalArgumentException("Please provide a valid input directory argument");
        }

        inputDir = Paths.get(args[inputDirIndex + 1]);
        if (outputDirIndex != -1) {
            outputDir = Paths.get(args[outputDirIndex + 1]);
        } else {
            outputDir = Paths.get(inputDir.toString().replace("src/test/java/", "target" + "/adocs/"));
        }
        if (!Files.isDirectory(inputDir)) {
            throw new IllegalArgumentException("Directory does not exist: " + inputDir);
        }
    }

    public void convert() {
        StaticJavaParser.getParserConfiguration().setLanguageLevel(JAVA_21);
        VerzeichnisADoc verzeichnis = new VerzeichnisADoc();

        try (Stream<Path> pathsStream = Files.walk(inputDir); ) {
            pathsStream
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".java"))
                    .forEach(path -> {
                        try {
                            Path outputPath = Paths.get(
                                    outputDir.toString(),
                                    path.getFileName().toString().replace(".java", ".adoc"));
                            processFile(path, outputPath, verzeichnis);
                        } catch (IOException e) {
                            log.error("Error processing file: " + path, e);
                        }
                    });
        } catch (IOException e) {
            log.error("Error walking directory", e);
            throw new RuntimeException(e);
        }
        writeVerzeichnisFile(outputDir, verzeichnis);
    }

    private static void writeVerzeichnisFile(Path outputDir, VerzeichnisADoc verzeichnis) {
        try {
            Path verzeichnisPath = Paths.get(outputDir.toString(), "Verzeichnis.adoc");
            Files.createDirectories(verzeichnisPath.getParent());
            verzeichnisPath = Files.createFile(verzeichnisPath);
            Files.writeString(verzeichnisPath, verzeichnis.toString());
            log.info("finalPath.toAbsolutePath(): {}", verzeichnisPath.toAbsolutePath());
        } catch (IOException e) {
            log.error("Error writing Verzeichnis file", e);
        }
    }

    static void processFile(Path inputPath, Path ouputPath, VerzeichnisADoc verzeichnis) throws IOException {
        InputJavaFile inputJavaFile = readFile(inputPath);
        DataClazzADoc dataClazzADoc = new DataClazzADoc();
        VoidVisitor<DataClazzADoc> methodNameVisitor = new JavaFileVisitor();
        methodNameVisitor.visit(inputJavaFile.cu, dataClazzADoc);
        verzeichnis.dataClazzADocs.add(dataClazzADoc);

        writeADocFile(ouputPath, dataClazzADoc.toString());
    }

    private static void writeADocFile(Path outputPath, String content) throws IOException {
        log.info("newFile: {}", outputPath);
        Files.createDirectories(outputPath.getParent());
        Path finalPath = Files.createFile(outputPath);
        Files.writeString(finalPath, content);

        log.info("finalPath.toAbsolutePath(): {}", finalPath.toAbsolutePath());
    }

    private static InputJavaFile readFile(Path path) throws IOException {

        if (!path.toFile().exists()) {
            throw new IllegalArgumentException("File does not exist: " + path);
        }
        CompilationUnit cu = StaticJavaParser.parse(Files.newInputStream(path));
        return new InputJavaFile(path, cu);
    }

    private record InputJavaFile(Path path, CompilationUnit cu) {}
}