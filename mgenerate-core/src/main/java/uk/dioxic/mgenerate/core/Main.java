package uk.dioxic.mgenerate.core;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import uk.dioxic.mgenerate.core.operator.OperatorFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Callable;

@Command(name = "mgenerate")
public class Main implements Callable<Integer> {

    @Option(names = {"-h", "--help"}, usageHelp = true, description = "display a help message")
    private boolean helpRequested = false;

    @Option(names = {"-o", "--out"}, description = "output file path")
    private Path output;

    //@Option(names = {"--type"}, description = "the output format, either json or bson (default: ${DEFAULT-VALUE})", defaultValue = "json")
    //private String type;

    @Option(names = {"-n", "--number"}, description = "number of documents to generate (default: ${DEFAULT-VALUE})", defaultValue = "10")
    private Integer number;

    @Parameters(paramLabel = "TEMPLATE", description = "template file path")
    private Template template;

    public static void main(String[] args) {
        CommandLine cl = new CommandLine(new Main());
        cl.registerConverter(Template.class, s -> Template.from(Paths.get(s)));
        System.exit(cl.execute(args));
    }

    @Override
    public Integer call() {
        if (output != null) {
            //Use try-with-resource to get auto-closeable writer instance
            try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(output))) {
                for (int i = 0; i < number; i++) {
                    writer.println(template.toJson());
                }
            } catch (IOException e) {
                System.out.println("output file [" + output.toString() + "] not writable");
                return 1;
            }
        }
        else {
            try (PrintWriter writer = new PrintWriter(System.out)) {
                for (int i = 0; i < number; i++) {
                    writer.println(template.toJson());
                }
            }
        }

        return 0;
    }

}
