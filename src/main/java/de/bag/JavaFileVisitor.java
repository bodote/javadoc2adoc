package de.bag;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.RecordDeclaration;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.javadoc.Javadoc;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NullMarked;

import java.util.Optional;

@Slf4j
@NullMarked
class JavaFileVisitor extends VoidVisitorAdapter<DataClazzADoc> {
    @Override
    public void visit(RecordDeclaration recordDeclaration, DataClazzADoc dataClazzADoc) {
        super.visit(recordDeclaration, dataClazzADoc);
        log.info("recordDeclaration Name Printed: " + recordDeclaration.getName());
        dataClazzADoc.title = recordDeclaration.getNameAsString();
        Optional<JavadocComment> comment = recordDeclaration.getJavadocComment();
        if (comment.isPresent()) {
            Javadoc javadoc = comment.get().parse();
            log.info("recordDeclaration comment Printed: "
                    + javadoc.getDescription().toText());
            dataClazzADoc.description = javadoc.getDescription().toText();

            javadoc.getBlockTags().forEach(blockTag -> {
                log.info("blockTag: " + blockTag);
                dataClazzADoc.clazzParams.add(new DataClazzADoc.ClazzParam(
                        blockTag.getName().orElse("<missing name>"),
                        blockTag.getContent().toText()));
            });
        }
        NodeList<AnnotationExpr> ann = recordDeclaration.getAnnotations();
        if (ann.isEmpty()) {
            return;
        }
        log.info("recordDeclaration annotation Printed: " + ann);
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration clazz, DataClazzADoc dataClazzADoc) {
        super.visit(clazz, dataClazzADoc);
        log.info("clazz Name Printed: " + clazz.getName());
        dataClazzADoc.title = clazz.getNameAsString();
        Optional<JavadocComment> comment = clazz.getJavadocComment();
        if (comment.isPresent()) {
            Javadoc javadoc = comment.get().parse();
            log.info("clazz comment Printed: " + javadoc.getDescription().toText());
            dataClazzADoc.description = javadoc.getDescription().toText();
        }
        NodeList<AnnotationExpr> ann = clazz.getAnnotations();
        if (ann.isEmpty()) {
            return;
        }
        log.info("clazz annotation Printed: " + ann);
    }

    @Override
    public void visit(ConstructorDeclaration constructorDeclaration, DataClazzADoc dataClazzADoc) {
        super.visit(constructorDeclaration, dataClazzADoc);
        log.info("constructorDeclaration Name Printed: " + constructorDeclaration.getName());
        Optional<JavadocComment> comment = constructorDeclaration.getJavadocComment();
        if (comment.isPresent()) {
            Javadoc javadoc = comment.get().parse();
            javadoc.getBlockTags().forEach(blockTag -> {
                log.info("blockTag: " + blockTag);
                dataClazzADoc.clazzParams.add(new DataClazzADoc.ClazzParam(
                        blockTag.getName().orElse("<missing name>"),
                        blockTag.getContent().toText()));
            });
        }
    }
}
