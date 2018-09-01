package uk.dioxic.mgenerate.core;

import org.apache.commons.cli.*;
import org.bson.Document;
import uk.dioxic.mgenerate.core.util.BsonUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CliOptions {

    private static final Options opt = new Options();

    static {
        opt.addRequiredOption("t", "template", true, "path to template file or directory");
        opt.addOption("o", "output", true, "path to output file or directory");
        opt.addOption("n", true, "number of documents to generate (per file)");
        opt.addOption("s", "single", false, "output to a single file");
    }

    private Path outputPath;
    private int documents = 10;
    private Path templatePath;
    private String jsonTemplate;
    private boolean singleFileOut;

    public CliOptions(String[] args) throws ParseException {
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(opt, args);

        String templateOptValue = cmd.getOptionValue("t");
        if (templateOptValue.startsWith("{")) {
            jsonTemplate = templateOptValue;
        }
        else {
            templatePath = Paths.get(cmd.getOptionValue("t"));
        }

        if (cmd.hasOption("o")) {
            outputPath = Paths.get(cmd.getOptionValue("o"));
        }

        if (cmd.hasOption("n")) {
            documents = Integer.valueOf(cmd.getOptionValue("n"));
        }

        if (cmd.hasOption("s")) {
            singleFileOut = Boolean.valueOf(cmd.getOptionValue("s"));
        }

        // validation checks

        if (!singleFileOut && outputPath != null) {
            if (Files.exists(outputPath) && !Files.isDirectory(outputPath)) {
                throw new ParseException("multiple file output is not valid - [" + outputPath + "] is a file!");
            }
        }

        if (!Files.exists(templatePath)) {
            throw new ParseException("template [" + templatePath + "] does not exist!");
        }
    }

    public boolean isTemplateFile() {
        return templatePath != null;
    }

    public List<Template> getTemplates() throws IOException {
        if (isTemplateFile()) {
            if (Files.isDirectory(templatePath)) {
                return Files.walk(templatePath)
                        .map(Template::new)
                        .collect(Collectors.toList());
            }
            return Arrays.asList(new Template(templatePath));
        }
        else {
            return Arrays.asList(new Template(jsonTemplate));
        }
    }

    public String getJsonTemplate() {
        return jsonTemplate;
    }

    public Path getOutputPath() {
        return outputPath;
    }

    public Integer getDocuments() {
        return documents;
    }

    public boolean isMultiFileOutput() {
        return outputPath != null && !singleFileOut;
    }

    public boolean isConsoleOutput() {
        return outputPath == null;
    }

    public boolean isSingleFileOutput() {
        return singleFileOut;
    }

    class Template {
        Document document;
        Path templateFile;

        public Template(Path templateFile) {
            this.document = BsonUtil.parseFile(templateFile);
            this.templateFile = templateFile;
        }

        public Template(String json) {
            document = BsonUtil.parse(json);
            templateFile = Paths.get("output.json");
        }
    }
}
