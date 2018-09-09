package uk.dioxic.mgenerate.core;

import org.bson.Document;
import uk.dioxic.mgenerate.core.util.BsonUtil;

import java.nio.file.Path;

public class Template {
    private Document document;
    private String name;

    public Template(Path templateFile) {
        this.document = BsonUtil.parseFile(templateFile);
        this.name = templateFile.getFileName().toString();
    }

    public Template(String json) {
        document = BsonUtil.parse(json);
        name = "output.json";
    }

    public Document getDocument() {
        return document;
    }

    public String getName() {
        return name;
    }
}