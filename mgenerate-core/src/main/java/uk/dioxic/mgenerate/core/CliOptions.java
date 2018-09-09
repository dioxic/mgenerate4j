package uk.dioxic.mgenerate.core;

import org.apache.commons.cli.*;
import uk.dioxic.mgenerate.core.util.FileUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

public class CliOptions {

    private static final Options opt = new Options();

    static {
        Option template = Option.builder("t")
                .longOpt("template")
                .hasArg()
                .desc("path to template file or directory")
                .required()
                .build();

        Option output = Option.builder("o")
                .longOpt("output")
                .hasArg()
                .desc("output file or directory")
                .build();

        Option number = Option.builder("n")
                .longOpt("number")
                .hasArg()
                .desc("number of documents to generate (default: 5)")
                .type(Integer.class)
                .build();

        opt.addOption(template)
                .addOption(output)
                .addOption(number);
    }

    private Path outputPath;
    private int number = 5;
    private Path templatePath;
    private String jsonTemplate;

    public CliOptions(String[] args) throws CliArgumentException, ParseException {
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd;

        try {
            cmd = parser.parse(opt, args);
        }
        catch (ParseException e) {
            printUsage();
            throw e;
        }

        String templateOptValue = cmd.getOptionValue("t");
        if (templateOptValue.startsWith("{")) {
            // explict template string
            jsonTemplate = templateOptValue;
        }
        else {
            // template file
            templatePath = Paths.get(templateOptValue);
        }

        if (cmd.hasOption("o")) {
            outputPath = Paths.get(cmd.getOptionValue("o"));
        }

        if (cmd.hasOption("n")) {
            number = Integer.valueOf(cmd.getOptionValue("n"));
        }

        if (!Files.exists(templatePath)) {
            throw new CliArgumentException("template [" + templatePath + "] does not exist!");
        }
    }

    public String getJsonTemplate() {
        return jsonTemplate;
    }

    public boolean isTemplateFile() {
        return templatePath != null;
    }

    public Path getTemplatePath() {
        return templatePath;
    }

    public List<Template> getTemplates() throws IOException {
        return isTemplateFile() ? FileUtil.getTemplates(getTemplatePath()) : Collections.singletonList(new Template(getJsonTemplate()));
    }

    public Path getOutputPath() {
        return outputPath;
    }

    public Integer getNumber() {
        return number;
    }

    public boolean isMultiFileOutput() {
        return outputPath != null && Files.isDirectory(outputPath);
    }

    public boolean isSingleFileOuput() {
        return outputPath != null && !Files.isDirectory(outputPath);
    }

    public boolean isConsoleOutput() {
        return outputPath == null;
    }

    public void printUsage() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("mgenerate", opt, true);
    }

}
