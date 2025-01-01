# javadoc2adoc

converts some subset of javadoc from java files into asciidoc format

it converts the javadoc from a java files

* before the top level class declaration and the javadoc before the constructor declaration
* the javadoc before a top level record declaration
* the following javadoc tags, when found inside the aforementioned javadoc:
    * @param

everything else is ignored

See examples in src/test/java/de/bag/testfiles

usage:
`java -cp target/javadoc2adoc-1.0-SNAPSHOT-jar-with-dependencies.jar Main -i src/test/java/de/bag/testfiles -o target/adocs`
where `src/test/java/de/bag/testfiles` is the directory with the java files to be converted and `target/adocs` is the
directory where the converted asciidoc - files are stored
