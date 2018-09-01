package uk.dioxic.mgenerate.core;

import org.apache.commons.cli.ParseException;
import uk.dioxic.mgenerate.common.exception.TerminateGenerationException;
import uk.dioxic.mgenerate.core.util.BsonUtil;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) throws IOException, ParseException {

        CliOptions cli = new CliOptions(args);

        Writer writer = new OutputStreamWriter(System.out);

        if (cli.isSingleFileOutput()) {
            writer = new PrintWriter(Files.newBufferedWriter(cli.getOutputPath()));
        }
//        else if (cli.isConsoleOutput()) {
//            writer = new PrintWriter(System.out);
//        }

        Long start = System.currentTimeMillis();

        for (CliOptions.Template template : cli.getTemplates()) {
            DocumentValueCache.mapDocument(template.document);

            if (cli.isMultiFileOutput()) {
                writer = Files.newBufferedWriter(template.templateFile);
            }
            try (PrintWriter pw = new PrintWriter(writer)) {
                Stream.generate(() -> BsonUtil.toJson(template.document))
                        .limit(cli.getDocuments())
                        .forEach(pw::println);
            }
            catch (TerminateGenerationException e) {

            }
        }

        Long end = System.currentTimeMillis();

        Double avg = (end.doubleValue() - start.doubleValue()) / cli.getDocuments().doubleValue();

        Long speed = Double.valueOf(cli.getDocuments().doubleValue() * 1000 / (end - start)).longValue();
        System.out.printf("Producting %s documents took %sms (%s docs/s)%n", cli.getDocuments(), end - start, speed);

    }

}
