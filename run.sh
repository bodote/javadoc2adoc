mvn clean package
rm -rf target/adoc
java -cp target/javadoc2adoc-1.0-SNAPSHOT-jar-with-dependencies.jar Main -i src/test/java/de/bag/testfiles -o target/adocs