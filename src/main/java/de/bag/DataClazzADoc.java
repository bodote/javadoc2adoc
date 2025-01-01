package de.bag;

import lombok.Data;
import org.jspecify.annotations.NullMarked;

import java.util.ArrayList;
import java.util.List;

@NullMarked
@Data
public class DataClazzADoc {
    String title;
    String description;
    List<ClazzParam> clazzParams = new ArrayList<>();

    public record ClazzParam(String name, String description) {
        public String toString() {
            return "* " + name + ": " + description;
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("= ")
                .append(title)
                .append("\n\n")
                .append(description)
                .append("\n\n")
                .append(".Properties of ")
                .append(title)
                .append("\n");
        clazzParams.forEach(clazzParam -> sb.append(clazzParam.toString()).append("\n"));
        return sb.toString();
    }
}
