package de.bag;

import java.util.ArrayList;
import java.util.List;

public class VerzeichnisADoc {
    List<DataClazzADoc> dataClazzADocs = new ArrayList<>();

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("= Verzeichnis\n\n");
        dataClazzADocs.forEach(dataClazzADoc -> sb.append("* xref:")
                .append(dataClazzADoc.title)
                .append(".adoc[")
                .append(dataClazzADoc.title)
                .append("]\n"));
        sb.append("\n");
        return sb.toString();
    }
}
