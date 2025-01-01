package de.bag;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.visitor.VoidVisitor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
public class Java2Adoc {
    static void processFile(Path path) throws IOException {
        InputJavaFile inputJavaFile = readFile(path);
        DataClazzADoc dataClazzADoc = new DataClazzADoc();
        VoidVisitor<DataClazzADoc> methodNameVisitor = new ClazzNamePrinter();
        methodNameVisitor.visit(inputJavaFile.cu, dataClazzADoc);
        // strip src/test/java/ from path

        writeADocFile(inputJavaFile.path, dataClazzADoc.toString());
    }

    private static void writeADocFile(Path oldPath, String content) throws IOException {
        String newPath = oldPath.getParent().toString().replace("src/test/java/", "target" + "/adocs/");
        String newFilename = oldPath.getFileName().toString().replace(".java", ".adoc");
        Path newFile = Paths.get(newPath, newFilename);
        log.info("newFile: {}", newFile);
        log.info("newFile: {}", newFile);
        Files.createDirectories(newFile.getParent());
        Path finalPath = Files.createFile(newFile);
        Files.writeString(finalPath, content);

        log.info("finalPath.toAbsolutePath(): {}", finalPath.toAbsolutePath());
    }

    private static InputJavaFile readFile(Path path) throws IOException {

        if (!path.toFile().exists()) {
            throw new IllegalArgumentException("File does not exist: " + path);
        }
        CompilationUnit cu = StaticJavaParser.parse(Files.newInputStream(path));

        InputJavaFile result = new InputJavaFile(path, cu);
        return result;
    }

    private record InputJavaFile(Path path, CompilationUnit cu) {}
}
